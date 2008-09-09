package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.Font;

import javax.media.opengl.GL;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;

import com.sun.opengl.util.j2d.TextRenderer;


public class Packet extends ASimObject {

	public Packet( IVariableContainer c, Link link ) {
		super(c);
		link.addPacket( this );
	}

	private static int displayListId = -1;
	
	private static TextRenderer textRenderer = new TextRenderer( new Font("Times New Roman", Font.PLAIN, 130 ), true, true );
	
	private double position = Math.random()*360;
	
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
		// set color
		gl.glColor4d( 1, 0, 1, 0 );
		// now rotate it
		gl.glRotated(position, getPosition().getX(), getPosition().getY(), getPosition().getZ());
		position += Math.random()*10+4;
		// now draw it
		gl.glCallList( displayListId );
	}
	
	public void preRenderObject( GL gl ) {
		displayListId = gl.glGenLists( 1 );
		gl.glNewList( displayListId, GL.GL_COMPILE );
			glut.glutSolidSphere(.15f, 6, 6);
		gl.glEndList();
	}

	@Override
	protected void handleEvent( IVidisEvent e ) {
		
	}

	public Vector3d getPosition() {
		try {
			Tuple3d pos = (Tuple3d) getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
			return new Vector3d( pos );
		}
		catch ( Exception e ) {
			return null;
		}
	}


}
