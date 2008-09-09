package vidis.modules.flooding;

import vidis.data.AUserNode;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayType;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

@ComponentInfo(name = "FloodNode")
public class FloodNode extends AUserNode {

	@Display(name = "flood sent", type = DisplayType.SHOW_SWING)
	public boolean floodSent = false;
	
	public void execute() {
		if (!floodSent) {
			for (IUserLink link : this.getConnectedLinks()) {
				System.out.println("send floodpacket");
				send(new FloodPacket(this, 0), link, 1 + (long) (Math.random() * 2));
			}
			floodSent = true;
		} else {
			// no action
		}
	}

	private void receive(FloodPacket packet) {
		// check if the sender was me
		System.out.print("rcv floodpacket");
		if (packet.getCreator().equals(this)) {
			// Logger.output(LogLevel.DEBUG, this, "rcv(OWN) => SINK");
		} else {
			System.out.print(" ==> forward");
			// send on all links but from who the flood packet came a answer
			for (IUserLink link : this.getConnectedLinks()) {
				if (!packet.getLinkToSource().equals(link)) {
					// Logger.output(LogLevel.DEBUG, this, "rcv('" + packet.getCreator() +
					// "') => flood '" + link.getOtherNode(this) + "'!");
					send(new FloodPacket(packet), link, 1 + (long) (Math.random() * 2));
				} else {
					// Logger.output(LogLevel.DEBUG, this, "rcv('" + packet.getCreator() +
					// "') => no flood '" + link.getOtherNode(this) + "'!");
				}
			}
		}
		System.out.println();
	}

	public void receive(IUserPacket packet) {
		if (packet instanceof FloodPacket) {
			receive((FloodPacket) packet);
		} else {
			//Logger.output(LogLevel.ERROR, this, "receive 'unknown' packet from " + packet.getSource());
		}

	}
}
