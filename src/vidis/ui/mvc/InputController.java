/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc;

import java.awt.event.KeyEvent;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import vidis.ui.events.DummyEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.KeyPressedEvent;
import vidis.ui.events.KeyReleasedEvent;
import vidis.ui.events.KeyTypedEvent;
import vidis.ui.events.StartEvent;
import vidis.ui.events.StopEvent;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;

public class InputController extends AController {

	private static Logger logger = Logger.getLogger( InputController.class );
	
	/**
	 * Map von KeyEvent.TASTE nach IVidisEvents event
	 */
	private Map<Integer, Integer> keyMap = new HashMap<Integer, Integer>();
	
	public InputController() {
		logger.debug( "Constructor()" );
		// load hardcoded key binding - later try to load config file and fallback to hardcoded keybinding
		loadHardcodedKeyBinding();
		
		registerEvent( IVidisEvent.KeyPressedEvent,
					   IVidisEvent.KeyReleasedEvent,
					   IVidisEvent.KeyTypedEvent );
		
//		registerEvent( IVidisEvent.MouseClickedEvent,
//					   IVidisEvent.MousePressedEvent,
//					   IVidisEvent.MouseReleasedEvent );
	}
	
	private void loadHardcodedKeyBinding() {
		logger.debug( "loadHardcodedKeyBinding()" );
		keyMap.put( KeyEvent.VK_W, IVidisEvent.ScrollUp );
		keyMap.put( KeyEvent.VK_A, IVidisEvent.ScrollLeft );
		keyMap.put( KeyEvent.VK_S, IVidisEvent.ScrollDown );
		keyMap.put( KeyEvent.VK_D, IVidisEvent.ScrollRight );
		keyMap.put( KeyEvent.VK_PAGE_UP, IVidisEvent.SkewUp);
		keyMap.put( KeyEvent.VK_PAGE_DOWN, IVidisEvent.SkewDown);
		keyMap.put( KeyEvent.VK_END, IVidisEvent.RotateRight);
		keyMap.put( KeyEvent.VK_DELETE, IVidisEvent.RotateLeft);
		keyMap.put(KeyEvent.VK_Q, IVidisEvent.ZoomIn);
		keyMap.put(KeyEvent.VK_E, IVidisEvent.ZoomOut);
		
		keyMap.put(KeyEvent.VK_0, IVidisEvent.ExportSimFile);
		
	}
	
	
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )");
		switch (event.getID()) {
		case IVidisEvent.KeyPressedEvent:
			handleKeyPressedEvent( (KeyPressedEvent)event );
			break;
		case IVidisEvent.KeyReleasedEvent:
			handleKeyReleasedEvent( (KeyReleasedEvent)event );
			break;
		case IVidisEvent.KeyTypedEvent:
			handleKeyTypedEvent( (KeyTypedEvent)event );
			break;
		}
	}
	
	private void handleKeyPressedEvent( KeyPressedEvent event ) {
		logger.debug( "handleKeyPressedEvent( "+event+" )");
		if (keyMap.containsKey( event.key ) ) {
			Dispatcher.forwardEvent( new StartEvent( keyMap.get( event.key ) ) );
		}
	}
	
	private void handleKeyReleasedEvent( KeyReleasedEvent event ) {
		logger.debug( "handleKeyReleasedEvent( "+event+" )");
		if (keyMap.containsKey( event.key ) ) {
			Dispatcher.forwardEvent( new StopEvent( keyMap.get( event.key ) ) );
		}		
	}
	
	private void handleKeyTypedEvent( KeyTypedEvent event ) {
		logger.debug( "handleKeyTypedEvent( "+event+" )");
		if (keyMap.containsKey( event.key ) ) {
			Dispatcher.forwardEvent( new DummyEvent( keyMap.get( event.key ) ) );
		}
	}

}
