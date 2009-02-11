/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc.api;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.IEventHandler;
import vidis.ui.events.IVidisEvent;

public abstract class AController extends AEventHandler implements IController {

	private static Logger logger = Logger.getLogger( AController.class );
	
	private Set<IController> childs = new HashSet<IController>();
	
	protected void registerEvent( int eventId ) {
		logger.debug( "registerEvent( "+eventId+" )");
		Dispatcher.registerEvent( this, eventId );
	}
	
	protected void registerEvent ( int... eventIds ) {
		logger.debug( "registerEvent( "+eventIds+" )");
		for ( int eventId : eventIds ) {
			registerEvent( eventId );
		}
	}
	
	protected void addChildController( IController c ) {
		logger.debug( "addChildController()" );
		childs.add(c);
	}
	
	protected void forwardEventToChilds( IVidisEvent event) {
		logger.debug( "forwardEventToChilds()" );
		for ( IController c : childs ) {
			c.fireEvent(event);
		}
	}
	
	protected void forwardEventToOtherHandler( IEventHandler h, IVidisEvent e ) {
		logger.debug( "forwardEventToOtherHandler()" );
		h.fireEvent( e );
	}
	
	public abstract void handleEvent( IVidisEvent event );

}
