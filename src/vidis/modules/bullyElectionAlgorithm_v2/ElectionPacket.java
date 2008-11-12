package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.AUserPacket;
import vidis.data.annotation.Display;

public class ElectionPacket extends AUserPacket {

    private String id;

    public ElectionPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @Display ( name="name" )
    public String toString() {
    	return "Elect{"+getBullyId()+"}";
    }
}