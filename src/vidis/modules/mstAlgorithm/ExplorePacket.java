package vidis.modules.mstAlgorithm;


public class ExplorePacket extends AMSTPacket {
	public ExplorePacket(int id, String senderId) {
		super(Type.EXPLORE, id, senderId);
	}
	public ExplorePacket(ExplorePacket p) {
		super(p);
	}
	public String getSourceId() {
		return ((MSTNode)getSource()).getId();
	}
}
