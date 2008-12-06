package vidis.modules.mstAlgorithm;

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
		// TODO Auto-generated method stub

	}
	int c1 = 0;
	int c2 = 0;
	public void execute() {
		if(c1 % 5 == 0) {
			for(IUserLink l : getConnectedLinks()) {
				int mod = c2 % AMSTPacket.Type.values().length;
				if(mod == AMSTPacket.Type.ECHO.ordinal()) {
					send(new EchoPacket(EchoPacket.generateRandomId(), getId()), l);
				} else if(mod == AMSTPacket.Type.EXPLORE.ordinal()) {
					send(new ExplorePacket(ExplorePacket.generateRandomId(), getId()), l);
				} else if(mod == AMSTPacket.Type.PING.ordinal()) {
					send(new PingPacket(PingPacket.generateRandomId(), getId()), l);
				} else if(mod == AMSTPacket.Type.PONG.ordinal()) {
					send(new PongPacket(PongPacket.generateRandomId(), getId()), l);
				}
			}
			c2++;
		}
		c1++;
	}
	
	public void receive(AMSTPacket p) {
		logger.info("receive: " + p);
	}
	
	public void receive(IUserPacket packet) {
		if(packet instanceof AMSTPacket) {
			receive((AMSTPacket)packet);
		} else {
			logger.warn("receive & cannot handle: " + packet);
		}
	}

}
