package vidis.modules.byzantineGenerals;

import java.util.HashMap;
import java.util.Map;

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
	
	protected void mySend(APacket p, IUserLink l) {
		alreadySeen.put(p.getId(), true);
		send(p, l);
	}
	
	protected void sendAttackPacket(APacket notToThisSource) {
		for(IUserLink l : getConnectedLinks()) {
			if(notToThisSource != null) {
				if(l.equals(notToThisSource.getLinkToSource())) {
					// do not send
				} else {
					mySend(new AttackPacket(notToThisSource.getId()), l);
				}
			} else {
				mySend(new AttackPacket(), l);
			}
		}
	}
	protected void sendRetreatPacket(APacket notToThisSource) {
		for(IUserLink l : getConnectedLinks()) {
			if(notToThisSource != null) {
				if(l.equals(notToThisSource.getLinkToSource())) {
					// do not send
				} else {
					mySend(new RetreatPacket(notToThisSource.getId()), l);
				}
			} else {
				mySend(new RetreatPacket(), l);
			}
		}
	}
	
	private Map<Double, Boolean> alreadySeen = new HashMap<Double, Boolean>();
	
	private boolean alreadySeen(double id) {
		return alreadySeen.containsKey(id);
	}
	
	protected void receive(APacket p) {
		if(alreadySeen(p.getId())) {
			// already seen this packet, do not query anymore
		} else {
			alreadySeen.put(p.getId(), true);
			switch(getNodeType()) {
				case GOOD:
					// propagate correct message
					if(p.getPacketType().equals(PacketType.ATTACK)) {
						sendAttackPacket(p);
					} else if(p.getPacketType().equals(PacketType.RETREAT)) {
						sendRetreatPacket(p);
					}
					break;
				case BAD:
					// propagate bad message
					if(p.getPacketType().equals(PacketType.ATTACK)) {
						sendRetreatPacket(p);
					} else if(p.getPacketType().equals(PacketType.RETREAT)) {
						sendAttackPacket(p);
					}
					break;
				case DONTKNOW:
					// propagate random (good/bad) message
					if(Math.random() < 0.5) {
						sendRetreatPacket(p);
					} else {
						sendAttackPacket(p);
					}
					break;
			}
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
