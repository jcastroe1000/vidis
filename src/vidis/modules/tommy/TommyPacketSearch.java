package vidis.modules.tommy;

import org.apache.log4j.Logger;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.DisplayColor;

public class TommyPacketSearch extends AUserPacket {
	private static Logger logger = Logger.getLogger(TommyPacketSearch.class);

	private float pseudoHash;
	private int range;
	
	@DisplayColor
	public ColorType color = ColorType.GREEN;
	
	public TommyPacketSearch(float search, int id) {
		pseudoHash = search;
		range = id;
	}
	
	public TommyPacketSearch(TommyPacketSearch copy) {
		pseudoHash = copy.getSearch();
		range = copy.getId();
	}
	
	public float getSearch() {
		return pseudoHash;
	}
	
	public int getId() {
		return range;
	}
}
