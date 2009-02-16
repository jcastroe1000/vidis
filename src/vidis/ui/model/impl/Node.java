/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;

import vidis.data.var.IVariableContainer;
import vidis.data.var.vars.AVariable;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.model.impl.guielements.ObjectGuiElement;
import vidis.ui.model.impl.guielements.variableDisplays.CompositeScrollPane;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;

/**
 * Visual representation of a Node
 * @author Christoph
 *
 */
public class Node extends ASimObject {
	
	/**
	 * The GUI Element that is used in the Right Panel
	 * @author Christoph
	 *
	 */
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

//		protected void renderText2( GL gl ) {
//			renderObjectText( gl, 0.01 );
//		};
		
		@Override
		protected synchronized void onMouseEnter(AMouseEvent e) {
			Node.this.setHighlighted( true );
			super.onMouseEnter(e);
		}
		
		@Override
		protected synchronized void onMouseExit(AMouseEvent e) {
			Node.this.setHighlighted( false );
			super.onMouseExit(e);
		}
		
		@Override
		protected boolean hasHeader1() {
			String id = "user.header1";
			return fetchVariableContainer().hasVariable(id);
		}
		
		@Override
		protected boolean hasHeader2() {
			String id = "user.header2";
			return fetchVariableContainer().hasVariable(id);
		}
		
		@Override
		protected String getHeaderLine1() {
			String key = "user.header1";
			AVariable v = fetchVariableContainer().getVariableById( key );
			if ( v != null ) {
				return (String) v.getData();
			}
			else {
				// fallback to ID
				return (String) fetchVariableContainer().getVariableById( AVariable.COMMON_IDENTIFIERS.ID ).getData();
			}
		}

		@Override
		protected String getHeaderLine2() {
			String key = "user.header2";
			AVariable v = fetchVariableContainer().getVariableById( key );
			if ( v != null ) {
				return (String) v.getData();
			}
			else {
				// fallback to empty string
				return "";
			}
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
		textRenderer.setColor( Color.black );
		
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
		
		// render node
		setColors( getVariableColor1(), getVariableColor2() );
		useColor( gl, getVariableColor1() );
		useMaterial(gl);
		if ( displayListId == -1 || lastDetailLevel  != Configuration.DETAIL_LEVEL ) {
			preRenderObject(gl);
		}
		gl.glCallList( displayListId );
		
		
		// render highlight
		if ( isHighlighted() ) {
			gl.glPushMatrix();
			//gl.glEnable(GL.GL_BLEND);
			Color old = getVariableColor1();
			setColors( new Color(old.getRed() / 255f, old.getGreen() / 255f, old.getBlue() /255f,0.1f), new Color( 1f, 1f, 1f, 0.5f) );
			useMaterial(gl);
			gl.glScaled(1.2, 1.2, 1.2);
			gl.glCallList( displayListId );
			//gl.glDisable(GL.GL_BLEND);
			
			gl.glPopMatrix();
		}
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
			if ( Configuration.NICE_NODES ) {
				glut.glutSolidSphere( 0.5, slices, stacks );
			}
			else {
				glut.glutSolidSphere( 0.5, 4, 4 );
			}
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
		try {
		return (String) getVariableById( AVariable.COMMON_IDENTIFIERS.ID ).getData();
		}
		catch ( Exception e ) {
			return "I AM NOT AN ID";
		}
	}
	
	public void kill() {
		try {
			guiObj.getParent().getParent().removeChild( guiObj );
		} catch (NullPointerException e) {
			// may happen if not opened in the gui
		}
	}
	
	
	@Override
	public IGuiContainer getOnScreenLabel() {
		return ((ObjectGuiElement)guiObj).getOnScreenLabel();
	}

}
