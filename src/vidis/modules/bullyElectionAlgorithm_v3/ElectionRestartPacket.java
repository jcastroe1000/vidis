package vidis.modules.bullyElectionAlgorithm_v3;

import vidis.data.AUserPacket;
import vidis.data.annotation.Display;

public class ElectionRestartPacket extends AUserPacket {

    private String id;
    
    public ElectionRestartPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @Display ( name="name" )
    public String toString() {
    	return "Restart{"+getBullyId()+"}";
    }
}