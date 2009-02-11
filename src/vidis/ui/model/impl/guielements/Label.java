/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements;

import javax.media.opengl.GL;

import vidis.ui.model.impl.BasicGuiContainer;

/**
 * this class draws a label
 * 
 * @author Dominik
 *
 */
public class Label extends BasicGuiContainer {
//	private static Logger logger = Logger.getLogger(Label.class);
	
	private String label;
	
	public Label(String label) {
		requireTextRenderer();
		setLabel(label);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public void renderContainer(GL gl) {
		gl.glPushMatrix();
			//gl.glTranslated(0, 0, textRenderer.getBounds(label).getHeight());
			gl.glScaled(0.01, 0.01, 0.01);
			textRenderer.begin3DRendering();
			textRenderer.draw3D(label, 0, 0, 0, 1f);
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
}
