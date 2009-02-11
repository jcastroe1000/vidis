/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui;


import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.VidisController;
import vidis.ui.mvc.api.Dispatcher;



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
