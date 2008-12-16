package vidis.modules.mstAlgorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

/**
 * This module node uses the Echo-Algorithm to determine the
 * Minimum-Spanning-Tree (MST) and then You can Ping a Node
 * to trace the path taken.
 * @author Dominik
 *
 */
public class MSTNode extends AUserNode {
	private static Logger logger = Logger.getLogger(MSTNode.class);
	
	/**
	 * this is the state
	 * @author Dominik
	 *
	 */
	private enum State {
		WHITE( ColorType.WHITE ),
		RED( ColorType.RED ),
		GREEN( ColorType.GREEN );
		private ColorType color;
		private State( ColorType c ) {
			this.color = c;
		}
		public ColorType getColor() {
			return color;
		}
	};
	
	/**
	 * stores the state
	 */
	private Map<String, State> currentState = new HashMap<String, State>();
	/**
	 * stores the counter for a certain query
	 */
	private Map<String, Integer> currentCounter = new HashMap<String, Integer>();
	/**
	 * stores the parent for a certain query
	*/
	private Map<String, String> currentParent = new HashMap<String, String>();
	/**
	 * stores the child information for a child (key)
	 * 
	 * this means that the MSTNode.getId() == map::key is reachable using one of the links referred
	 * 
	 * of course when sending it should be obvious that one should take the shortest
	 */
//	private Map<String, List<IUserLink>> currentChild = new HashMap<String, List<IUserLink>>();

	private static boolean TESTING = false;
	private static boolean INITIALLY_EXPLORE = true;
	
	@Override
	public void init() {
		// set testing mode
		TESTING = false;
		INITIALLY_EXPLORE = true;
		
		// now do real init
		
		// set my state
		currentState.put(getId(), State.WHITE);
		
		// some other init stuff
		if(TESTING && getId().equalsIgnoreCase("node01")) {
			explore();
		} else if(INITIALLY_EXPLORE) {
			explore();
		}
	}

	@Display(name="Show MST")
	public void showMST() {
		// send ping to everybody
		for(Entry<String, Map<Long, IUserLink>> e : reachableChilds.entrySet()) {
			mst(e.getKey());
		}
	}
	
	private void mst(String nodeId) {
		if(nodeId.equals(getId())) {
			// local host
			logger.info("PING "+nodeId+"(localhost): OK");
		} else {
			if(reachableChilds.containsKey(nodeId)) {
				long min = Long.MAX_VALUE;
				Entry<Long, IUserLink> minEntry = null;
				// send it to shortest edge
				for(Entry<Long, IUserLink> e : reachableChilds.get(nodeId).entrySet()) {
					if(e.getKey() < min) {
						min = e.getKey();
						minEntry = e;
					}
				}
				if(minEntry != null && min < Long.MAX_VALUE) {
					sendMst(new MstPacket(getNewPacketId(),getId(),nodeId), minEntry.getValue());
				}
			} else {
				// unreachable (??)
				logger.info("Target unreachable from here!");
			}
		}
	}
	
	private void sendMst(MstPacket pOld, IUserLink linkToSource) {
		MstPacket p = new MstPacket(pOld);
		send(p, linkToSource);
	}

	@Display(name="Testping: node08")
	public void testPing() {
		ping("node08");
	}
	
	@Display(name="Explore")
	public void explore() {
		// reset reachable nodes
		reachableChilds.clear();
		// set state to red (pending)
		currentState.put(getId(), State.RED);
		// send explore packet
		for(IUserLink l : getConnectedLinks()) {
			sendExplore(l);
		}
	}
	
	@Display(name="Ping Node")
	public void ping(MSTNode node) {
		ping(node.getId());
	}
	
	public void ping(String nodeId) {
		if(nodeId.equals(getId())) {
			// local host
			logger.info("PING "+nodeId+"(localhost): OK");
		} else {
			if(reachableChilds.containsKey(nodeId)) {
				long min = Long.MAX_VALUE;
				Entry<Long, IUserLink> minEntry = null;
				// send it to shortest edge
				for(Entry<Long, IUserLink> e : reachableChilds.get(nodeId).entrySet()) {
					if(e.getKey() < min) {
						min = e.getKey();
						minEntry = e;
					}
				}
				if(minEntry != null && min < Long.MAX_VALUE) {
					sendPing(new PingPacket(getNewPacketId(),getId(),nodeId), minEntry.getValue());
				}
			} else {
				// unreachable (??)
				logger.info("Target unreachable from here!");
			}
		}
	}
	
	// reset ids
	private List<Integer> resetPacketsSeen = new LinkedList<Integer>();
	
	// reset color packet
	@Display(name="Reset link colors")
	public void resetLinkColors() {
		int id = getNewPacketId();
		for(IUserLink l : getConnectedLinks()) {
			send(new AMSTPacket(AMSTPacket.Type.RESETCOLOR, id, getId()) {
				
			}, l);
		}
		resetPacketsSeen.add(id);
	}
	
	@DisplayColor
	public ColorType getColor() {
		if(TESTING && getId().equalsIgnoreCase("node01")) {
			return currentState.get(getId()).getColor();
		} else {
			return ColorType.GREY;
		}
	}
	
	public void execute() {
	}
	
	protected int getNewPacketId() {
		return AMSTPacket.generateRandomId();
	}
	
	public String getId() {
		return super.getId();
	}
	
	/**
	 * Remembers the parent of a explore round as a mapping
	 * exploreNodeId -> packetId -> parentNodeLink
	 * This is used at Echo packets in order to know where to
	 * forward the answer Echo to.
	 */
	private Map<String, Map<Long, IUserLink>> reachableChilds = new HashMap<String, Map<Long,IUserLink>>();
	
	protected void sendExplore(IUserLink l) {
		ExplorePacket p = new ExplorePacket(getNewPacketId(), getId());
		send(p, l);
	}
	protected void sendExplore(ExplorePacket pOld, IUserLink l) {
		ExplorePacket p = new ExplorePacket(pOld);
		send(p, l);
	}
	protected void sendEcho(ExplorePacket pOld, IUserLink l) {
		Map<String, Long> hm = new HashMap<String, Long>();
		hm.put(getId(), l.getDelay());
		EchoPacket p = new EchoPacket(pOld.getId(), pOld.getQueryierId(),hm);
		send(p, l);
	}
	protected void sendEcho(EchoPacket pOld, IUserLink l) {
		Map<String, Long> hm = new HashMap<String, Long>();
		// update old stuff by adding the current distance
		for(Entry<String, Long> e : pOld.getDistances().entrySet()) {
			hm.put(e.getKey(), e.getValue() + l.getDelay());
		}
		// and now put myself
		hm.put(getId(), l.getDelay());
		// finally send it
		EchoPacket p = new EchoPacket(pOld.getId(), pOld.getQueryierId(), hm);
		send(p, l);
	}
	private void sendPong(PingPacket pOld, IUserLink linkToSource) {
		PongPacket p = new PongPacket(pOld);
		send(p, linkToSource);
	}
	private void sendPing(PingPacket pOld, IUserLink linkToSource) {
		PingPacket p = new PingPacket(pOld);
		send(p, linkToSource);
	}
	private void sendPong(PongPacket pOld, IUserLink linkToSource) {
		PongPacket p = new PongPacket(pOld);
		send(p, linkToSource);
	}
	protected void receive(EchoPacket p) {
		// check if I am the querier
		if(p.getQueryierId().equals(getId())) {
			// fine, we're done with this
			logger.info("WE'RE DONE FOR: "+p.getDistances() + "!");
			// put them to the parents list
			for(Entry<String, Long> e : p.getDistances().entrySet()) {
				if(!reachableChilds.containsKey(e.getKey())) {
					reachableChilds.put(e.getKey(), new HashMap<Long, IUserLink>());
				}
				reachableChilds.get(e.getKey()).put(e.getValue(), p.getLinkToSource());
			}
		} else {
			for ( IUserLink l : getConnectedLinks() ) {
				// respond to ONLY the parent by sending a echo
				if(((MSTNode)l.getOtherNode(this)).getId().equals(currentParent.get(p.getQueryierId()))) {
					sendEcho(p, l);
				}
			}
		}
	}
	protected void receive(ExplorePacket p) {
		// set / reset white state
		if( !currentState.containsKey(p.getQueryierId()) || currentState.get(p.getQueryierId()).equals(State.GREEN)) {
			currentState.put(p.getQueryierId(), State.WHITE);
		}
		// real algorithm
		if ( currentState.get(p.getQueryierId()) == State.WHITE ) {
			currentState.put(p.getQueryierId(), State.RED);
			for ( IUserLink l : getConnectedLinks() ) {
				if ( ! l.equals(p.getLinkToSource()) ) {
					sendExplore(p, l);
				}
			}
			// set first neighbour
			currentParent.put( p.getQueryierId(), p.getSourceId());
		}
		// increase counter
		currentCounter.put(p.getQueryierId(), currentCounter.get(p.getQueryierId())+1);
		
		// handle green state on every message
		if ( currentCounter.get(p.getQueryierId()) == getConnectedLinks().size() ) {
			currentState.put(p.getQueryierId(), State.GREEN);
			handleGreen(p);
		}
	}
	
	private void handleGreen(ExplorePacket p) {
		if(currentState.get(p.getQueryierId()) == State.GREEN || currentCounter.get(p.getQueryierId()) == getConnectedLinks().size()) {
			// check if I am the querier
			if(p.getQueryierId().equals(getId())) {
				// fine, we're done with this
				logger.info("WE'RE DONE FOR: "+getId()+"!");
			} else {
				for ( IUserLink l : getConnectedLinks() ) {
					// respond to ONLY the parent by sending a echo
					if(((MSTNode)l.getOtherNode(this)).getId().equals(currentParent.get(p.getQueryierId()))) {
						sendEcho(p, l);
					}
				}
			}
		} else {
			// cannot handle green
		}
	}
	protected void receive(PingPacket p) {
		// decide:
		if(p.getTargetId().equals(getId())) {
			// am I receiver -> send Pong back
			sendPong(p, p.getLinkToSource());
		} else {
			// else -> forward to shortest edge
			// ask knowledgebase
			if(reachableChilds.containsKey(p.getTargetId())) {
				long min = Long.MAX_VALUE;
				Entry<Long, IUserLink> minEntry = null;
				// send it to shortest edge
				for(Entry<Long, IUserLink> e : reachableChilds.get(p.getTargetId()).entrySet()) {
					if(e.getKey() < min) {
						min = e.getKey();
						minEntry = e;
					}
				}
				if(minEntry != null && min < Long.MAX_VALUE) {
					sendPing(p, minEntry.getValue());
				}
			} else {
				// unreachable (??)
				logger.info("Target unreachable from here!");
			}
		}
	}

	protected void receive(PongPacket p) {
		// decide:
		if(p.getTargetId().equals(getId())) {
			// am I receiver -> send Pong back
			logger.info("RECEIVED PONG");
		} else {
			// else -> forward to shortest edge
			// ask knowledgebase
			if(reachableChilds.containsKey(p.getTargetId())) {
				long min = Long.MAX_VALUE;
				Entry<Long, IUserLink> minEntry = null;
				// send it to shortest edge
				for(Entry<Long, IUserLink> e : reachableChilds.get(p.getTargetId()).entrySet()) {
					if(e.getKey() < min) {
						min = e.getKey();
						minEntry = e;
					}
				}
				if(minEntry != null && min < Long.MAX_VALUE) {
					sendPong(p, minEntry.getValue());
				}
			} else {
				// unreachable (??)
				logger.info("Target unreachable from here!");
			}
		}
	}
	
	protected void receive(MstPacket p) {
		// decide:
		if(p.getTargetId().equals(getId())) {
			// am I receiver -> send Pong back
		} else {
			// else -> forward to shortest edge
			// ask knowledgebase
			if(reachableChilds.containsKey(p.getTargetId())) {
				long min = Long.MAX_VALUE;
				Entry<Long, IUserLink> minEntry = null;
				// send it to shortest edge
				for(Entry<Long, IUserLink> e : reachableChilds.get(p.getTargetId()).entrySet()) {
					if(e.getKey() < min) {
						min = e.getKey();
						minEntry = e;
					}
				}
				if(minEntry != null && min < Long.MAX_VALUE) {
					sendMst(p, minEntry.getValue());
				}
			} else {
				// unreachable (??)
				logger.info("Target unreachable from here!");
			}
		}
	}

	public void receive(AMSTPacket p) {
		if( !currentCounter.containsKey(p.getQueryierId())) {
			currentCounter.put(p.getQueryierId(), 0);
		}
		logger.info("receive: " + p);
		switch(p.getType()) {
			case RESETCOLOR:
				// forward if not seen yet
				if(!resetPacketsSeen.contains(p.getId())) {
					for(IUserLink l : getConnectedLinks()) {
						// all but sender
						if(!l.equals(p.getLinkToSource())) {
							send(new AMSTPacket(p.getType(), p.getId(), p.getQueryierId()) {
							},l);
						}
					}
					resetPacketsSeen.add(p.getId());
				} else {
					// do not forward
				}
				break;
			case ECHO:
				receive((EchoPacket) p);
				break;
			case EXPLORE:
				receive((ExplorePacket) p);
				break;
			case PING:
				receive((PingPacket) p);
				break;
			case PONG:
				receive((PongPacket) p);
				break;
			case MSTPACKET:
				receive((MstPacket) p);
				break;
		}
	}
	
	public void receive(IUserPacket packet) {
		if(packet instanceof AMSTPacket) {
			receive((AMSTPacket)packet);
		} else {
			logger.warn("receive & cannot handle: " + packet);
		}
	}

}
