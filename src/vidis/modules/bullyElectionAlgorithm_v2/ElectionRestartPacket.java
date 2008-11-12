package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.Display;

public class ElectionRestartPacket extends ABullyPacket {

    private String id;
    
    public ElectionRestartPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @Display ( name="name" )
    public String toString() {
    	return "Restart{"+getBullyId()+","+getHops()+"/"+getMaxHops()+"}";
    }

	@Override
	public int getMaxHops() {
		return 15;
	}
}