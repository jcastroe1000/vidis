/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import javax.media.opengl.GL;
import javax.swing.JOptionPane;

import vidis.ui.events.mouse.AMouseEvent;


public class TextGuiContainer extends BasicGuiContainer {

	private String text;
//	private Color color = Color.black;
//	private Color textColor = Color.red;
	
	//private static TextRenderer textRenderer = new TextRenderer( new Font("Times New Roman", Font.PLAIN, 130 ), true, true );
	
	@Override
	public void renderContainer(GL gl) {
		requireTextRenderer();
		gl.glPushMatrix();
			//gl.glTranslated(0, 0, textRenderer.getBounds(label).getHeight());
			gl.glScaled(0.01, 0.01, 0.01);
			textRenderer.begin3DRendering();
			textRenderer.draw3D(text, 0, 0, 0, 1f);
			textRenderer.end3DRendering();
		gl.glPopMatrix();
		super.renderContainer(gl);
//		renderStrokeString(gl, GLUT.STROKE_ROMAN, text, getWidth());
//		Rectangle2D bounds = textRenderer.getBounds(text);
//		gl.glPushMatrix();
//		float scale = (float) getWidth() / (float) bounds.getWidth();
//		gl.glTranslated(0, 2, 0);
//		gl.glScaled( scale, -scale, 1 );
//		textRenderer.begin3DRendering();
//		textRenderer.setUseVertexArrays(false);
//		textRenderer.draw3D( text, 0f, 0f, 0f, 0.001f );
//		textRenderer.end3DRendering();
//		gl.glPopMatrix();
	}
	
	public void setText(String txt){
		this.text = txt;
	}
	
	@Override
	protected void onMouseClicked( AMouseEvent e ) {
		JOptionPane.showMessageDialog(null, "you clicked on an TextGuiContainer!");
	}

}
