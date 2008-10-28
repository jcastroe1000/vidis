package vidis.ui.mvc;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.mvc.api.AController;
import vidis.ui.vis.FrameContainer;

public class WindowController extends AController {
	
	private static Logger logger = Logger.getLogger(WindowController.class);

	private FrameContainer frame;
	
	public WindowController() {
		logger.debug( "Constructor()" );
		registerEvent( IVidisEvent.InitWindow );
	}
	
	@SuppressWarnings( "unchecked" )
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )" );
		switch (event.getID()) {
		case IVidisEvent.InitWindow:
			frame = new FrameContainer( "VIDIS" );
			frame.setVisible( true );
			break;
		case IVidisEvent.RegisterCanvas:
			frame.addGLCanvas( ((VidisEvent<GLCanvas>) event).getData() );
			frame.doLayout();
		}

	}
	

}
