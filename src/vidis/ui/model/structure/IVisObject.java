package vidis.ui.model.structure;

import javax.media.opengl.GL;

import com.sun.opengl.util.GLUT;


public interface IVisObject {
	
	public static final GLUT glut = new GLUT();
	public void render( GL gl );
	public void renderText( GL gl );
	
	public boolean isTextRenderable();
	public void kill();
}
