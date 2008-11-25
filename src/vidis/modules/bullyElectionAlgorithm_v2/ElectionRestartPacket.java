package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

public class ElectionRestartPacket extends ABullyPacket {

    private String id;
    
    public ElectionRestartPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @DisplayColor
    public ColorType color = ColorType.BLUE;
    
    @Display ( name="name" )
    public String toString() {
    	return "Restart{"+getBullyId()+","+getHops()+"/"+getMaxHops()+"}";
    }

	@Override
	public int getMaxHops() {
		return 15;
	}
}