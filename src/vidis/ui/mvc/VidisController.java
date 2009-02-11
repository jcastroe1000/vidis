/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;

public class VidisController extends AController {

	private static Logger logger = Logger.getLogger( VidisController.class );
	
	public VidisController() {
		logger.debug( "Constructor()" );
		addChildController( new InputController() );
		addChildController( new WindowController() );
		addChildController( new SceneController() );
		addChildController( new SimulatorController() );
		addChildController( new JobController() );
		
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )" );
		switch ( event.getID() ) {
		case IVidisEvent.Init:
			logger.info( "sending InitWindow Event" );
			Dispatcher.forwardEvent( VidisEvent.InitWindow );
			logger.info( "sending InitScene Event" );
			Dispatcher.forwardEvent( VidisEvent.InitScene );
			logger.info( "sending InitSimulator Event" );
			Dispatcher.forwardEvent( VidisEvent.InitSimulator );
			break;
		}
		forwardEventToChilds( event );
	}

}
