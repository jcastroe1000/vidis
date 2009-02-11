/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import org.apache.log4j.Logger;

import vidis.ui.events.KeyPressedEvent;
import vidis.ui.events.KeyReleasedEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MouseMovedEvent;
import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;
import vidis.ui.mvc.api.Dispatcher;

public class InputListener implements KeyListener, MouseWheelListener, MouseListener, MouseMotionListener {
	
	/**
	 * listens to all sorts of AWT input events and forwards them into Vidis own Event System
	 * @author Christoph
	 */
	private static Logger logger = Logger.getLogger( InputListener.class );	
	
	public InputListener(){
	}
	
	public void keyPressed(KeyEvent e) {
		logger.debug( "keyPressed("+e+")");
		Dispatcher.forwardEvent( new KeyPressedEvent( e ) );
	}

	public void keyReleased(KeyEvent e) {
		logger.debug( "keyReleased("+e+")");
		Dispatcher.forwardEvent( new KeyReleasedEvent( e ) );
	}

	public void keyTyped(KeyEvent e) {
//		logger.debug( "keyTyped("+e+")");
//		Dispatcher.forwardEvent( new KeyTypedEvent( e ) );
	}

	public void mouseWheelMoved(MouseWheelEvent e) {
	}

	public void mouseClicked(MouseEvent e) {
		logger.warn( "mouseClicked("+e+")");
		Dispatcher.forwardEvent( new MouseClickedEvent( e ) );
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mousePressed(MouseEvent e) {
		logger.debug( "mousePressed("+e+")");
		Dispatcher.forwardEvent( new MousePressedEvent( e ) );
	}

	public void mouseReleased(MouseEvent e) {
		logger.debug( "mouseReleased("+e+")");
		Dispatcher.forwardEvent( new MouseReleasedEvent( e ) );
	}
	
	public void mouseDragged(MouseEvent e) {
		Dispatcher.forwardEvent( new MouseMovedEvent( e ) );
	}

	public void mouseMoved(MouseEvent e) {
		Dispatcher.forwardEvent( new MouseMovedEvent( e ) ); 
	}

	
}
