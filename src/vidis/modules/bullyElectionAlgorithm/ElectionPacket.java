package vidis.modules.bullyElectionAlgorithm;

import vidis.data.AUserPacket;

public class ElectionPacket extends AUserPacket {

    private double id;

    public ElectionPacket(double id) {
    	this.id = id;
    }

    public double getBullyId() {
    	return id;
    }
}