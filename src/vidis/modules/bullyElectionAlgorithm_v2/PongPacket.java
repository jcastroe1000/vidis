package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.Display;

public class PongPacket extends ABullyPacket {

    private String id;
    private String senderId;

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