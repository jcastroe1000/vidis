package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

public class AttackPacket extends APacket {
	private static Logger logger = Logger.getLogger(AttackPacket.class);

	@Override
	protected PacketType getPacketType() {
		return PacketType.ATTACK;
	}

}
