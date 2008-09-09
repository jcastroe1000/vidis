package vidis.ui.model.impl;

import java.awt.Font;

import javax.media.opengl.GL;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;

import com.sun.opengl.util.j2d.TextRenderer;

public class Node extends ASimObject {
	
	public Node(IVariableContainer c) {
		super(c);
	}
	
	private static int displayListId = -1;
	
	private static TextRenderer textRenderer = new TextRenderer( new Font("Times New Roman", Font.PLAIN, 130 ), true, true );

	private void drawText(GL gl, String text, double angle, double x, double y, double z) {
		gl.glPushMatrix();
			gl.glTranslated(0.0, 0.8, 0.0);
			gl.glScaled(0.001, 0.001, 0.001);
			gl.glRotated(angle, x, y, z);
			textRenderer.begin3DRendering();
			textRenderer.setUseVertexArrays(false);
			textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
			textRenderer.end3DRendering();
			gl.glCullFace(GL.GL_FRONT);
			gl.glFrontFace(GL.GL_CW);
		gl.glPopMatrix();
	}
	
	@Override
	public void renderObject(GL gl) {
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
			preRenderObject(gl);
		}
		String text = "test";
		try {
			// add text
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
		} catch (NullPointerException e) {
			// may happen, but if, don't care
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
		} finally {
			drawText(gl, text, 0, 0, 1, 0);
			drawText(gl, text, 90, 0, 1, 0);
			drawText(gl, text, 180, 0, 1, 0);
			drawText(gl, text, 270, 0, 1, 0);
		}
		// draw node
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
