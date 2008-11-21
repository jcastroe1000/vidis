package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;

public class Button extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Button.class);

	private boolean pressed = false;
	
	float borderPercent = 0.05f;
	
	private String text = "BUTTON";
	
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	private Color textColor = Color.white;
	
	public Button() {
		setColor1( Color.gray );
		setColor2( Color.gray.darker() );
	}
	
	@Override
	public void renderContainer(GL gl) {
		double h = getHeight();
		double w = getWidth();
		double border = h * borderPercent;
		// text
		
		requireTextRenderer();
		
		useColor( gl, getColor() );
		
		
		
		useColor( gl, getColor() );
		// center
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, getHeight() - border);
			gl.glVertex2d( getWidth() - border, getHeight() - border );
			gl.glVertex2d( getWidth() - border, border);	
		gl.glEnd();
	
		// rand
		if ( !pressed ) {
			useColor( gl, colorLight );
		}
		else {
			useColor( gl, colorDark );
		}

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
		
		if ( !pressed ) {
			useColor( gl, colorDark );
		}
		else {
			useColor( gl, colorLight );
		}
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
		Rectangle2D r = textRenderer.getBounds( "Apdq[]" );
		float scale = 0.01f;
		
		double factor = 0.7;
		double fontScaleWidth = (w * factor) / (r.getWidth() * scale);
		double fontScaleHeight = (h * factor) / (r.getHeight() * scale);
		double fontScale = fontScaleHeight;
		
		gl.glPushMatrix();
			
		//gl.glScaled( fontScale, fontScale, 1);
		if ( pressed ) {
				gl.glTranslated( border/2d, -border/2d, 0 );
			}
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
			textRenderer.draw3D( text, 
					(float) (w / 2f - r.getWidth() * scale * fontScale / 2f), 
					(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
					0.5f,
					(float) (scale * fontScale) );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
	
	private void useColor( GL gl, Color c ) {
		gl.glColor4d(c.getRed()/255d, c.getGreen()/255d, c.getBlue()/255d, c.getAlpha()/255d);
	}
	
	@Override
	protected void onMousePressed(MousePressedEvent e) {
		pressed = true;
	}
	@Override
	protected void onMouseReleased(MouseReleasedEvent e) {
		pressed = false;
	}
	
	public void setText( String text ) {
		this.text = text;
	}
}
