package vidis.sim;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.sim.AComponent;
import vidis.data.sim.SimLink;
import vidis.data.sim.SimNode;
import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.sim.classloader.VidisClassLoader;
import vidis.sim.classloader.modules.impl.dir.FileModuleFile;
import vidis.sim.classloader.modules.interfaces.IModuleFileComponent;
import vidis.sim.exceptions.SimulatorConfigRuntimeException;
import vidis.sim.simulatorInternals.SimulatorData;
import vidis.sim.xml.modules.XMLModuleReader;
import vidis.sim.xml.modules.dataStructure.DocumentData;
import vidis.sim.xml.modules.dataStructure.DocumentDataConnection;
import vidis.sim.xml.modules.dataStructure.DocumentDataLink;
import vidis.ui.model.graph.layouts.AGraphLayout;

public class Simulator {
	/* -- time management for interpolation -- */
	/**
	 * lastTime stores the time of the last simulatorStep
	 */
	private int medianSize = 3;
	private long lastTime;
	private List<Long> lastTimes = new LinkedList<Long>();
	/* --------------------------------------- */
	
	/* -- logger -- */
	private static final Logger logger = Logger.getLogger(Simulator.class);

	/* -- simulator stuff -- */
	private static Simulator instance;

	private SimulatorData data;
	
	private List<IModuleFileComponent> simFileHistory = new LinkedList<IModuleFileComponent>();
	
	private static boolean RUN_WITH_3D = true;

	private Simulator() {
		player = new Player();
		player.startWorker();
		data = new SimulatorData();
	}

	public static final boolean configIsEnable3D() {
		return RUN_WITH_3D;
	}

	/**
	 * one of the configuration functions enable 3d; must be called BEFORE
	 * Simulator.createInstance() is called!
	 */
	public static final void configEnable3D() {
		if (!isInstantiated()) {
			RUN_WITH_3D = true;
		} else {
			throw new SimulatorConfigRuntimeException();
		}
	}

	/**
	 * one of the configuration functions; disable 3d; must be called BEFORE
	 * Simulator.createInstance() is called!
	 */
	public static final void configDisable3D() {
		if (!isInstantiated()) {
			RUN_WITH_3D = false;
		} else {
			throw new SimulatorConfigRuntimeException();
		}
	}

	/**
	 * create the simulator instance;
	 * 
	 * initializes the simulator
	 * 
	 * NOTE: call configuration functions BEFORE this one!
	 */
	public static final void createInstance() {
		if (!isInstantiated()) {
			instance = new Simulator();
		}
	}

	/**
	 * retrieves the simulator instance;
	 * 
	 * NOTE: calls Simulator.createInstance() to create the simulator instance
	 * 
	 * @return the simulator instance
	 */
	public static final Simulator getInstance() {
		if (!isInstantiated()) {
			createInstance();
		}
		return instance;
	}

	public static final boolean isInstantiated() {
		return instance != null;
	}

	private Player player;

	public void simulateOneStep() {
		if (lastTimes.size() > medianSize)
			lastTimes.remove(0);
		long now = System.currentTimeMillis();
		lastTimes.add(now - lastTime);
		lastTime = now;
		data.executeComponents();
	}

	public long getLastStepDuration() {
		long median = 0;
		if (lastTimes.size() > 0) {
			for (int i = 0; i < lastTimes.size(); i++)
				median += lastTimes.get(i);
			median /= lastTimes.size();
		}
		return median;
	}
	
	public void importSimFile(IModuleFileComponent f) {
		simFileHistory.add(f);
		
		XMLModuleReader reader = XMLModuleReader.parse(f);
		
		init(reader);
	}

	public void importSimFile(File file) {
		FileModuleFile f = new FileModuleFile(file);
		
		importSimFile(f);
	}

	private final void init(XMLModuleReader reader) {
		if (reader == null) {
			throw new RuntimeException("Cannot initialize Module: reader == null; please check your config file!");
		}

		player.stop();

//		if (data.components.size() > 0) {
			data.killComponents();
			reset();
			data = new SimulatorData();
//		}

		if (reader.getDocument().getNodeDensity() != null) {
			double density = reader.getDocument().getNodeDensity();
			AGraphLayout.setNodeDensityToAll(density);
		}

		// get nodes
		Map<String, SimNode> nodes = new TreeMap<String, SimNode>();
		generateSimNodes(nodes, reader.getDocument());
		
		// get links
		Map<String, SimLink> links = new HashMap<String, SimLink>();
		generateSimLinks(links, reader.getDocument());

		// connect nodes via links
		generateSimNode_SimLink_connections(nodes, links, reader.getDocument());
	}

	private void generateSimNode_SimLink_connections(Map<String, SimNode> nodes, Map<String, SimLink> links, DocumentData document) {
		for (DocumentDataConnection documentConnection : document.getConnections()) {
			// may should be done using a node connect function, but for now it's fine
			// if we connect manually
			SimLink link = links.get(documentConnection.getLink().getId());
			if (link != null) {
				SimNode nodeA = nodes.get(documentConnection.getNodeA().getId());
				SimNode nodeB = nodes.get(documentConnection.getNodeB().getId());
				if(link.isConnected()) {
//					System.err.println("TRYING TO MULTIPLE CONNECT THIS LINK: " + link + " TO ("+nodeA+","+nodeB+") IS ALREADY CONNECTED! PLEASE WATCH YOUR CONFIGURATION!");
					logger.error("TRYING TO MULTIPLE CONNECT THIS LINK: " + link + " TO ("+nodeA+","+nodeB+") IS ALREADY CONNECTED! PLEASE WATCH YOUR CONFIGURATION!");
				} else {
					link.connect(nodeA, nodeB);
					// register a connected link only
					registerComponent(link);
				}
			}
		}
	}

	private Constructor<?> getEmptyConstructor(Class<?> c) {
		Constructor<?>[] constructors = c.getConstructors();
		// fetch correct constructor
		Constructor<?> constructor = null;
		for (int i = 0; i < constructors.length; i++) {
			// match empty constructor
			if (constructors[i].getParameterTypes().length == 0) {
				constructor = constructors[i];
				break;
			}
		}
		return constructor;
	}

	private void generateSimLinks(Map<String, SimLink> links, DocumentData document) {
		List<String> linkIds = new LinkedList<String>();
		linkIds.addAll(document.getLinks().keySet());
		Collections.sort(linkIds);
		for (String id : linkIds) {
			DocumentDataLink documentLink = document.getLinkById(id);
			String classpath = document.getPackageName() + "." + documentLink.getClasspath();
			try {
//				Class<?> clazz = Class.forName(classpath);
				Class<?> clazz = VidisClassLoader.getInstance().loadClass(classpath);
				Constructor<?> constructor = getEmptyConstructor(clazz);
				if (constructor != null) {
					// instance IUserLink
					Object obj = constructor.newInstance();
					// check if really IUserLink
					if (obj instanceof IUserLink) {
						IUserLink link = (IUserLink) obj;
						SimLink sim = new SimLink(link, documentLink.getDelay());
						sim.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.ID, id));
						// set variables
						for (String identifier : document.getLinkById(id).getVariables().keySet()) {
							sim.registerVariable(new DefaultVariable(AVariable.COMMON_SCOPES.USER + "." + identifier, document.getLinkById(id).getVariables().get(identifier)));
						}
						// register instance to simulator
						links.put(documentLink.getId(), sim);
					} else {
						logger.error("CANNOT INSTANTIATE A LINK THAT DOES NOT IMPLEMENT " + IUserLink.class);
					}
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}

	private void generateSimNodes(Map<String, SimNode> nodes, DocumentData document) {
		List<String> nodeIds = new LinkedList<String>();
		nodeIds.addAll(document.getNodes().keySet());
		Collections.sort(nodeIds);
		for (String nodeId : nodeIds) {
			String classpath = document.getPackageName() + "." + document.getNodeById(nodeId).getClasspath();
			try {
				// Class<?> clasS =
				// ClassLoader.getSystemClassLoader().loadClass(classpath);
//				Class<?> c = Class.forName(classpath);
				Class<?> c = VidisClassLoader.getInstance().loadClass(classpath);
				Constructor<?> k = getEmptyConstructor(c);
				Object o = k.newInstance();
				if (o instanceof IUserNode) {
					SimNode node = new SimNode((IUserNode) o);
					node.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.ID, nodeId));
					for (String identifier : document.getNodeById(nodeId).getVariables().keySet()) {
						node.registerVariable(new DefaultVariable(AVariable.COMMON_SCOPES.USER + "." + identifier, document.getNodeById(nodeId).getVariables().get(identifier)));
					}
//					if (!node.hasVariable(AVariable.COMMON_IDENTIFIERS.POSITION)) {
//						Point3d point = ((GraphSpiralLayout)GraphSpiralLayout.getInstance()).nextNodePoint3d();
//						// Logger.output(this, nodeId + " => " + point);
//						// Logger.output(LogLevel.WARN, this, nodeId + " => " + point);
//						node.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.POSITION, point));
//					}
					nodes.put(nodeId, node);
					registerComponent(node);
					// this.informAll(new NodeJoinEvent(node.getInstance()));
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void reload() {
		if( simFileHistory.size() > 0) {
			importSimFile( simFileHistory.get(simFileHistory.size()-1) );
		} else {
			// cannot reset
			
		}
	}

	public void reset() {
		data.reset();
	}

	public List<AComponent> getSimulatorComponents() {
		return data.getComponents();
	}

	public void registerComponent(AComponent component) {
		data.registerComponent(component);
	}
	public void unregisterObject(AComponent component) {
		data.unregisterComponent(component);
	}

	public long getNow() {
		return data.getTime();
	}

	public Player getPlayer() {
		return player;
	}
}
