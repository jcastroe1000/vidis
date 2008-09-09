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
