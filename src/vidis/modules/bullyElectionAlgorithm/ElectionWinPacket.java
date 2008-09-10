package vidis.modules.bullyElectionAlgorithm;

import vidis.data.AUserPacket;

public class ElectionWinPacket extends AUserPacket {

    private double id;

    public ElectionWinPacket(double id) {
    	this.id = id;
    }

    public double getBullyId() {
    	return id;
    }
}