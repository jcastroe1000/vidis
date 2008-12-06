package vidis.modules.mstAlgorithm;

import org.apache.log4j.Logger;

public class PongPacket extends AMSTPacket {
	private static Logger logger = Logger.getLogger(PongPacket.class);
	public PongPacket(int id, String senderId) {
		super(Type.PONG, id, senderId);
	}
	public PongPacket(PongPacket p) {
		super(p);
	}
}
