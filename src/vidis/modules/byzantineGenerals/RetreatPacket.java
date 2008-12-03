package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

public class RetreatPacket extends APacket {
	private static Logger logger = Logger.getLogger(RetreatPacket.class);

	@Override
	protected PacketType getPacketType() {
		return PacketType.RETREAT;
	}

}
