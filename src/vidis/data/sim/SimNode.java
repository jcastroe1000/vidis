package vidis.data.sim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.vecmath.Point3d;

import org.apache.log4j.Logger;

import vidis.data.exceptions.ObstructInitCallException;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;
import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.sim.Simulator;
import vidis.sim.exceptions.ObstructInitRuntimeCallException;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.model.impl.Node;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.mvc.api.Dispatcher;

/**
 * simulator node
 * 
 * @author dominik
 * 
 */
public class SimNode extends AComponent implements ISimNodeCon, Comparable<SimNode> {
	private static Logger logger = Logger.getLogger( SimNode.class );
	
    protected IUserNode logic;

    // -------- sim fields ------- //
    /**
     * contains all connected links in this node
     */
    private Map<IUserLink, SimLink> links;

    /**
     * internal packet queue
     */
    private List<PacketQueueHolder> packetQueue;

    private IVisObject visObject;
    
    private int step = 0;
    
    /**
     * public constructor
     * 
     * @param node
     *          IUserNode this simulator node stands for
     */
    public SimNode(IUserNode node) {
		super();
		init();
		init(node);
		
		visObject = new Node( this );
		ObjectEvent nextEvent = new ObjectEvent( IVidisEvent.ObjectRegister, visObject );
		Dispatcher.forwardEvent( nextEvent );
    }

    /**
     * initialize this class
     */
    private void init() {
		this.links = new ConcurrentHashMap<IUserLink, SimLink>();
		this.packetQueue = new LinkedList<PacketQueueHolder>();
		// setVariable(new Variable<Long>("packetsReceived", 0l));
    }

    @Override
    public void kill() {
		links.clear();
		packetQueue.clear();
		super.kill();
    }
    
    @Override
    protected void killVisObject() {
    	ObjectEvent nextEvent = new ObjectEvent( IVidisEvent.ObjectUnregister, visObject );
		Dispatcher.forwardEvent( nextEvent );
    }

    /**
     * initialize this class with provided user node
     * 
     * @param node
     *          the user node
     */
    private void init(IUserNode node) {
		logic = node;
		try {
			initVars();
		    node.init(this);
		} catch (ObstructInitCallException e) {
		    // never happens, if anyway throw a real severe exception
		    throw new ObstructInitRuntimeCallException(e.getCause());
		}
    }

    public void execute() {
    	if ( step == 0) logic.init();
		processPacketQueue();
		super.execute();
		if (!isSleeping()) {
		    this.logic.execute();
		} else {
			logger.debug("skip logic.execute()");
		}
		step++;
    }

    public List<IUserLink> getConnectedLinks() {
    	return new LinkedList<IUserLink>(this.links.keySet());
    }

    /**
     * retrieve the user node associated with this object
     * 
     * @return the IUserNode
     */
    public IUserNode getUserLogic() {
    	return logic;
    }

    /**
     * retrieve if this node is connected through a link
     * 
     * @param link
     *          the link to check
     * @return true or false
     */
    private boolean isConnectedThrough(IUserLink link) {
    	return this.links.containsKey(link);
    }

    private SimLink getConnectedLink(IUserLink link) {
    	return this.links.get(link);
    }

    public void send(IUserPacket p, IUserLink link) {
    	send(p, link, 0);
    }

    public void send(IUserPacket packet, IUserLink link, long wait) {
		if (isConnectedThrough(link)) {
		    SimPacket simPacket = new SimPacket(packet, getConnectedLink(link), this, getConnectedLink(link).getOtherNode(this));
		    logger.debug(this + ".send("+packet+", "+link+", "+wait+");");
		    if (wait <= 0) {
		    	doSendOperation(simPacket, getConnectedLink(link));
		    } else {
		    	addToPacketQueue(simPacket, getConnectedLink(link), wait);
		    }
		} else {
		    // cannot send
		}
    }

    private void doSendOperation(SimPacket simPacket, SimLink link) {
		if (hasVariable(AVariable.COMMON_IDENTIFIERS.PACKETSSENT)) {
		    Long oldValue = (Long) ((DefaultVariable)getVariableById(AVariable.COMMON_IDENTIFIERS.PACKETSSENT)
			    ).getData();
		    ((DefaultVariable)getVariableById(AVariable.COMMON_IDENTIFIERS.PACKETSSENT)).update(Long.valueOf(oldValue + 1));
		} else {
		    registerVariable(new DefaultVariable(
			    AVariable.COMMON_IDENTIFIERS.PACKETSSENT, 1l));
		}
		link.send(simPacket, link.getOtherNode(this));
    }

    /**
     * cause this node to receive a packet
     * 
     * @param packet
     *          the packet to receive
     */
    public final void receive(SimPacket packet) {
		if (hasVariable(AVariable.COMMON_IDENTIFIERS.PACKETSRECEIVED)) {
			Long oldValue = (Long) ((DefaultVariable)getVariableById(AVariable.COMMON_IDENTIFIERS.PACKETSRECEIVED)
		    ).getData();
	    ((DefaultVariable)getVariableById(AVariable.COMMON_IDENTIFIERS.PACKETSRECEIVED)).update(Long.valueOf(oldValue + 1));
		} else
		    registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.PACKETSRECEIVED, 1l));
		logger.debug(this + ".receive("+packet.getUserLogic()+");");
		logic.receive(packet.getUserLogic());
		// kill 3d instance
		packet.kill();
    }

    /**
     * internal packet queue holder
     * 
     * @author dpsenner
     * 
     */
    private static class PacketQueueHolder {
		SimPacket packet;
		SimLink link;
		long timeout;
	
		public PacketQueueHolder(SimPacket packet, SimLink link, long timeout) {
		    this.packet = packet;
		    this.link = link;
		    this.timeout = timeout;
		}
    }

    private void addToPacketQueue(SimPacket simPacket, SimLink link, long wait) {
    	packetQueue.add(new PacketQueueHolder(simPacket, link, wait));
    }

    private void processPacketQueue() {
		for (int i = 0; i < packetQueue.size(); i++) {
		    PacketQueueHolder tmp = packetQueue.get(i);
		    if (tmp.timeout < 0) {
			// send
			doSendOperation(tmp.packet, tmp.link);
			// remove from queue
			packetQueue.remove(tmp);
			i--;
		    } else {
			// queue
			tmp.timeout--;
		    }
		}
    }

    public void addConnection(SimLink link) {
    	links.put(link.getUserLogic(), link);
    }
    
    public void removeConnection(SimLink simLink) {
    	links.remove(simLink.getUserLogic());
	}
    
    public void connect(IUserNode n, Class<? extends IUserLink> lclazz, long delay) {
    	// TODO connect this one
			for(int i=0; i<Simulator.getInstance().getSimulatorComponents().size(); i++) {
				AComponent c = Simulator.getInstance().getSimulatorComponents().get(i);
				if(c.getUserLogic().equals(n)) {
					try {
						SimLink s = new SimLink(lclazz.newInstance(), delay);
						s.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.ID, "spawn_link_"+(Math.random()*Double.MAX_VALUE)));
						s.connect(this, (SimNode)c);
						Simulator.getInstance().registerComponent(s);
						System.err.println("connected " + this + " with " + c + " through " + s);
						break;
					} catch (InstantiationException e) {
						logger.fatal(e);
					} catch (IllegalAccessException e) {
						logger.fatal(e);
					} catch (ClassCastException e) {
						logger.fatal(e);
					}
				}
			}
    }

    @Override
    public int hashCode() {
    	return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
		if (obj == null) {
		    return false;
		} else if (obj instanceof SimNode) {
		    return compareTo((SimNode) obj) == 0;
		} else {
		    return false;
		}
    }

    public int compareTo(SimNode o) {
		if (o == null)
		    return -1;
		if (hasVariable(AVariable.COMMON_IDENTIFIERS.ID))
		    return ((String) (((DefaultVariable)this
			    .getVariableById(AVariable.COMMON_IDENTIFIERS.ID)).getData()))
			    .compareTo((String) ((DefaultVariable)(o.getVariableById(AVariable.COMMON_IDENTIFIERS.ID)
				    )).getData());
		else
		    return -1;
    }
    
    public String getId() {
    	return getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
    }

	public List<SimLink> getConnectedLinksSim() {
		return new ArrayList<SimLink>(links.values());
	}

	public boolean isConnectedTo(SimNode b) {
		List<SimLink> links = getConnectedLinksSim();
		for(int i=0; i<links.size(); i++) {
			if(links.get(i).getOtherNode(this).equals(b)) {
				return true;
			}
		}
		return false;
	}
	
	public IUserNode spawnNewNode() {
		SimNode n;
		try {
			n = new SimNode(getUserLogic().getClass().newInstance());
			n.registerVariable( new DefaultVariable(AVariable.COMMON_IDENTIFIERS.ID, "spawn_node_"+(Math.random()*Double.MAX_VALUE)));
			n.registerVariable( new DefaultVariable(AVariable.COMMON_IDENTIFIERS.POSITION, new Point3d()));
			Simulator.getInstance().registerComponent(n);
			Dispatcher.forwardEvent( IVidisEvent.LayoutReLayout );
			return n.getUserLogic();
		} catch (InstantiationException e) {
			logger.error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		}
		return null;
	}
}