/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Label;
import vidis.ui.model.impl.PercentMarginLayout;

public abstract class PopupWindow extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(PopupWindow.class);
	
	public PopupWindow(String title) {
		color1 = new Color(150, 150, 150, 150);
		color2 = new Color(150, 150, 150, 0);
		this.setLayout( new PercentMarginLayout(-0.05, -0.5, -0.35, -0.15, -0.35, -0.6));
		
		// titlebar
		Label tmp = new Label(title);
		tmp.setLayout( new PercentMarginLayout(0, -0.93, 0, 0, -0.07, -1) );
		tmp.setTextColor( Color.LIGHT_GRAY );
		addChild( tmp );
		
		// upper right close button
		Label tmp2 = new Label("[X]") {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				close();
			}
		};
//		tmp2.setBounds(0, 0, 3, 3);
		tmp2.setLayout( new PercentMarginLayout(-0.9, -0.93, 0, 0, -0.07, -0.1) );
		tmp2.setTextColor( Color.LIGHT_GRAY );
		addChild( tmp2 );
	}
	
	protected final void close() {
		this.setVisible( false );
	}
}
