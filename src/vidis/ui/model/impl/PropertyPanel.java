/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import javax.media.opengl.GL;

import vidis.ui.model.structure.ASimObject;

public class PropertyPanel extends BasicGuiContainer {
//	private static Logger logger = Logger.getLogger(PropertyPanel.class);

	private ASimObject propertySource;
	
	enum State {
		NORMAL,
		MINIMIZED;
	}
	
	private State state;
	
	@Override
	public void renderContainer(GL gl) {
		switch (state) {
		case NORMAL:
			// render the box
			super.renderContainer(gl);
			
			// render the content
			gl.glPushMatrix();
				// FIXME move it to right position
				propertySource.renderObject(gl);
			gl.glPopMatrix();
			break;
		case MINIMIZED:
			break;
		}
	}
}
	
