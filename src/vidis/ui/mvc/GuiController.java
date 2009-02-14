/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.mvc;

import org.apache.log4j.Logger;

import vidis.ui.events.CameraEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.gui.Gui;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.ui.vis.camera.GuiCamera;

public class GuiController extends AController {

	private static Logger logger = Logger.getLogger( GuiController.class );
	
	private GuiCamera guiCamera;
	private Gui gui;
	
	public GuiController() {
		logger.debug("Constructor()");
		registerEvent( IVidisEvent.InitGui );
		
		registerEvent( 	IVidisEvent.ObjectRegister, 
				   		IVidisEvent.ObjectUnregister );
		
		registerEvent( IVidisEvent.FPS );
		
		registerEvent( IVidisEvent.ShowGuiContainer );
		
		registerEvent( IVidisEvent.SelectASimObject );
		
		registerEvent( IVidisEvent.MouseMovedEvent_AWT,
					   IVidisEvent.MousePressedEvent_AWT,
					   IVidisEvent.MouseReleasedEvent_AWT );
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )");
		switch ( event.getID() ) {
		case IVidisEvent.InitGui:
			initialize();
			break;
		case IVidisEvent.ShowGuiContainer:
			gui.addContainer( (IGuiContainer) ((VidisEvent)event).getData() );
			break;
		case IVidisEvent.SelectASimObject:
			logger.error("Selected  " + ((VidisEvent<ASimObject>)event).getData() + " hash=" + event.hashCode() );
			gui.setSelection( ((VidisEvent<ASimObject>)event).getData() );
			break;
		case IVidisEvent.MouseMovedEvent_AWT:
		case IVidisEvent.MousePressedEvent_AWT:
		case IVidisEvent.MouseReleasedEvent_AWT:
			guiCamera.fireEvent( event );
			break;
		case IVidisEvent.FPS:
			String fps = ((VidisEvent)event).getData().toString();
			if(gui != null && gui.fps != null)
				gui.fps.setText( String.format( "%4.1ffps", Double.parseDouble(fps) ) );
			break;
		case IVidisEvent.ObjectRegister:
			logger.fatal( "OBJECT REGISTER EVENT IN GuiController" );
//			gui.registerObject( ((ObjectEvent)event).getObject() );
			IVisObject o = ((ObjectEvent)event).getObject();
			if ( o instanceof ASimObject ) {
				ASimObject o1 = (ASimObject) o;
				if ( o1.getOnScreenLabel() != null ) {
					ObjectEvent nextEvent = new ObjectEvent( IVidisEvent.ObjectRegister, o1.getOnScreenLabel() );
					Dispatcher.forwardEvent( nextEvent );
				}
			}
			break;
		case IVidisEvent.ObjectUnregister:
//			gui.unregisterObject( ((ObjectEvent)event).getObject() );
			IVisObject o2 = ((ObjectEvent)event).getObject();
			if ( o2 instanceof ASimObject ) {
				ASimObject o3 = (ASimObject) o2;
				if ( o3.getOnScreenLabel() != null ) {
					ObjectEvent nextEvent = new ObjectEvent( IVidisEvent.ObjectUnregister, o3.getOnScreenLabel() );
					Dispatcher.forwardEvent( nextEvent );
				}
			}
			break;
			
		}
	}
	
	private void initialize() {
		logger.debug( "initialize()" );
		gui = new Gui();
		guiCamera = new GuiCamera(gui);
		Dispatcher.forwardEvent( new CameraEvent( IVidisEvent.CameraRegister, guiCamera ));
		Dispatcher.forwardEvent( new ObjectEvent( IVidisEvent.ObjectRegister, gui.getMainContainer() ));
	}

}
