package vidis.ui.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.data.sim.AComponent;
import vidis.data.sim.SimNode;
import vidis.sim.Simulator;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.model.graph.layouts.impl.GraphElectricSpringLayout;
import vidis.ui.model.graph.layouts.impl.GraphRandomLayout;
import vidis.ui.model.graph.layouts.impl.GraphSpiralLayout;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

public class SimulatorController extends AController {
	private static Logger logger = Logger.getLogger( SimulatorController.class );

	Simulator sim;
	
	public SimulatorController() {
		logger.debug( "Constructor()" );
		
		registerEvent( IVidisEvent.InitSimulator );
		
		registerEvent( IVidisEvent.SimulatorPlay, 
						IVidisEvent.SimulatorLoad );
		
		registerEvent( IVidisEvent.LayoutApplyGraphElectricSpring, 
				IVidisEvent.LayoutApplyRandom,
				IVidisEvent.LayoutApplySpiral);
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
		case IVidisEvent.SimulatorLoad:
			if(event instanceof VidisEvent) {
				// now pick a file
				File f = (File) ((VidisEvent)event).getData();
				if ( f != null && f.exists() && f.isFile()) {
					if(!Simulator.getInstance().getPlayer().isPaused())
						Simulator.getInstance().getPlayer().pause();
					Simulator.getInstance().getPlayer().stop();
					sim.importSimFile(f);
					Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGraphElectricSpring );
				}
			}
			break;
		case IVidisEvent.LayoutApplyGraphElectricSpring:
			try {
					GraphElectricSpringLayout.getInstance().apply(getNodes());
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		case IVidisEvent.LayoutApplyRandom:
			try {
					GraphRandomLayout.getInstance().apply(getNodes());
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		case IVidisEvent.LayoutApplySpiral:
			try {
					GraphSpiralLayout.getInstance().apply(getNodes());
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		}
	}
	
	private void initialize() {
		Simulator.createInstance();
		sim = Simulator.getInstance();
		sim.importSimFile( ResourceManager.getModuleFile("bullyElectionAlgorithm", "demo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("demo", "demo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("demo", "simpledemo.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("flooding", "flood1.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("flooding", "v1.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vartest", "onenode.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "simple.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "complex.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "veryComplex.msim") );
//		sim.importSimFile( ResourceManager.getModuleFile("vectorClockAlgorithm", "veryComplexLoose.msim") );
		// apply default layout
//		layout();
		// start playing
		//sim.getPlayer().play();
//		Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGraphElectricSpring );
	}
	
	private List<SimNode> getNodes() {
		List<AComponent> components = sim.getSimulatorComponents();
		List<SimNode> nodes = new ArrayList<SimNode>();
		for(AComponent component : components) {
			if(component instanceof SimNode) {
				nodes.add((SimNode) component);
			}
		}
		return nodes;
	}
}
