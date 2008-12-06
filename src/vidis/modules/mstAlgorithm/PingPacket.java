package vidis.modules.mstAlgorithm;

import org.apache.log4j.Logger;

public class PingPacket extends AMSTPacket {
	private static Logger logger = Logger.getLogger(PingPacket.class);
	public PingPacket(int id, String senderId) {
		super(Type.PING, id, senderId);
	}
	public PingPacket(PingPacket p) {
		super(p);
	}
}
