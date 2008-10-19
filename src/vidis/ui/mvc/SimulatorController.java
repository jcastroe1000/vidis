package vidis.ui.mvc;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.data.sim.AComponent;
import vidis.data.sim.SimNode;
import vidis.sim.Simulator;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.graph.layouts.impl.GraphElectricSpringLayout;
import vidis.ui.mvc.api.AController;
import vidis.util.ResourceManager;

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
			sim.getPlayer().playPause();
			break;
		}
	}
	
	private void initialize() {
		Simulator.createInstance();
		sim = Simulator.getInstance();
//		sim.importSimFile( ResourceManager.getModuleFile("bullyElectionAlgorithm", "demo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("demo", "demo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("demo", "simpledemo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("flooding", "flood1.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("flooding", "v1.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vartest", "onenode.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "simple.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "complex.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "veryComplex.msim") );
		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "veryComplexLoose.msim") );
		// apply default layout
//		layout();
		// start playing
		//sim.getPlayer().play();
		
	}
	
	private void layout() {
		try {
			List<AComponent> components = sim.getSimulatorComponents();
			List<SimNode> nodes = new ArrayList<SimNode>();
			for(AComponent component : components) {
				if(component instanceof SimNode) {
					nodes.add((SimNode) component);
				}
			}
			GraphElectricSpringLayout.getInstance().apply(nodes);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
