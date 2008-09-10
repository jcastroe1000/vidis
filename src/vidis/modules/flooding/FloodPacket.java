package vidis.modules.flooding;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayType;

@ComponentColor(color = ColorType.LIGHT_GREY)
public class FloodPacket extends AUserPacket {
	@Display(name = "sent by")
	public FloodNode whoSentThis;
	@Display(name = "hop count")
	public int hopCount;

	public FloodPacket(FloodNode whoSentThis, int hopCount) {
		this.whoSentThis = whoSentThis;
		this.hopCount = hopCount;
	}

	public FloodPacket(FloodPacket packet) {
		this(packet.whoSentThis, packet.hopCount + 1);
	}

	@Display(name = "name", type = DisplayType.SHOW_3D_AND_SWING)
	public String getName() {
		return "flood(" + getCreator().getVariable("name").getData().toString() + ")";
	}

	public String toString() {
		return getName();
	}

	public FloodNode getCreator() {
		return this.whoSentThis;
	}
}