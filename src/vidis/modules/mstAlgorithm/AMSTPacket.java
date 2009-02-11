/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.mstAlgorithm;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

public abstract class AMSTPacket extends AUserPacket {
	protected enum Type {
		EXPLORE(ColorType.CYAN),
		ECHO(ColorType.BLUE),
		PING(ColorType.ORANGE),
		PONG(ColorType.RED),
		RESETCOLOR(ColorType.BLACK),
		MSTPACKET(ColorType.GREEN);
		private ColorType c;
		private Type(ColorType c) {
			this.c = c;
		}
		public ColorType getColor() {
			return c;
		}
	};
	
	private static Logger logger = Logger.getLogger(AMSTPacket.class);
	
	private int packetId;
	private Type packetType;
	private String whoSentThis;
	
	public AMSTPacket(Type t, int id, String senderId) {
		packetType = t;
		packetId = id;
		whoSentThis = senderId;
	}
	public AMSTPacket(AMSTPacket p) {
		this(p.packetType, p.packetId, p.whoSentThis);
	}
	
	public Type getType() {
		return packetType;
	}
	
	@DisplayColor
	public ColorType getColor() {
		return getType().getColor();
	}
	@Display(name="uid")
	public int getId() {
		return packetId;
	}
	public String getQueryierId() {
		return whoSentThis;
	}
	
	// ------------ random id generator ------------ //
	private static Set<Integer> randomIds = new HashSet<Integer>();
	private static int someRandomId() {
		return (int) (Math.random()*Integer.MAX_VALUE);
	}
	public static int generateRandomId() {
		int id;
		while((randomIds.contains(id = someRandomId())));
		logger.info("generated id: " + id);
		return id;
	}
}
