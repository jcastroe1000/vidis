package vidis.ui.vis.objects;


import javax.media.opengl.GL;
import javax.vecmath.Point3d;

import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IVisObject;

public class Selector implements IVisObject  {

	private ASimObject selection;
	private double angle = 0;
	
	public void render(GL gl) {
		angle += 1; //Math.PI / 36;
		if ( angle > 360 ) angle -= 360; //2* Math.PI) angle = 0;
		gl.glPushMatrix();
		gl.glTranslated( getPos().x, getPos().y, getPos().z );
		gl.glRotated(angle, 0, 1, 0);
		gl.glColor3f(1f, 0f, 1f);
		double size = 0.3;
		gl.glBegin( GL.GL_LINES );
			gl.glVertex3d( 0, 0, 0 );
			gl.glVertex3d( 1 * size, 2 * size, 1 * size );
			gl.glVertex3d( 0, 0, 0 );
			gl.glVertex3d( -1 * size, 2 * size, 1 * size );
			gl.glVertex3d( 0, 0, 0 );
			gl.glVertex3d( -1 * size, 2 * size, -1 * size );
			gl.glVertex3d( 0, 0, 0 );
			gl.glVertex3d( 1 * size, 2 * size, -1 * size );
			
			gl.glVertex3d( 1 * size, 2 * size, 1 * size );
			gl.glVertex3d( -1 * size, 2 * size, 1 * size );
			gl.glVertex3d( -1 * size, 2 * size, 1 * size );
			gl.glVertex3d( -1 * size, 2 * size, -1 * size );
			gl.glVertex3d( -1 * size, 2 * size, -1 * size );
			gl.glVertex3d( 1 * size, 2 * size, -1 * size );
			gl.glVertex3d( 1 * size, 2 * size, -1 * size );
			gl.glVertex3d( 1 * size, 2 * size, 1 * size );
		gl.glEnd();
		gl.glPopMatrix();
	}

	public Point3d getPos() {
		if ( selection != null ) {
			Point3d v = selection.getPosition();
			Point3d ret = new Point3d();
			ret.add( v, new Point3d( 0, selection.getHitRadius(), 0 ));
			return ret;
		}
		else {
			return new Point3d( 3453,345,345 );
		}
	}
	
	public void resetSelection() {
		selection = null;
	}
	
	public void setSelectedObject( ASimObject obj ) {
		selection = obj;
	}
	
	public IVisObject getSelectedObject() {
		return selection;
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
