package vidis.modules.mstAlgorithm;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
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

	@Override
	public void init() {
		if(getId().equalsIgnoreCase("node01")) {
			// send explore packet
			for(IUserLink l : getConnectedLinks()) {
				sendExplore(l);
			}
		}
	}
	public void execute() {
	}
	
	protected int getNewPacketId() {
		return AMSTPacket.generateRandomId();
	}
	
	// ----------- echo stuff ---------- //
	
	protected void receive(EchoPacket p) {
		// append myself to packet trace
		// forward to parent
	}
	
	// ----------- explore stuff ---------- //
	/**
	 * Remembers the parent of a explore round as a mapping
	 * exploreNodeId -> packetId -> parentNodeLink
	 * This is used at Echo packets in order to know where to
	 * forward the answer Echo to.
	 */
	private Map<String, Map<Integer, IUserLink>> parents = new HashMap<String, Map<Integer,IUserLink>>();
	/**
	 * Remembers the explores from a sender so that we
	 * can decide whether we handled this exploring round
	 * already or if we have to handle it now.
	 */
	private Map<String, List<Integer>> explores = new HashMap<String, List<Integer>>();
	
	protected void sendExplore(IUserLink l) {
		ExplorePacket p = new ExplorePacket(getNewPacketId(), getId());
		send(p, l);
	}
	protected void sendExplore(ExplorePacket pOld, IUserLink l) {
		ExplorePacket p = new ExplorePacket(pOld);
		send(p, l);
	}
	protected void sendEcho(ExplorePacket pOld, IUserLink l) {
		EchoPacket p = new EchoPacket(pOld.getId(), pOld.getSenderId());
	}
	protected void receive(ExplorePacket p) {
		// check if we sent this id already to everybody
		if(!explores.containsKey(p.getSenderId())) {
			explores.put(p.getSenderId(), new LinkedList<Integer>());
		}
		if(explores.containsKey(p.getSenderId())) {
			if(explores.get(p.getSenderId()).contains(p.getId())) {
				// already explored this id, forget it
			} else {
				// new explore round, let's do some work
				{
					// store parent
					if(!parents.containsKey(p.getSenderId())) {
						parents.put(p.getSenderId(), new HashMap<Integer, IUserLink>());
					}
					if(parents.containsKey(p.getSenderId())) {
						if(parents.get(p.getSenderId()).containsKey(p.getId())) {
							// already seen this, send echo
//							parents.get(p.getSenderId()).put(p.getId(), p.getLinkToSource());
							sendEcho(p, parents.get(p.getSenderId()).get(p.getId()));
						} else {
							// not seen this yet, store it
							parents.get(p.getSenderId()).put(p.getId(), p.getLinkToSource());
						}
					}
					// decide: do we have a parent for this sender id and have we informed everybody of this explore?
					if(explores.get(p.getSenderId()).contains(p.getId()) || getConnectedLinks().size() == 0) { // explore makes no sense, send Echo
						// answer sender with Echo
						sendEcho(p, parents.get(p.getSenderId()).get(p.getId()));
					} else { // explore makes sense, propagate explore
						// forward to everybody but sender
						for(IUserLink l : getConnectedLinks()) {
							if(!l.equals(p.getLinkToSource())) {
								sendExplore(p, l);
							}
						}
					}
					// remember exploring round
					explores.get(p.getSenderId()).add(p.getId());
				}
			}
		}
	}
	protected void receive(PingPacket p) {
		// decide:
		// am I receiver -> send Pong back
		// else -> forward to shortest edge
	}
	protected void receive(PongPacket p) {
		// decide:
		// am I receiver -> DONE
		// else -> forward to shortest edge
	}
	
	public void receive(AMSTPacket p) {
		logger.info("receive: " + p);
		switch(p.getType()) {
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
