package vidis.ui.model.impl.guielements;

import javax.media.opengl.GL;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Packet;
import vidis.ui.model.structure.AGuiContainer;

public class PacketGuiElement extends BasicGuiContainer {
	//private static Logger logger = Logger.getLogger(PacketGuiElement.class);
	
	private Packet packet;
	
	public PacketGuiElement(Packet packet) {
		this.packet = packet;
	}
	
	private Packet getPacket() {
		return packet;
	}
	
	@Override
	public void renderContainer(GL gl) {
		// draw heading
		// FIXME
		// draw content
		// FIXME
	}

}
