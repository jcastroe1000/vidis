package vidis.modules.byzantineGenerals;


public class RetreatPacket extends APacket {
	public RetreatPacket(int id) {
		super(id);
	}

	@Override
	protected PacketType getPacketType() {
		return PacketType.RETREAT;
	}

}
