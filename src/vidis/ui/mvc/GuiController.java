package vidis.ui.mvc;

import org.apache.log4j.Logger;

import vidis.ui.events.CameraEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.gui.Gui;
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
				   IVidisEvent.MouseReleasedEvent );
		
		registerEvent( IVidisEvent.FPS );
		
		
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )");
		switch ( event.getID() ) {
		case IVidisEvent.InitGui:
			initialize();
			break;
		case IVidisEvent.MouseClickedEvent:
			if ( ((MouseClickedEvent)event).ray == null ) {
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
