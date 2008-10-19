package vidis.ui.model.impl;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;

public class Node extends ASimObject {
	
	public Node(IVariableContainer c) {
		super(c);
	}
	
	private static int displayListId = -1;

	private void drawText(GL gl, String text, double angle, double x, double y, double z, Vector3d move) {
		if(Configuration.DISPLAY_WIREFRAME)
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );
		gl.glPushMatrix();
			gl.glTranslated(0.0 + move.x, 0.8 + move.y, 0.0 + move.z);
			gl.glScaled(0.001, 0.001, 0.001);
			gl.glRotated(angle, x, y, z);
			gl.glRotated(Configuration.LOOK_ANGLE_X, -1, 0, 0);
			gl.glRotated(Configuration.LOOK_ANGLE_Y, 0, -1, 0);
			textRenderer.begin3DRendering();
			textRenderer.setUseVertexArrays(false);
			textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
		// disable wireframe for text
		if(Configuration.DISPLAY_WIREFRAME)
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_LINE );
	}
	
	@Override
	public void renderObject(GL gl) {
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
			gl.glColor3d(0, 1, 0);
			preRenderObject(gl);
		}
		String text = "test";
		try {
			// add text
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
		} catch (NullPointerException e) {
			// may happen, but if, don't care
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
			text = getVariableIds().toString();
		} finally {
			//Rectangle2D bounds = textRenderer.getBounds(text);
			//double width = bounds.getWidth();
			//double scale = (width/2) / 350;
			// front
			gl.glPushMatrix();
				// rotate the whole thingy by 180
//				gl.glRotated(180, 0, 1, 0);
				drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
//				// front
//				drawText(gl, text, 0, 0, 1, 0, new Vector3d(scale,0,scale*1));
//				// right
//				drawText(gl, text, 90, 0, 1, 0, new Vector3d(scale*-1,0,-scale));
//				// back
//				drawText(gl, text, 180, 0, 1, 0, new Vector3d(-scale,0,scale*-1));
//				// left
//				drawText(gl, text, 270, 0, 1, 0, new Vector3d(scale*1,0,scale));
			gl.glPopMatrix();
		}
		// draw node
		if ( mouse ) {
			gl.glColor3d( 0, 0, 1 );
		}
		else {
			gl.glColor3d( 0, 1, 0 );
		}
		gl.glCallList( displayListId );
	}
	
	public void preRenderObject(GL gl) {
		int slices_min = 6;
		int slices_max = 25;
		int stacks_min = 6;
		int stacks_max = 25;
		int slices = (int)Math.round(Configuration.DETAIL_LEVEL * slices_max + slices_min);
		int stacks = (int)Math.round(Configuration.DETAIL_LEVEL * stacks_max + stacks_min);
		requireTextRenderer();
		gl.glNewList( displayListId, GL.GL_COMPILE );
			glut.glutSolidSphere( 0.5, slices, stacks );
		gl.glEndList();
	}

	@Override
	protected void handleEvent(IVidisEvent e) {
		System.err.println(e);
	}
	
	@Override
	public double getHitRadius() {
		return 0.5;
	}

	
	private boolean mouse = false;
	@Override
	public void onMouseIn() {
		mouse = true;
	}

	@Override
	public void onMouseOut() {
		mouse = false;
	}

	public String getId() {
		return (String) getVariableById( AVariable.COMMON_IDENTIFIERS.ID ).getData();
	}

}
