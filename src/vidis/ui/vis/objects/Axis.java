/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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

	public boolean isTextRenderable() {
		// TODO Auto-generated method stub
		return false;
	}

	public void renderText(GL gl) {
		// TODO Auto-generated method stub
		
	}
}
