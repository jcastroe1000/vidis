package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

public class PongPacket extends ABullyPacket {

    private String id;
    private String senderId;
    
    @DisplayColor
    public ColorType color = ColorType.YELLOW;

    public PongPacket(String id, String senderId) {
    	this.id = id;
    	this.senderId = senderId;
    }

    public String getBullyId() {
    	return id;
    }
    
    public String getSenderId() {
    	return senderId;
    }
    
    @Display ( name="name" )
    public String toString() {
    	return "Pong{"+getBullyId()+","+getSenderId()+","+getHops()+"/"+getMaxHops()+"}";
    }
}