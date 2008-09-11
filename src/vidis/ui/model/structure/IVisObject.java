package vidis.ui.model.structure;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;

import com.sun.opengl.util.GLUT;


public interface IVisObject {
	public static final GLUT glut = new GLUT();
	public void render( GL gl );
}
