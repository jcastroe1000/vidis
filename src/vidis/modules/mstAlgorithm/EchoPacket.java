package vidis.modules.mstAlgorithm;

import org.apache.log4j.Logger;

public class EchoPacket extends AMSTPacket {
	private static Logger logger = Logger.getLogger(EchoPacket.class);
	public EchoPacket(int id, String senderId) {
		super(Type.ECHO, id, senderId);
	}
	public EchoPacket(EchoPacket p) {
		super(p);
	}
}
