package vidis.modules.mstAlgorithm;

import org.apache.log4j.Logger;

public class ExplorePacket extends AMSTPacket {
	private static Logger logger = Logger.getLogger(ExplorePacket.class);
	public ExplorePacket(int id, String senderId) {
		super(Type.EXPLORE, id, senderId);
	}
	public ExplorePacket(ExplorePacket p) {
		super(p);
	}
}
