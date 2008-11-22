package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

import org.apache.log4j.Logger;

import vidis.ui.vis.camera.GuiCamera;

public abstract class AScissorContainer extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(AScissorContainer.class);

	
	public AScissorContainer() {
		super();
		color1 = Color.blue;
	}
	
	
	@Override
	public void renderContainer(GL gl) {
		// container border and background
		gl.glPushMatrix();
			setColor(gl);
			gl.glBegin(GL.GL_QUADS); 
				gl.glVertex2d(0, 0);
				gl.glVertex2d(0, getHeight());
				gl.glVertex2d(getWidth(), getHeight());
				gl.glVertex2d(getWidth(), 0);	
			gl.glEnd();
			// debugging spheres
			gl.glColor3d(0, 1, 0);
			gl.glPushMatrix();
				gl.glTranslated(0, 0, 0);
				glut.glutWireSphere(0.2, 4, 4);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(getWidth(), 0, 0);
				glut.glutWireSphere(0.2, 4, 4);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(getWidth(), getHeight(), 0);
				glut.glutWireSphere(0.2, 4, 4);
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(0, getHeight(), 0);
				glut.glutWireSphere(0.2, 4, 4);
			gl.glPopMatrix();
		gl.glPopMatrix();
		// container content
		
		Point2i pos = GuiCamera.convert3Dto2D( new Point2d( getX(), getY() ));
		Point2i hw = GuiCamera.convert3Dto2D( new Point2d( getWidth(), getHeight() ));
		// set the scissor
		gl.glScissor( pos.x, pos.y, hw.x, hw.y );
		// draw the content
		gl.glEnable( GL.GL_SCISSOR_TEST );
		gl.glColor3d(0, 1, 0);
		gl.glPushMatrix(); {
			//gl.glTranslated(getWidth() / 2d, getHeight() / 2d, 0);
			renderScissoredContent(gl);
		}
		gl.glPopMatrix();
		gl.glDisable( GL.GL_SCISSOR_TEST );
	}
	
	protected abstract void renderScissoredContent( GL gl );
	
}
