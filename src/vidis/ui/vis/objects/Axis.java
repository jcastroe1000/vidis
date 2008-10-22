package vidis.ui.vis.objects;


import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.ui.model.structure.IVisObject;

public class Axis implements IVisObject  {

	public void render(GL gl) {
		
		// x axis red
		gl.glColor3f(1f, 0f, 0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0f, 0f, 0f);
			gl.glVertex3f(1f, 0f, 0f);
			gl.glVertex3f(1f, 0f, 0f);
			gl.glVertex3f(0.9f, 0.1f, 0.0f);
			gl.glVertex3f(1f, 0f, 0f);
			gl.glVertex3f(0.9f, -0.1f, 0.0f);
		gl.glEnd();
		// y axis green
		gl.glColor3f(0f, 1f, 0f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0f, 0f, 0f);
			gl.glVertex3f(0f, 1f, 0f);
			gl.glVertex3f(0f, 1f, 0f);
			gl.glVertex3f(0.1f, 0.9f, 0.0f);
			gl.glVertex3f(0f, 1f, 0f);
			gl.glVertex3f(-0.1f, 0.9f, 0.0f);
		gl.glEnd();
		// z axis blue
		gl.glColor3f(0f, 0f, 1f);
		gl.glBegin(GL.GL_LINES);
			gl.glVertex3f(0f, 0f, 0f);
			gl.glVertex3f(0f, 0f, 1f);
			gl.glVertex3f(0f, 0f, 1f);
			gl.glVertex3f(0f, 0.1f, 0.9f);
			gl.glVertex3f(0f, 0f, 1f);
			gl.glVertex3f(0f, -0.1f, 0.9f);
		gl.glEnd();

	}

	public Vector3d getPos() {
		return new Vector3d(0, 0, 0);
	}

	public void kill() {
		// TODO Auto-generated method stub
		
	}
}
