package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

public class PingPacket extends ABullyPacket {

    private String id;
    private String senderId;

    public PingPacket(String id, String senderId) {
    	this.id = id;
    	this.senderId = senderId;
    }

    public String getBullyId() {
    	return id;
    }
    
    public String getSenderId() {
    	return senderId;
    }
    
    @DisplayColor
    public ColorType color = ColorType.ORANGE;
    
    @Display ( name="name" )
    public String toString() {
    	return "Ping{"+getBullyId()+","+getSenderId()+","+getHops()+"/"+getMaxHops()+"}";
    }

	@Override
	public int getMaxHops() {
		return 15;
	}
}