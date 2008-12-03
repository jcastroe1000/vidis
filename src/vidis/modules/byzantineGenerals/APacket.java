package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.DisplayColor;

public abstract class APacket extends AUserPacket {
	private static Logger logger = Logger.getLogger(APacket.class);
	
	private double id;
	
	public APacket() {
		id = Math.random() * Double.MAX_VALUE;
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
	
	public double getId() {
		return id;
	}
	
	protected abstract PacketType getPacketType();
}
