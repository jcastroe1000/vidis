package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;
import vidis.modules.byzantineGenerals.APacket.PacketType;

public abstract class ANode extends AUserNode {
	private static Logger logger = Logger.getLogger(ANode.class);
	
	protected enum NodeType {
		GOOD(ColorType.GREEN),
		BAD(ColorType.RED),
		DONTKNOW(ColorType.ORANGE);
		
		private ColorType color;
		private NodeType( ColorType c ) {
			this.color = c;
		}
		public ColorType getColor() {
			return color;
		}
	};
	
	@DisplayColor
	public ColorType getColor() {
		return getNodeType().getColor();
	}
	
	protected abstract NodeType getNodeType();

	@Override
	public void init() {
		// don't need to do anything
	}
	
	@Display(name="General: ATTACK!")
	public void sendAttack() {
		sendAttackPacket(null);
	}
	@Display(name="General: RETREAT!")
	public void sendRetreat() {
		sendRetreatPacket(null);
	}
	
	protected void sendAttackPacket(IUserLink notToThisSource) {
		for(IUserLink l : getConnectedLinks()) {
			if(l.equals(notToThisSource)) {
				// do not send
			} else {
				send(new AttackPacket(), l);
			}
		}
	}
	protected void sendRetreatPacket(IUserLink notToThisSource) {
		for(IUserLink l : getConnectedLinks()) {
			if(l.equals(notToThisSource)) {
				// do not send
			} else {
				send(new RetreatPacket(), l);
			}
		}
	}
	
	protected void receive(APacket p) {
		switch(getNodeType()) {
			case GOOD:
				// propagate correct message
				if(p.getPacketType().equals(PacketType.ATTACK)) {
					sendAttackPacket(p.getLinkToSource());
				} else if(p.getPacketType().equals(PacketType.RETREAT)) {
					sendRetreatPacket(p.getLinkToSource());
				}
				break;
			case BAD:
				// propagate bad message
				if(p.getPacketType().equals(PacketType.ATTACK)) {
					sendRetreatPacket(p.getLinkToSource());
				} else if(p.getPacketType().equals(PacketType.RETREAT)) {
					sendAttackPacket(p.getLinkToSource());
				}
				break;
			case DONTKNOW:
				// propagate random (good/bad) message
				if(Math.random() < 0.5) {
					sendRetreatPacket(p.getLinkToSource());
				} else {
					sendAttackPacket(p.getLinkToSource());
				}
				break;
		}
	}

	public void receive(IUserPacket packet) {
		if(packet instanceof APacket) {
			receive((APacket) packet);
		}
	}

	public void execute() {
		// basically don't need to do anything
	}
}
