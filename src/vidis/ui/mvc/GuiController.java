package vidis.ui.mvc;

import org.apache.log4j.Logger;

import vidis.ui.events.AMouseEvent;
import vidis.ui.events.CameraEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.gui.Gui;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;
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
		
		registerEvent( IVidisEvent.MouseClickedEvent,
				   IVidisEvent.MousePressedEvent,
				   IVidisEvent.MouseReleasedEvent,
				   IVidisEvent.MouseMovedEvent );
		
		registerEvent( IVidisEvent.FPS );
		
		registerEvent( IVidisEvent.ShowGuiContainer );
		
		registerEvent( IVidisEvent.SelectASimObject );
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
			logger.error("Selected  " + ((VidisEvent<ASimObject>)event).getData() );
			gui.setSelection( ((VidisEvent<ASimObject>)event).getData() );
			break;
		
		case IVidisEvent.MouseClickedEvent:
		case IVidisEvent.MousePressedEvent:
		case IVidisEvent.MouseReleasedEvent:
		case IVidisEvent.MouseMovedEvent:
			if ( ((AMouseEvent)event).rayCalculated == false && ((AMouseEvent)event).forwardTo3D == false && ((AMouseEvent)event).guiCoords == null && guiCamera != null ) {
				guiCamera.fireEvent( event );
			}
			break;
		case IVidisEvent.FPS:
			String fps = ((VidisEvent)event).getData().toString();
			if(gui != null && gui.fps != null)
				gui.fps.setText( String.format( "%4.1ffps", Double.parseDouble(fps) ) );
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
