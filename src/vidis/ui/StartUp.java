package vidis.ui;


import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.VidisController;
import vidis.ui.mvc.api.Dispatcher;



public class StartUp {
	private static Logger logger = Logger.getLogger( StartUp.class );
	
	public static void main(String[] args){
//		BasicConfigurator.configure();
		
		logger.info( "starting up" );
		logger.info( "java.library.path:" );
		String[] l = System.getProperty( "java.library.path" ).split(";");
		for (String s : l) logger.info( " * " + s );
		
		Dispatcher.registerController( new VidisController() );	
		Dispatcher.forwardEvent( IVidisEvent.Init );
		
	}
}
