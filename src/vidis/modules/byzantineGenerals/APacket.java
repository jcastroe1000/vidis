package vidis.modules.byzantineGenerals;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.DisplayColor;

public abstract class APacket extends AUserPacket {
	private int id;
	
	public APacket(int id) {
		this.id = id;
	}
	
	protected enum PacketType {
		ATTACK(ColorType.GREEN),
		RETREAT(ColorType.RED);
		
		private ColorType color;
		private PacketType( ColorType c ) {
			this.color = c;
		}
		public ColorType getColor() {
			return color;
		}
	};
	
	@DisplayColor
	public ColorType getColor() {
		return getPacketType().getColor();
	}
	
	public int getId() {
		return id;
	}
	
	protected abstract PacketType getPacketType();
}
