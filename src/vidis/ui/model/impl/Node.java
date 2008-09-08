package vidis.ui.model.impl;

import javax.media.opengl.GL;

import vidis.data.var.IVariableContainer;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;

public class Node extends ASimObject {
	
	public Node(IVariableContainer c) {
		super(c);
	}
	
	private static int displayListId = -1;

	@Override
	public void renderObject(GL gl) {
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
			preRenderObject(gl);
		}
		gl.glColor3d( 1, 0, 0 );
		gl.glCallList( displayListId );
	}
	
	public void preRenderObject(GL gl) {
		gl.glNewList( displayListId, GL.GL_COMPILE );
			glut.glutSolidSphere(0.5, 20, 20);
		gl.glEndList();
	}

	@Override
	protected void handleEvent(IVidisEvent e) {
		// TODO Auto-generated method stub
		
	}

}
