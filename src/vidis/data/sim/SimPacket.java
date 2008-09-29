package vidis.data.sim;

import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;
import vidis.data.var.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.sim.Simulator;
import vidis.sim.exceptions.ObstructInitCallException;
import vidis.sim.exceptions.ObstructInitRuntimeCallException;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.model.impl.Packet;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.mvc.api.Dispatcher;

public class SimPacket extends AComponent implements ISimPacketCon {

	private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger( SimPacket.class );
	
    protected IUserPacket logic;

    // -------- sim fields ------- //
    private SimLink through;
    private SimNode from;
    private SimNode to;

    private IVisObject visObject;
    
    public SimPacket(IUserPacket packet, SimLink link, SimNode source, SimNode target) {
		super();
		init();
		init(packet);
		// set variables
		setThrough(link);
		setFrom(source);
		setTo(target);
		
		//System.err.println(getVariableIds());
		// set 3d object; this (SimPacket) class should be fully initialized at this
		// call!
		// setObject3D(new Packet3D(this));

    }

    private void init() {
		// yet nothing as we have no lists, maps or other composite objects
		// generate id for this packet
    }

    private void init(IUserPacket packet) {
		setLogic(packet);
		try {
			initVars();
		    packet.init(this);
		} catch (ObstructInitCallException e) {
		    // never happens, if anyway throw a real severe exception
		    throw new ObstructInitRuntimeCallException(e.getCause());
		}
    }

    public void execute() {
		super.execute();
		if (!isSleeping()) {
		    this.logic.execute();
		} else {
//		    Logger.output(LogLevel.DEBUG, this, "skip logic.execute()");
		}
		super.checkVariablesChanged();
    }

    public final IUserPacket getUserLogic() {
    	return logic;
    }

    public final double getAlpha() {
    	return getThrough().getAlphaForPacket(this);
    }

    public final int getDirection() {
    	return getThrough().getDirectionForPacket(this);
    }

    public final SimLink getThrough() {
    	return through;
    }

    private final void setFrom(SimNode from) {
    	this.from = from;
    }

    private final void setLogic(IUserPacket logic) {
    	this.logic = logic;
    }

    private final void setThrough(SimLink through) {
    	this.through = through;
    }

    private final void setTo(SimNode to) {
    	this.to = to;
    }

    public IUserNode getFrom() {
    	return from.getUserLogic();
    }

    public IUserNode getTo() {
    	return to.getUserLogic();
    }

    public IUserLink getLink() {
    	return through.getUserLogic();
    }
    
    private DefaultVariable noPos = new DefaultVariable( AVariable.COMMON_IDENTIFIERS.POSITION, new Vector3d( 100,100,100 ) );
    
    private AVariable positionOverride() {
    	// ugly workaround
    	if ( Simulator.getInstance().getPlayer().isPaused() ) {
    		return super.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION );
    	}
    	
    	
    	double alpha = getThrough().getAlphaForPacket( this );
    	if ( alpha == -1 ) {
    		kill();
    		return noPos;
    	}
	    	// FIXME
	//		double alpha = getThrough().getAlphaForPacket(this);
			double direction = getThrough().getDirectionForPacket(this);
			SimNode nodeA = getThrough().getNodeASim();
			SimNode nodeB = getThrough().getNodeBSim();
			SimNode nodeFrom = null, nodeTo = null;
			if ( direction < 0 ) {
				nodeFrom = nodeA;
				nodeTo = nodeB;
			} 
			else {
				nodeFrom = nodeB;
				nodeTo = nodeA;
			}
			Point3d posFrom = (Point3d) nodeFrom.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
			Point3d posTo = (Point3d) nodeTo.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
			// calculate pos
			Point3d pos = null;
		//	long stepsOnLink = getThrough().getStepsOnLinkForPacket( this );
			//Vector3d move = getThrough().getMove();
			//Vector3d moveScaled = new Vector3d( move );
			//moveScaled.scale( alpha );
			Vector3d position = new Vector3d();
			position.interpolate( posFrom, posTo, alpha );
			
			double alpham = 1 - alpha;
			// height:
			Vector3d up = new Vector3d( 0, 1, 0 );
			Vector3d tmp = new Vector3d();
			tmp.sub( posFrom, posTo );
			double length = tmp.length();
			tmp.interpolate( posFrom, posTo, 0.5 );
			Point3d M = new Point3d( tmp );
			tmp.scale( length / 2d, up );
			M.add( tmp );
			Point3d MxPosFrom = new Point3d();
			Point3d MxPosTo = new Point3d();
			MxPosFrom.interpolate( posFrom, M, alpha );
			MxPosTo.interpolate( posTo, M, alpham );
			position.interpolate( MxPosFrom, MxPosTo, alpha );
			
			
	//		if ( direction > 0 ) {
	//			position.negate();
	//		}
	//		Vector3d posv = new Vector3d( posFrom );
	//		posv.add( moveScaled );
			
	//		Vector3d way = new Vector3d();
	//		way.sub(posTo, posFrom);
	//		way.scale(alpha);
			pos = new Point3d( position );
			
			// set var or register var
			if( hasVariable( AVariable.COMMON_IDENTIFIERS.POSITION ) ) {
				// update
				((DefaultVariable)super.getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION)).update(pos);
			} else {
				registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.POSITION, pos));
			}
		return super.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION );
    }
    
    @Override
    public AVariable getVariableById(String id) throws ClassCastException {
    	if(id.equals(AVariable.COMMON_IDENTIFIERS.POSITION)) {
    		return positionOverride();
    	} else {
    		return super.getVariableById(id);
    	}
    }
    
    @Override
    public void kill() {
    	getThrough().getVisObject().delPacket( this.visObject );
    	logger.debug( "kill()" );
    	super.kill();
    	//logger.info("");
    	ObjectEvent oe = new ObjectEvent( IVidisEvent.ObjectUnregister, this.visObject );
    	Dispatcher.forwardEvent( oe );
    	this.visObject = null;
    }

	public void createVisObject() {
		visObject = new Packet(this, this.getThrough().getVisObject() );
		ObjectEvent nextEvent = new ObjectEvent( IVidisEvent.ObjectRegister, visObject );
		Dispatcher.forwardEvent( nextEvent );
	}
}