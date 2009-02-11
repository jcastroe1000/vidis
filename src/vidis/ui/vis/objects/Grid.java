/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.vis.objects;


import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.ui.config.Configuration;
import vidis.ui.model.structure.IVisObject;

/**
 * Draws a Grid
 * @author Christoph
 *
 */
public class Grid implements IVisObject {

	private double x=-100;
	private double z=-100;
	private double deltax=Configuration.GRID_STEP;
	private double deltaz=Configuration.GRID_STEP;
	private double yoff = 0.02;
	private int anzx=100;
	private int anzz=100;
	private float lWidth = 0.01f;
	public Grid(){
		
	}
	public void render(GL gl) {
		// draw Lines
		gl.glColor3f(0.95f, 0.95f, .95f);
      	gl.glLineWidth(lWidth);
      	gl.glBegin(GL.GL_LINES);
      	for (double ix=x; ix <=x+anzx*deltax; ix+=deltax){
      		gl.glVertex3d(ix, yoff, z);
      		gl.glVertex3d(ix, yoff, z+anzz*deltaz);
      	}
      	for (double iz=z;iz <=z+anzz*deltaz; iz+=deltaz){
      		gl.glVertex3d(x, yoff, iz);
      		gl.glVertex3d(x+anzx*deltax, yoff, iz);
      	}
      	gl.glEnd();
      	// draw Points
//		gl.glPointSize(2f);
//		gl.glColor3f(0.7f, 0.7f, 0.7f);
//		gl.glBegin(GL.GL_POINTS);
//      	for (double ix=x; ix <=x+anzx*deltax; ix+=deltax)
//      		for (double iz=z;iz <=z+anzz*deltaz; iz+=deltaz)
//      			gl.glVertex3d(ix, yoff, iz);
//      	gl.glEnd();
    }
	public Vector3d getPos() {
		return new Vector3d(this.x, 0, this.z);
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
