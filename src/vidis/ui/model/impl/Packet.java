package vidis.ui.model.impl;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;


public class Packet extends ASimObject {

	public Packet( IVariableContainer c, Link link ) {
		super(c);
		link.addPacket( this );
	}

	private static int displayListId = -1;
	
	private double position = Math.random()*360;
	
	private void drawText(GL gl, String text, double angle, double x, double y, double z, Vector3d move) {
		// disable wireframe for text
		if(Configuration.DISPLAY_WIREFRAME)
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );
		gl.glPushMatrix();
			gl.glCullFace(GL.GL_FRONT);
			gl.glFrontFace(GL.GL_CW);
			gl.glTranslated(0.0 + move.x, 0.8 + move.y, 0.0 + move.z);
			gl.glScaled(0.001, 0.001, 0.001);
			gl.glRotated(angle, x, y, z);
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
	public void renderObject( GL gl ) {
		if ( displayListId == -1 ) {
			preRenderObject( gl );
		}
		// add name
		/*
		if(getVariableIds().contains(AVariable.COMMON_IDENTIFIERS.NAME)) {
			textRenderer.begin3DRendering();
			textRenderer.draw3D( getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString(), 0f, 0f, 0f, 1f );
			textRenderer.end3DRendering();
		} else {
			gl.glColor3d(1, 0, 1);
			String text = "test inspector very long, inspector even longer";
			gl.glPushMatrix();
				gl.glScaled(0.001, 0.001, 0.001);
				textRenderer.begin3DRendering();
				textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
				textRenderer.end3DRendering();
			gl.glPopMatrix();
		}*/
		String text = "";
		try {
			// add text
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
		} catch (NullPointerException e) {
			// may happen, but if, don't care
//			try {
//				text = getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
//			} catch (NullPointerException e2) {
//				text = getVariableContainer().toString();
//			}
		} finally {
			if(text.length() > 0) {
				gl.glPushMatrix();
					// rotate the whole thingy by 180 (future rotate by 180 AND camera angle)
					//gl.glRotated(180, 0, 1, 0);
					drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
		//			// front
		//			drawText(gl, text, 0, 0, 1, 0, new Vector3d(scale,0,scale*1));
		//			// right
		//			drawText(gl, text, 90, 0, 1, 0, new Vector3d(scale*-1,0,-scale));
		//			// back
		//			drawText(gl, text, 180, 0, 1, 0, new Vector3d(-scale,0,scale*-1));
		//			// left
		//			drawText(gl, text, 270, 0, 1, 0, new Vector3d(scale*1,0,scale));
				gl.glPopMatrix();
			}
		}
		// set color
		gl.glColor4d( 1, 0, 1, 0 );
		// now rotate it
		gl.glRotated(position, getPosition().x, getPosition().y, getPosition().z);
		position += Math.random()*10+4;
		// now draw it
		gl.glCallList( displayListId );
	}
	
	public void preRenderObject( GL gl ) {
		requireTextRenderer();
		displayListId = gl.glGenLists( 1 );
		gl.glNewList( displayListId, GL.GL_COMPILE );
			glut.glutSolidSphere(.15f, 6, 6);
		gl.glEndList();
	}

	@Override
	protected void handleEvent( IVidisEvent e ) {
		
	}

	public Point3d getPosition() {
		try {
			Tuple3d pos = (Tuple3d) getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
			return new Point3d( pos );
		}
		catch ( Exception e ) {
			return null;
		}
	}

	@Override
	public double getHitRadius() {
		return 0.15;
	}

}
