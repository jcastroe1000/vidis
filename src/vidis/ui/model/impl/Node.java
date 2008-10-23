package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.guielements.Mode;
import vidis.ui.model.impl.guielements.variableDisplays.CompositeScrollPane;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class Node extends ASimObject {
	
	private class NodeGuiElement extends BasicGuiContainer {
		
		Mode mode = Mode.MINIMIZED;
		
		//private static Logger logger = Logger.getLogger(NodeGuiElement.class);
		
		private double minHeight = 2;
		private double normHeight = 4;
		private double expHeight = 15;
		
		private CompositeScrollPane scrollPane;
		
		public NodeGuiElement() {
			this.color1 = Color.green;
			this.setOpaque(true);
			
			scrollPane = new CompositeScrollPane( Node.this.getVariableContainer() );
			scrollPane.setLayout( new ILayout() {

				public double getHeight() {
					return expHeight - normHeight;
				}

				public double getWidth() {
					return NodeGuiElement.this.getWidth();
				}

				public double getX() {
					return 0;
				}

				public double getY() {
					switch ( mode ) {
						case MINIMIZED:
							return Double.MAX_VALUE;
						case NORMAL:
							return Double.MAX_VALUE;
						case EXPANDED:
							return 0;
					}
					return Double.MAX_VALUE;
				}

				public void layout() {
					// nothing dude
				}

				public void setGuiContainer(IGuiContainer c) {
					// nothing dude
				}
				
			});
			this.addChild( scrollPane );
			
			// debug inhalt
			scrollPane.addChild( new BasicGuiContainer() );
			
		}
		
		@Override
		public void renderContainer(GL gl) {
			requireTextRenderer();
			
			double textH = this.textH * 0.01;
			switch ( mode ) {
			case MINIMIZED:
				double h2e = minHeight / 2d;
				gl.glPushMatrix();
					gl.glTranslated(h2e, h2e, 0);
					Node.this.renderObject(gl);
				gl.glPopMatrix();
				gl.glPushMatrix();
					gl.glTranslated(2 * h2e, h2e-textH/2.0, 0);
					renderObjectText( gl, 0.01 );
				gl.glPopMatrix();
				break;
			case NORMAL:
				double h2e2 = normHeight / 2d;
				double nfac = normHeight / minHeight;
				gl.glPushMatrix();
					gl.glTranslated(h2e2, h2e2, 0);
					gl.glScaled(nfac, nfac, 1d);
					Node.this.renderObject(gl);
				gl.glPopMatrix();
				gl.glPushMatrix();
					gl.glTranslated(2 * h2e2, normHeight-textH/2.0-minHeight/2d, 0);
					renderObjectText( gl, 0.01 );
				gl.glPopMatrix();
				break;
			case EXPANDED:
				double h2e3 = normHeight / 2d;
				double nfac3 = normHeight / minHeight;
				gl.glPushMatrix();
					gl.glTranslated(h2e3, h2e3 + expHeight - normHeight, 0);
					gl.glScaled(nfac3, nfac3, 1d);
					Node.this.renderObject(gl);
				gl.glPopMatrix();
				gl.glPushMatrix();
					gl.glTranslated(2 * h2e3, expHeight-textH/2.0-minHeight/2d, 0);
					renderObjectText( gl, 0.01 );
				gl.glPopMatrix();
				break;
			}
			
			super.renderContainer(gl);
		}
		
		@Override
		public double getWantedHeight() {
			switch ( mode ) {
				case MINIMIZED: return minHeight;
				case NORMAL: return normHeight;
				case EXPANDED: return expHeight;
				default: return -1;
			}
		}

		@Override
		protected void onMouseClicked(MouseClickedEvent e) {
			if ( mode == Mode.MINIMIZED ) mode = Mode.NORMAL;
			else if ( mode == Mode.NORMAL ) mode = Mode.EXPANDED;
			else if ( mode == Mode.EXPANDED ) mode = Mode.MINIMIZED;
		}
	}
	

	
	public Node(IVariableContainer c) {
		super(c);
		guiObj = new NodeGuiElement();
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
	
	private String text;
	
	public void renderObjectText( GL gl ) {
		renderObjectText( gl, 0.001 );
	}
	
	public void renderObjectText( GL gl, double scale ) {
		gl.glPushMatrix();
//			drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
			gl.glScaled( scale, scale, scale );
			textRenderer.begin3DRendering();
			textRenderer.setUseVertexArrays(false);
			textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
	
	@Override
	public void renderObject(GL gl) {
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
			gl.glColor3d(0, 1, 0);
			preRenderObject(gl);
		}
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
//			drawText(gl, text, 0, 0, 1, 0, new Vector3d(0, 0, 0));
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
	
	public void kill() {
		try {
			guiObj.getParent().getParent().removeChild( guiObj );
		} catch (NullPointerException e) {
			// may happen if not opened in the gui
		}
	}

}
