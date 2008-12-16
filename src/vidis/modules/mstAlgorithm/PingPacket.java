package vidis.modules.mstAlgorithm;


public class PingPacket extends AMSTPacket {
	private String targetId;
	public PingPacket(int id, String senderId, String targetId) {
		super(Type.PING, id, senderId);
		this.targetId = targetId;
	}
	public PingPacket(PingPacket p) {
		this(p.getId(), p.getQueryierId(), p.getTargetId());
	}
	public String getTargetId() {
		return targetId;
	}
}
