package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;

import vidis.ui.model.structure.AGuiContainer;
import vidis.util.ColorGenerator;

import com.sun.opengl.util.GLUT;

public class BasicGuiContainer extends AGuiContainer {

	private boolean opaque = true;
	
	public void setOpaque( boolean value ) {
		this.opaque = value;
	}
	private Color color;
	public BasicGuiContainer() {
		color = ColorGenerator.generateRandomColor();
	}
	public void setColor( Color c ) {
		this.color = c;
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
		gl.glColor4d(color.getRed()/255d, color.getGreen()/255d, color.getBlue()/255d, 0.5);
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
	
	
	
	

}
