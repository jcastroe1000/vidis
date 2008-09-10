package vidis.ui.mvc;

import java.io.File;

import org.apache.log4j.Logger;

import vidis.sim.Simulator;
import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.api.AController;

public class SimulatorController extends AController {
	private static Logger logger = Logger.getLogger( SimulatorController.class );

	Simulator sim;
	
	public SimulatorController() {
		logger.debug( "Constructor()" );
		
		registerEvent( IVidisEvent.InitSimulator );
		
		registerEvent( IVidisEvent.SimulatorPlay );
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		logger.debug( "handleEvent( "+event+" )" );
		switch ( event.getID() ) {
		case IVidisEvent.InitSimulator:
			initialize();
			break;
		case IVidisEvent.SimulatorPlay:
			sim.getPlayer().play();
			break;
		}
	}
	
	private void initialize() {
		Simulator.createInstance();
		sim = Simulator.getInstance();
		sim.importSimFile( new File( "./data/modules/demo/demo.msim" ) );
	}

}
