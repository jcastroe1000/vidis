package vidis.modules.bullyElectionAlgorithm_v1;

import vidis.data.AUserPacket;
import vidis.data.annotation.Display;

public class ElectionWinPacket extends AUserPacket {

    private String id;

    public ElectionWinPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @Display ( name="name" )
    public String toString() {
    	return "Win{"+getBullyId()+"}";
    }
}