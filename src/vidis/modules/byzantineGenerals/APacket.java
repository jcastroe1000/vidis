/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
