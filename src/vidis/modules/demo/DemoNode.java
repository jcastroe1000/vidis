package vidis.modules.demo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;

@ComponentInfo(name = "DemoNode")
@ComponentColor(color = ColorType.BLACK)
public class DemoNode extends AUserNode {

	private Map<IUserLink, Boolean> pingSent = new ConcurrentHashMap<IUserLink, Boolean>();
	private Map<IUserNode, Boolean> pongReceived = new ConcurrentHashMap<IUserNode, Boolean>();

	public void execute() {
		for (IUserLink link : this.getConnectedLinks()) {
			if (!pingSent.containsKey(link) || !pingSent.get(link)) {
				// store it
				pingSent.put(link, true);
				// Logger.output(this, "send Ping over " + link + " to " +
				// link.getOtherNode(this));
				// send ping on all links, 1 to 3 steps processing time
				send(new DemoPingPacket(), link, 1 + (long) (Math.random() * 2));
			}
		}
	}
	
	@Display(name="pongs")
	public String pongsReceived() {
		StringBuffer buff = new StringBuffer();
		for(IUserNode l : pongReceived.keySet()) {
			buff.append(l.toString() + "\n");
		}
		return buff.toString();
	}

	private void receive(DemoPingPacket packet) {
		// Logger.output(this, "receive Ping from " + packet.getSource());
		// Logger.output(this, "send Pong over " + packet.getLinkToSource() + " to "
		// + packet.getSource());
		// answer with pong, no processing time
		send(new DemoPongPacket(), packet.getLinkToSource());
	}

	private void receive(DemoPongPacket packet) {
		// Logger.output(this, "receive Pong from " + packet.getSource());
		// reset
		pongReceived.put(packet.getSource(), true);
		pingSent.put(packet.getLinkToSource(), false);
	}

	public void receive(IUserPacket packet) {
		if (packet instanceof DemoPingPacket) {
			receive((DemoPingPacket) packet);
		} else if (packet instanceof DemoPongPacket) {
			receive((DemoPongPacket) packet);
		} else {
			//Logger.output(LogLevel.ERROR, this, "receive 'unknown' packet from " + packet.getSource());
		}

	}
}
