package vidis.modules.mstAlgorithm;


public class MstPacket extends AMSTPacket {
	private String targetId;
	public MstPacket(int id, String senderId, String targetId) {
		super(Type.MSTPACKET, id, senderId);
		this.targetId = targetId;
	}

	public MstPacket(MstPacket p) {
		this(p.getId(), p.getQueryierId(), p.getTargetId());
	}
	
	public String getTargetId() {
		return targetId;
	}
}
