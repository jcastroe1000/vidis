package vidis.modules.mstAlgorithm;

import vidis.data.AUserLink;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserPacket;

/**
 * This is a standard implementation. It provides everything a link
 * must can to connect nodes and deliver packets.
 * @author Dominik
 *
 */
@ComponentInfo(name = "Default Link")
public class DefaultLink extends AUserLink {
	private ColorType c = ColorType.BLACK;
	
	@DisplayColor
	public ColorType getColor() {
		return c;
	}
	public void execute() {
		for(IUserPacket p : getPacketsOnLink()) {
			if(p instanceof PingPacket) {
				// accept color of ping
				c = ((PingPacket)p).getColor();
			} else if(p instanceof PongPacket) {
				// accept color of pong
				c = ((PongPacket)p).getColor();
			} else if(p instanceof MstPacket) {
				c = ((MstPacket)p).getColor();
			} else {
				c = ColorType.BLACK;
			}
		}
	}
}
