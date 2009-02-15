package vidis.modules.pingPong;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class PingPongNode extends AUserNode {
	@Override
	public void init() {
		// not needed
	}
	
	@Display(name="Ping!")
	public void ping() {
		for(IUserLink l : getConnectedLinks())
			send(new Ping(), l);
	}
	
	public void receive(IUserPacket packet) {
		if(packet instanceof Ping) {
			send(new Pong(), packet.getLinkToSource());
		} else if(packet instanceof Pong) {
			// say: hooray!
		}
	}

	public void execute() {
	}
}
