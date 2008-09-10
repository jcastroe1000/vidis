package vidis.data.sim;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import vidis.sim.Simulator;
import vidis.sim.exceptions.ObstructInitCallException;
import vidis.sim.exceptions.ObstructInitRuntimeCallException;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.model.impl.Link;
import vidis.ui.mvc.api.Dispatcher;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.var.AVariable;

public class SimLink extends AComponent implements ISimLinkCon {
	
	private static Logger logger = Logger.getLogger( SimLink.class );

    protected IUserLink logic;

    // -------- sim fields ------- //
    private SimNode a;
    private SimNode b;
    private long delay;
    private List<PacketQueueHolder> queue;

    private Link visObject;
    
    public Link getVisObject() {
		return visObject;
	}

	public SimLink(IUserLink link, long delay) {
		super();
		linkcount++;
		init();
		init(link);
		// set fields
		setDelay(delay);
		
		if ( visObject == null ) {
			visObject = new Link( this );
		}
    }

    private void init() {
    	this.queue = new LinkedList<PacketQueueHolder>();
    }

    @Override
    public void kill() {
		synchronized (queue) {
		    for (PacketQueueHolder pqh : queue) {
		    	pqh.packet.kill();
		    }
		    queue.clear();
		}
		super.kill();
    }

    private void init(IUserLink link) {
		this.logic = link;
		try {
			initVars();
		    link.init(this);
		} catch (ObstructInitCallException e) {
		    // never happens, if anyway throw a real severe exception
		    throw new ObstructInitRuntimeCallException(e.getCause());
		}
    }

    private void setDelay(long delay) {
    	this.delay = delay;
    }
    public void connect(SimNode a, SimNode b) {
		this.a = a;
		this.b = b;
		ObjectEvent oe = new ObjectEvent( IVidisEvent.ObjectRegister, visObject );
		Dispatcher.forwardEvent( oe );
    }

    public void execute() {
		processQueue();
		super.execute();
		if (!isSleeping()) {
		    this.logic.execute();
		} else
		    logger.debug("skip logic.execute()");
		super.checkVariablesChanged();
    }

	public IUserNode getNodeA() {
    	return a.getUserLogic();
    }

    public IUserNode getNodeB() {
    	return b.getUserLogic();
    }

    /**
     * retrieve the other node connected to this link
     * 
     * @param node
     *          this is me
     * @return the other node
     */
    public final SimNode getOtherNode(SimNode node) {
		if (getNodeASim().compareTo(node) == 0) {
		    return getNodeBSim();
		} else if (getNodeBSim().compareTo(node) == 0) {
		    return getNodeASim();
		} else {
		    logger.debug("getOtherNode(" + node
			    + ") => null");
		    return null;
		}
    }

    /**
     * retrieve the user link contained in this simulator link
     * 
     * @return the user link
     */
    public final IUserLink getUserLogic() {
    	return this.logic;
    }

    /**
     * retrieve the delay of this node
     * 
     * @return the delay
     */
    public final long getDelay() {
    	return this.delay;
    }

    public void send(SimPacket packet, SimNode to) {
		if (a.equals(to) || b.equals(to)) {
		    if (getDelay() <= 0) {
			// deliver immediatly
			deliver(packet, to);
		    } else {
			queue(packet, to);
		    }
		}
    }

    private void queue(SimPacket packet, SimNode to) {
    	queue.add(new PacketQueueHolder(packet, to, getDelay()));
    }

    private void deliver(SimPacket packet, SimNode to) {
    	to.receive(packet);
    }

    private void processQueue() {
		synchronized (queue) {
		    for (int i = 0; i < queue.size(); i++) {
			PacketQueueHolder tmp = queue.get(i);
			/*
			 * 
			 * if (getVariableById(String.class,
			 * "system.id").getData().compareToIgnoreCase("link11") == 0)
			 * Logger.output(LogLevel.DEBUG, this, "checkQueue(" + i + ":" +
			 * tmp.timeout + ":" + tmp.packet + ")");
			 */
			if (tmp.timeout < 0) {
			    // deliver
			    deliver(tmp.packet, tmp.to);
			    // remove from queue
			    if (queue.remove(tmp)) {
				i--;
			    }
			} else {
			    // queue
			    tmp.timeout--;
			    tmp.lastStepTime = System.currentTimeMillis();
			}
		    }
		}
    }

    private static class PacketQueueHolder {
		public PacketQueueHolder(SimPacket packet, SimNode to, long timeout) {
		    this.packet = packet;
		    this.to = to;
		    this.timeout = timeout;
		    this.lastStepTime = System.currentTimeMillis();
		    // this.startMillis = System.currentTimeMillis();
		    // this.lastMillis = System.currentTimeMillis();
		}
	
		SimPacket packet;
		SimNode to;
		long timeout;
		long lastStepTime;
		// long startMillis;
		// long lastMillis;
    }

    private PacketQueueHolder getPacketQueueHolderForPacket(SimPacket packet) {
		for (int i = 0; i < queue.size(); i++) {
		    PacketQueueHolder tmp = queue.get(i);
		    if (tmp.packet.equals(packet)) {
			return tmp;
		    }
		}
		return null;
    }

    
    public long getStepsOnLinkForPacket(SimPacket packet) {
    	logger.debug("getStepsOnLinkForPacket("+packet+");");
		PacketQueueHolder tmp = getPacketQueueHolderForPacket(packet);
		if (tmp != null) {
		    int dir = packet.getDirection();
		    // long durationLastStep = Simulator.getInstance().getLastStepDuration();
		    // long timeStart = tmp.startMillis;
		    long stepsToTarget = tmp.timeout;
		    long length = this.delay;
	
		    return length - stepsToTarget;
		}
		return -1;
    }
    
    /**
     * determine the alpha position of a packet
     * 
     * @param packet
     *          the packet to probe
     * @return -1: unknown, otherwise value in [0.0, 1.0]
     */
    // private double getSimpleAlphaForPacket(SimPacket packet) {
    // PacketQueueHolder tmp = getPacketQueueHolderForPacket(packet);
    // if (tmp != null) {
    // return (double) tmp.timeout / (double) getDelay();
    // }
    // return -1;
    // }
    // private long lastAlphaRealTime, lastRealTimeDiff;
    // private long lastTime = 0;
    // private double lastAlpha, deltaAlpha;
    
    
    public double getAlphaForPacket(SimPacket packet) {
    	logger.debug("getAlphaForPacket("+packet+");");
		PacketQueueHolder tmp = getPacketQueueHolderForPacket(packet);
		if ( tmp != null ) {
			long now = System.currentTimeMillis();
			
			long lastStepTime = tmp.lastStepTime;
			
			long timeOnThisStep = now - lastStepTime;
			
			long lastStepDuration = Simulator.getInstance().getLastStepDuration();
			
			// 100 -> lastStepDuration
			// ? -> timeOnThisStep
			
			double alpha1 = (double) timeOnThisStep / (double) lastStepDuration;
			
			// 100 -> stepsAll
			// ? -> stepsDone
			
			long stepsToDo = tmp.timeout;
			long stepsAll = this.getDelay();
			long stepsDone = stepsAll - stepsToDo;
			
//			long timeAll = stepsAll * lastStepDuration;
			
			double alpha0  = (double) stepsDone  / (double) stepsAll;
			
			double alpha1Scale = 1d / stepsAll;
			
			double alpha = alpha0 + alpha1Scale * alpha1;
			if ( alpha > 1 ) alpha = 1;
			return alpha;
		}
		return -1;
    }

    /**
     * determine the direction of a packet
     * 
     * @param packet
     *          the packet to find
     * @return 0: unknown, 1: to A, -1: to B
     */
    public int getDirectionForPacket(SimPacket packet) {
    	logger.debug("getDirectionForPacket("+packet+");");
		PacketQueueHolder tmp = getPacketQueueHolderForPacket(packet);
		if (tmp != null) {
		    if (tmp.to.equals(a)) {
			return 1;
		    } else if (tmp.to.equals(b)) {
			return -1;
		    }
		}
		return 0;
    }

    /**
     * Bewegungsvector von A nach B
     */
    private Vector3d move = null;

    public Vector3d getMove() {
    	logger.debug("getMove()");
		if (move == null)
		    calcMove();
		return move;
    }

    public void calcMove() {
    	logger.debug("calcMove()");
		// TODO safe checks
		Tuple3d a = (Tuple3d) getNodeASim().getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
		Tuple3d b = (Tuple3d) getNodeBSim().getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
		Vector3d AB = new Vector3d( b );
		AB.sub( a );
		Vector3d moveSimStep = new Vector3d( AB );
		//moveSimStep.scale( 1d / delay );
		// grob zum testen
		move = moveSimStep;
    }

    public SimNode getNodeASim() {
    	return a;
    }

    public SimNode getNodeBSim() {
    	return b;
    }

    public String toString() {
		return super.toString() + "{" + getNodeASim() + "-" + getNodeBSim()
			+ "}";
    }
    
    public final static String POINT_A = "virtual.pointA";
    public final static String POINT_B = "virtual.pointB";
    
    @Override
    public Set<String> getVariableIds() {
    	Set<String> ret = new HashSet<String>( super.getVariableIds() );
    	ret.add( POINT_A );
    	ret.add( POINT_B );
    	return ret;
    }
    static int linkcount = 0;
    @Override
    public AVariable getVariableById(String id) throws ClassCastException {
    	if ( id.equals( POINT_A ) ) {
    		SimNode simnode = getNodeASim();
    		if ( simnode == null) {
    			System.out.println(this + " LINKCOUNT = " + linkcount);
    			
    		}
    		AVariable v = simnode.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION );
    		return v;
    	}
    	else if ( id.equals( POINT_B ) ) {
    		SimNode simnode = getNodeBSim();
    		return simnode.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION );
    	}
    	else {
    		return super.getVariableById(id);
    	}
    }
}
