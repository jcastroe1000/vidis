package vidis.ui.vis.objects;


import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.ui.model.structure.IVisObject;

/**
 * Draws a Grid
 * @author Christoph
 *
 */
public class Grid implements IVisObject {

	private double x=-100;
	private double z=-100;
	private double deltax=2.0;
	private double deltaz=2.0;
	private double yoff = 0.02;
	private int anzx=100;
	private int anzz=100;
	public Grid(){
		
	}
	public void render(GL gl) {
		// draw Lines
		gl.glColor3f(0.5f, 0.5f, 0.5f);
      	gl.glLineWidth(0.5f);
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
		gl.glPointSize(2f);
		gl.glColor3f(0.7f, 0.7f, 0.7f);
		gl.glBegin(GL.GL_POINTS);
      	for (double ix=x; ix <=x+anzx*deltax; ix+=deltax)
      		for (double iz=z;iz <=z+anzz*deltaz; iz+=deltaz)
      			gl.glVertex3d(ix, yoff, iz);
      	gl.glEnd();
      
      	
    }
	public Vector3d getPos() {
		return new Vector3d(this.x, 0, this.z);
	}
}
