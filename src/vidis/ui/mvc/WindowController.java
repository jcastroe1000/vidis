/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.mvc.api.AController;
import vidis.ui.vis.FrameContainer;

public class WindowController extends AController {
	
	private static Logger logger = Logger.getLogger(WindowController.class);

	private FrameContainer frame;
	
	public WindowController() {
		logger.debug( "Constructor()" );
		registerEvent( IVidisEvent.InitWindow );
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )" );
		switch (event.getID()) {
		case IVidisEvent.InitWindow:
			frame = new FrameContainer( "VIDIS" );
			frame.setVisible( true );
			break;
		case IVidisEvent.RegisterCanvas:
			frame.addGLCanvas( ((VidisEvent<GLCanvas>) event).getData() );
			frame.doLayout();
		}

	}
	

}
