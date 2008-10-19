package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.mvc.api.Dispatcher;

public class NodeField extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(NodeField.class);

	float borderPercent = 0.05f;
	
	/**
	 * represents the node
	 */
	private Node node = null;
	
	/**
	 * represents the shown substring 
	 */
	private String shownText = "";
	
	private boolean selecting = false;
	
	/**
	 * 
	 */
	private int shownChars = 20;
	
	/**
	 * 
	 */
	private int textOffset = 0;
	
	/**
	 * 
	 */
	private int cursorPosition = 0;
	// border colors
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	// text color
	private Color textColor = Color.black;
	// background
	private Color backColor = Color.white;
	
	public NodeField() {
		setColor1( backColor );
		setColor2( backColor.darker() );
	}
	
	@Override
	public void renderContainer(GL gl) {
		double h = getHeight();
		double w = getWidth();
		double border = h * borderPercent;

		double innerH = h - 2 * border;
		double innerW = h - 2 * border;
		
		requireTextRenderer();
		
		useColor( gl, getColor() );
		// center
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, getHeight() - border);
			gl.glVertex2d( getWidth() - border, getHeight() - border );
			gl.glVertex2d( getWidth() - border, border);	
		gl.glEnd();
	
		// rand
		useColor( gl, colorDark );

		//  bottom
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, h );
			gl.glVertex2d( border, h - border);
			gl.glVertex2d( w - border, h - border );
			gl.glVertex2d( w, h );	
		gl.glEnd();
		//  left
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, 0);
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, h - border );
			gl.glVertex2d( 0, h );	
		gl.glEnd();
		
		useColor( gl, colorLight );
		//  top
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, 0);
			gl.glVertex2d( w, 0 );
			gl.glVertex2d( w - border, border );
			gl.glVertex2d( border, border );	
		gl.glEnd();
	//  right
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( w, 0);
			gl.glVertex2d( w, h);
			gl.glVertex2d( w - border, h - border );
			gl.glVertex2d( w - border, border );	
		gl.glEnd();
		
		// text
		String text = node==null?"<no node selected>":"<Node: " + node.getId() + ">";
		
		if ( selecting ) {
			text = "[select a node]";
		}
		
		Rectangle2D r = textRenderer.getBounds( text );
		float scale = 0.01f;
		
		double factor = 0.7;
		double fontScaleWidth = (w * factor) / (r.getWidth() * scale);
		double fontScaleHeight = (h * factor) / (r.getHeight() * scale);
		double fontScale = fontScaleHeight;
		
		gl.glPushMatrix();
			
		//gl.glScaled( fontScale, fontScale, 1);
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
			int endIndex = textOffset + shownChars;
			if ( endIndex > text.length() ) {
				endIndex = text.length();
			}
			String s = text.substring( textOffset, endIndex );
			textRenderer.draw3D(  s, 
					(float) ( border + border ), 
					(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
					0.5f,
					(float) (scale * fontScale) );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
		
		if ( cursorPosition >= textOffset && cursorPosition < text.length() - textOffset ) {
		// cursor position
		double cursorX = 0;
		double cursorY = border;
		double cursorW = 0.1;
		double cursorH = innerH;
		
		useColor(gl, new Color( 1f, 0f, 0f, 0.4f ) );
		gl.glPushMatrix();
			gl.glTranslated(0, 0, 0.5);
			gl.glBegin( GL.GL_QUADS );
				gl.glVertex2d( cursorX, cursorY );
				gl.glVertex2d( cursorX, cursorY + cursorH );
				gl.glVertex2d( cursorX + cursorW, cursorY + cursorH );
				gl.glVertex2d( cursorX + cursorW, cursorY );
			gl.glEnd();
		gl.glPopMatrix();
		
		}
	}
	
	private void useColor( GL gl, Color c ) {
		gl.glColor4d(c.getRed()/255d, c.getGreen()/255d, c.getBlue()/255d, c.getAlpha()/255d);
	}
	
	@Override
	protected void onMousePressed(MousePressedEvent e) {
	}
	@Override
	protected void onMouseReleased(MouseReleasedEvent e) {
	}
	@Override
	protected void onMouseClicked(MouseClickedEvent e) {
		startNodeCapturing();
	}
	
	public void setNode( Node node ) {
		selecting = false;
		this.node = node;
	}
	
	public Node getNode() {
		return this.node;
	}
	
	public void startNodeCapturing() {
		selecting = true;
		VidisEvent<NodeField> next = new VidisEvent<NodeField>( IVidisEvent.StartNodeCapturing, this );
		Dispatcher.forwardEvent( next );
	}
}
