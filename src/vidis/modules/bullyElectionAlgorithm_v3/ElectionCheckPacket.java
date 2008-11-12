package vidis.modules.bullyElectionAlgorithm_v3;

import vidis.data.AUserPacket;
import vidis.data.annotation.Display;

public class ElectionCheckPacket extends AUserPacket {

    private String id;
    private String senderId;

    public ElectionCheckPacket(String id, String senderId) {
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
    	return "Check{"+getBullyId()+","+getSenderId()+"}";
    }
}