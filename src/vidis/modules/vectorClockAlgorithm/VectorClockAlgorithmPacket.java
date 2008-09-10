package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;

@ComponentColor(color = ColorType.LIGHT_GREY)
public class VectorClockAlgorithmPacket extends AUserPacket {
    private VectorTime time = new VectorTime();
    public VectorClockAlgorithmPacket(VectorTime time) {
    	this.time.update(time);
    }
    public VectorTime getTime() {
    	return time;
    }
    public String getName() {
    	return getTime().toString();
    }

    public String toString() {
    	return getName();
    }
}