package vidis.modules.demo;

import vidis.data.AUserPacket;

public abstract class ADemoPacket extends AUserPacket {
	public String toString() {
		if(hasVariable("name")) {
			return getVariable("name").getData().toString();
		} else {
			return super.toString();
		}
	}
}
