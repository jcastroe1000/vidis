package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.AUserPacket;

public abstract class ABullyPacket extends AUserPacket {
	protected int hopsLeft;
	public ABullyPacket() {
		setHops(getMaxHops());
	}
	public ABullyPacket(int hops) {
		super();
		setHops(hops);
	}
	public void setHops(int hops) {
		hopsLeft = hops;
	}
	public int getHops() {
		return hopsLeft;
	}
	public int getMaxHops() {
		return 15;
	}
}
