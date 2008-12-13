package vidis.modules.mstAlgorithm;

import java.util.Map;

import org.apache.log4j.Logger;

public class EchoPacket extends AMSTPacket {
	private static Logger logger = Logger.getLogger(EchoPacket.class);
	private Map<String, Long> distances;
	public EchoPacket(int id, String senderId, Map<String, Long> distances) {
		super(Type.ECHO, id, senderId);
		this.distances = distances;
	}
	public EchoPacket(EchoPacket p) {
		super(p);
	}
	public Map<String, Long> getDistances() {
		return distances;
	}
}
