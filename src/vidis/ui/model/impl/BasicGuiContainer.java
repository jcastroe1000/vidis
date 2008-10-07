package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;
import vidis.ui.model.structure.AGuiContainer;
import vidis.util.ColorGenerator;

public class BasicGuiContainer extends AGuiContainer {

	private static Logger logger = Logger.getLogger( BasicGuiContainer.class );	
	
	private boolean opaque = true;
	
	public void setOpaque( boolean value ) {
		this.opaque = value;
	}
	
	protected synchronized Color getColor() {
		if ( color == null ) {
			color = color1;
		}
		return color;
	}
	private Color color;
	protected Color color1;
	protected Color color2 = Color.red;
	public BasicGuiContainer() {
		color1 = ColorGenerator.generateRandomColor();
	}
	public void setColor1( Color c ) {
		this.color1 = c;
	}
	public void setColor2( Color c ) {
		this.color2 = c;
	}
	
	void renderStrokeString(GL gl, int font, String string, double contwith) {
		// Center Our Text On The Screen
        float strwidth = glut.glutStrokeLengthf(font, string);
        
        double scale = contwith / strwidth;
        gl.glTranslated(0, 2*scale , 0); // FIXME
        gl.glScaled(scale, -scale, 1);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(font, c);
        }
    }
	
	@Override
	public void renderContainer(GL gl) {
//		gl.glPushMatrix();
//		renderStrokeString(gl, GLUT.STROKE_MONO_ROMAN, "BasicGuiContainer", getWidth());
//		gl.glPopMatrix();
		gl.glPushMatrix();
		try {
			gl.glColor4d(getColor().getRed()/255d, getColor().getGreen()/255d, getColor().getBlue()/255d, 0.5);
		}
		catch ( Exception e ) {
			gl.glColor4d(0d, 1d, 0d, 0.5);
			logger.error( "error setting color", e );
		}
			if (opaque) {
				gl.glBegin(GL.GL_QUADS); 
					gl.glVertex2d(0, 0);
					gl.glVertex2d(0, getHeight());
					gl.glVertex2d(getWidth(), getHeight());
					gl.glVertex2d(getWidth(), 0);	
				gl.glEnd();
			}
			// debugging spheres
//			gl.glColor3d(0, 1, 0);
//			gl.glPushMatrix();
//				gl.glTranslated(0, 0, 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(getWidth(), 0, 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(getWidth(), getHeight(), 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(0, getHeight(), 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	@Override
	protected void onMouseEnter() {
		logger.info( "onMouseEnter() on " + this );
		color = color2;
	}
	@Override
	protected void onMouseExit() {
		logger.info( "onMouseExit() on " + this );
		color = color1;
	}
	
	
	private String name;
	public void setName( String name ) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name==null?super.toString():this.name;
	}

	@Override
	protected void onMouseClicked(MouseClickedEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMouseClicked() on " + this );
	}

	@Override
	protected void onMousePressed(MousePressedEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMousePressed() on " + this );
	}

	@Override
	protected void onMouseReleased(MouseReleasedEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMouseReleased() on " + this );
	}

}
