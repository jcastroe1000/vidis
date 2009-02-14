package vidis.modules.tommy;

import org.apache.log4j.Logger;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.DisplayColor;

public class TommyPacketResponse extends AUserPacket {
	private static Logger logger = Logger.getLogger(TommyPacketResponse.class);
	
	private int range;
	
	@DisplayColor
	public ColorType color = ColorType.RED;
	
	// usually includes download link, node id, data id
	public TommyPacketResponse(int id) {
		range = id;
	}
	
	public TommyPacketResponse(TommyPacketResponse copy) {
		range = copy.getId();
	}
	
	public int getId() {
		return range;
	}
}
