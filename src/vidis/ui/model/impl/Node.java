package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseMovedEvent;
import vidis.ui.model.impl.guielements.ObjectGuiElement;
import vidis.ui.model.impl.guielements.variableDisplays.CompositeScrollPane;
import vidis.ui.model.structure.ASimObject;

public class Node extends ASimObject {
	
	private class NodeGuiElement extends ObjectGuiElement {
		//private static Logger logger = Logger.getLogger(NodeGuiElement.class);
		
		
		public NodeGuiElement() {
			init();
		}
		
		protected CompositeScrollPane initScrollPane() {
			return new CompositeScrollPane( Node.this.getVariableContainer() );
		}
		
		protected IVariableContainer fetchVariableContainer() {
			return Node.this.getVariableContainer();
		}

		protected void renderObject( GL gl ) {
			Node.this.renderObject(gl);
		}

		protected void renderText2( GL gl ) {
			renderObjectText( gl, 0.01 );
		};
		
		@Override
		protected synchronized void onMouseEnter(MouseMovedEvent e) {
			Node.this.setHighlighted( true );
			super.onMouseEnter(e);
		}
		
		@Override
		protected synchronized void onMouseExit(MouseMovedEvent e) {
			Node.this.setHighlighted( false );
			super.onMouseExit(e);
		}

	}
	
	public Node(IVariableContainer c) {
		super(c);
		guiObj = new NodeGuiElement();
	}
	
	protected Color getDefaultColor() {
		return Color.red;
	}
	
	protected boolean isMouseOver() {
		return mouse;
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
	
	private String text = "";
	private double lastDetailLevel = Configuration.DETAIL_LEVEL;
	
	public void renderObjectText( GL gl ) {
		renderObjectText( gl, 0.001 );
	}
	
	public void renderObjectText( GL gl, double scale ) {
//		gl.glPushMatrix();
////			drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
//			gl.glScaled( scale, scale, scale );
//			textRenderer.begin3DRendering();
//			textRenderer.setUseVertexArrays(false);
//			textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
//			textRenderer.end3DRendering();
//		gl.glPopMatrix();
		
		
		text = "test";
		try {
			// add text
			text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
		} catch (NullPointerException e) {
			// may happen, but if, don't care
			try {
				text = getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
			} catch (NullPointerException e2) {
				try {
					text = getVariableIds().toString();
				} catch (NullPointerException e3) {
					text = "huh?";
				}
			}
		} finally {
			drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
		}
		
	}
	
	@Override
	public void renderObject(GL gl) {
		setColors( getVariableColor1(), getVariableColor2() );
		useColor( gl, getVariableColor1() );
		useMaterial(gl);
		if ( displayListId == -1 || lastDetailLevel  != Configuration.DETAIL_LEVEL ) {
			preRenderObject(gl);
		}
		
		gl.glCallList( displayListId );
	}
	
	public void preRenderObject(GL gl) {
		lastDetailLevel = Configuration.DETAIL_LEVEL;
		if ( displayListId != -1 ) {
			gl.glDeleteLists(displayListId, 1);
			displayListId = -1;
		}
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
		}
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
		setHighlighted( true );
		mouse = true;
		((NodeGuiElement)guiObj).setHighlighted( true );
	}

	@Override
	public void onMouseOut() {
		setHighlighted( false );
		mouse = false;
		((NodeGuiElement)guiObj).setHighlighted( false );
	}

	public String getId() {
		return (String) getVariableById( AVariable.COMMON_IDENTIFIERS.ID ).getData();
	}
	
	public void kill() {
		try {
			guiObj.getParent().getParent().removeChild( guiObj );
		} catch (NullPointerException e) {
			// may happen if not opened in the gui
		}
	}

}
