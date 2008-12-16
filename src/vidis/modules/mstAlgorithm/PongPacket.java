package vidis.modules.mstAlgorithm;


public class PongPacket extends AMSTPacket {
	private String targetId;
	public PongPacket(int id, String senderId, String targetId) {
		super(Type.PONG, id, senderId);
		this.targetId = targetId;
	}
	public PongPacket(PongPacket p) {
		this(p.getId(), p.getQueryierId(), p.getTargetId());
	}
	public PongPacket(PingPacket p) {
		this(p.getId(), p.getTargetId(), p.getQueryierId());
	}
	public String getTargetId() {
		return targetId;
	}
}
