/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.variableDisplays;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.model.impl.Button;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class MethodDisplay extends Display {
	private static Logger logger = Logger.getLogger(MethodDisplay.class);

	private Button exec;
	
	public MethodDisplay ( AVariable v ) {
		super(v);
		final MethodVariable var = (MethodVariable) v;
		exec = new Button() {
			protected void onMouseClicked(AMouseEvent e) {
				if ( var.getMethodExpectsParameters() ) {
					// FIXME
				}
				else {
					var.getData();
				}
			}
		};
		
		exec.setText( v.getIdentifier() );
		this.setText( "" );
		this.addChild( exec );
		exec.setLayout(new ILayout() {
			public double getHeight() {
				return 1.5;
			}
			public double getWidth() {
				return MethodDisplay.this.getWidth();
			}
			public double getX() {
				return 0;
			}
			public double getY() {
				return 0;
			}
			public void layout() {
			}
			public void setGuiContainer(IGuiContainer c) {
			}
		});
	}
	
	@Override
	public double getWantedHeight() {
		return 1.5;
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new CollectionDisplay( var );
//	}
	
	@Override
	public void renderContainer(GL gl) {
		super.renderContainer(gl);
	}
}
