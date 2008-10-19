package vidis.ui.mvc;


import org.apache.log4j.Logger;

import vidis.ui.events.CameraEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.ui.vis.camera.DefaultCamera;


public class CameraController extends AController{

	private static Logger logger = Logger.getLogger( CameraController.class );
	
	private DefaultCamera defaultCamera;
	
	public CameraController() {
		logger.debug( "Constructor()" );
		
		registerEvent( IVidisEvent.InitCamera );
		
		registerEvent(	IVidisEvent.ScrollDown,
						IVidisEvent.ScrollUp,
						IVidisEvent.ScrollLeft,
						IVidisEvent.ScrollRight,
						IVidisEvent.SkewDown,
						IVidisEvent.SkewUp,
						IVidisEvent.RotateLeft,
						IVidisEvent.RotateRight,
						IVidisEvent.ZoomIn,
						IVidisEvent.ZoomOut);
		
		registerEvent(	IVidisEvent.MouseClickedEvent,
						IVidisEvent.MouseMovedEvent );
	}
	
	@Override
	public void handleEvent( IVidisEvent event ) {
		logger.debug( "handleEvent("+event+")" );
		switch ( event.getID() ) {
		case IVidisEvent.InitCamera:
			initialize();
			break;
		case IVidisEvent.ZoomIn:
		case IVidisEvent.ZoomOut:
		case IVidisEvent.RotateLeft:
		case IVidisEvent.RotateRight:
		case IVidisEvent.SkewDown:
		case IVidisEvent.SkewUp:
		case IVidisEvent.ScrollDown:
		case IVidisEvent.ScrollLeft:
		case IVidisEvent.ScrollUp:
		case IVidisEvent.ScrollRight:
			forwardEventToOtherHandler( defaultCamera, event );
			break;
		case IVidisEvent.MouseClickedEvent:
		case IVidisEvent.MouseMovedEvent:
			defaultCamera.fireEvent( event );
			break;
		}
	}
	
	private void initialize() {
		defaultCamera = new DefaultCamera();
		Dispatcher.forwardEvent( new CameraEvent( IVidisEvent.CameraRegister, defaultCamera ) );
	}
	
}
