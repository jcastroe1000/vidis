package vidis.ui.mvc;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.data.sim.AComponent;
import vidis.data.sim.SimNode;
import vidis.sim.Simulator;
import vidis.sim.classloader.modules.impl.AModuleFile;
import vidis.sim.classloader.modules.impl.dir.FileModuleFile;
import vidis.ui.config.Configuration;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.JobAppend;
import vidis.ui.events.VidisEvent;
import vidis.ui.events.jobs.ALayoutJob;
import vidis.ui.model.graph.layouts.GraphLayout;
import vidis.ui.model.graph.layouts.impl.GraphElectricSpringLayout;
import vidis.ui.model.graph.layouts.impl.GraphGridLayout;
import vidis.ui.model.graph.layouts.impl.GraphRandomLayout;
import vidis.ui.model.graph.layouts.impl.GraphSpiralLayout;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

public class SimulatorController extends AController {
	private static Logger logger = Logger.getLogger( SimulatorController.class );

	private Simulator sim = Simulator.getInstance();
	
	public SimulatorController() {
		logger.debug( "Constructor()" );
		
		registerEvent( IVidisEvent.InitSimulator );
		
		registerEvent( IVidisEvent.SimulatorPlay, 
						IVidisEvent.SimulatorLoad,
						IVidisEvent.SimulatorReload );
		
		registerEvent(
				IVidisEvent.LayoutApplyGraphElectricSpring, 
				IVidisEvent.LayoutApplyRandom,
				IVidisEvent.LayoutApplySpiral,
				IVidisEvent.LayoutApplyGrid
		);
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
				AModuleFile f = null;
				try {
					f = (AModuleFile) ((VidisEvent)event).getData();
				} catch(ClassCastException e) {
					logger.error("catched and resolved exception; older versions may use java.util.File, so let's try fallback.", e);
					try {
						f = new FileModuleFile((File) ((VidisEvent)event).getData());
					} catch(ClassCastException e2) {
						logger.error("catched BUT COULD NOT RECOVER: ",e2);
					}
				} finally {
					if(f != null) {
	//					System.err.println("Loading MSIM: " + f);
						// stop simulator
						if(!Simulator.getInstance().getPlayer().isPaused())
							Simulator.getInstance().getPlayer().pause();
						Simulator.getInstance().getPlayer().stop();
						
						// load file
						Simulator.getInstance().importSimFile(f);
						
						// reset detail level
						Configuration.DETAIL_LEVEL = 0.0;
						
						// apply a nice layout
						Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGrid );
					}
				}
				
//				if ( f != null && f.exists() && f.isFile()) {
//					if(!Simulator.getInstance().getPlayer().isPaused())
//						Simulator.getInstance().getPlayer().pause();
//					Simulator.getInstance().getPlayer().stop();
//					sim.importSimFile(f);
//					Configuration.DETAIL_LEVEL = 0.0;
////					Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGraphElectricSpring );
//					Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGrid );
//				}
			}
			break;
		case IVidisEvent.SimulatorReload:
			sim.reload();
			Dispatcher.forwardEvent( IVidisEvent.LayoutApplyGrid );
			break;
		case IVidisEvent.LayoutApplyGraphElectricSpring:
			Dispatcher.forwardEvent( new JobAppend (new ALayoutJob() {
				public GraphLayout getLayout() {
					return GraphElectricSpringLayout.getInstance();
				}
				public Collection<SimNode> getNodes() {
					return SimulatorController.this.getNodes();
				}
			}));
			break;
		case IVidisEvent.LayoutApplyRandom:
			try {
				Dispatcher.forwardEvent( new JobAppend (new ALayoutJob() {
					public GraphLayout getLayout() {
						return GraphRandomLayout.getInstance();
					}
					public Collection<SimNode> getNodes() {
						return SimulatorController.this.getNodes();
					}
				}));
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		case IVidisEvent.LayoutApplySpiral:
			try {
				Dispatcher.forwardEvent( new JobAppend (new ALayoutJob() {
					public GraphLayout getLayout() {
						return GraphSpiralLayout.getInstance();
					}
					public Collection<SimNode> getNodes() {
						return SimulatorController.this.getNodes();
					}
				}));
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		case IVidisEvent.LayoutApplyGrid:
			try {
				Dispatcher.forwardEvent( new JobAppend (new ALayoutJob() {
					public GraphLayout getLayout() {
						return GraphGridLayout.getInstance();
					}
					public Collection<SimNode> getNodes() {
						return SimulatorController.this.getNodes();
					}
				}));
				} catch (Exception e) {
					logger.error(e);
				}
			break;
		}
	}
	
	private void initialize() {
//		Simulator.createInstance();
//		sim = Simulator.getInstance();
//		sim.importSimFile( ResourceManager.getModuleFile("bullyElectionAlgorithm", "demo.msim") );
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
		Dispatcher.forwardEvent( new VidisEvent<File>( IVidisEvent.SimulatorLoad, ResourceManager.getModuleFile("vectorClockAlgorithm", "simple.msim") ) );
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
