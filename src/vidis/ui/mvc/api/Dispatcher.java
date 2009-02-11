/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc.api;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import vidis.ui.events.DummyEvent;
import vidis.ui.events.IVidisEvent;

public class Dispatcher {

	private static Logger logger = Logger.getLogger( Dispatcher.class );

	public static Set<IController> controller = new HashSet<IController>();
	
	private static Map<Integer, Set<IController>> eventHandler = new HashMap<Integer, Set<IController>>();
	
	/**
	 * forwards an event to all Controllers
	 * @param event
	 */
	public static void forwardEvent( IVidisEvent event ) {
		logger.debug( "forwardEvent( "+ event + " )" );
		if ( eventHandler.containsKey( event.getID() )) {
			for ( IController c : eventHandler.get( event.getID() ) ) {
				c.fireEvent( event );
			}
		}
		else {
			logger.warn( "forwardEvent: nobody registered for "+event+" .. forwarding to all" );
			for ( IController c : controller ) {
				c.fireEvent( event );
			}
		}
	}
	
	/**
	 * wraps an eventid in a <code>DummyEvent</code> and calls <code>forwardEvent( IVidisEvent event )</code>
	 * @param eventid
	 */
	public static void forwardEvent( int eventid ) {
		IVidisEvent dummyEvent = new DummyEvent( eventid );
		forwardEvent( dummyEvent );
	}
	
	public static void registerController( IController c ) {
		controller.add( c );
	}
	
	public static void registerEvent( IController c, int eventId ) {
		if ( eventHandler.containsKey( eventId ) ){
			eventHandler.get( eventId ).add( c );
		}
		else {
			Set<IController> s = new HashSet<IController>();
			s.add( c );
			eventHandler.put( eventId, s );
		}
	}
	
}
