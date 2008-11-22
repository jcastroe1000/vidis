package vidis.ui;


import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.VidisController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;



public class StartUp {
	private static Logger logger = Logger.getLogger( StartUp.class );
	
	public static void main(String[] args){
		// try jar loading
//		System.err.println(ResourceManager.getModules());
//		
//		System.exit(0);
		
//		BasicConfigurator.configure();
		
		logger.info( "starting up" );
		logger.info( "java.library.path:" );
		String[] l = System.getProperty( "java.library.path" ).split(";");
		for (String s : l) logger.info( " * " + s );
		
		Dispatcher.registerController( new VidisController() );	
		Dispatcher.forwardEvent( IVidisEvent.Init );
		
	}
}
