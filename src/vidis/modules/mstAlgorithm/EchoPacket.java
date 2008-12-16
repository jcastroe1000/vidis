package vidis.modules.mstAlgorithm;

import java.util.Map;

public class EchoPacket extends AMSTPacket {
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
