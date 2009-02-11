/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.mstAlgorithm;

import vidis.data.AUserLink;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserPacket;

/**
 * This is a standard implementation. It provides everything a link
 * must can to connect nodes and deliver packets.
 * @author Dominik
 *
 */
@ComponentInfo(name = "Default Link")
public class DefaultLink extends AUserLink {
	private ColorType c = ColorType.BLACK;
	
	@DisplayColor
	public ColorType getColor() {
		return c;
	}
	public void execute() {
		for(IUserPacket p : getPacketsOnLink()) {
			if(p instanceof PingPacket) {
				// accept color of ping
				c = ((PingPacket)p).getColor();
			} else if(p instanceof PongPacket) {
				// accept color of pong
				c = ((PongPacket)p).getColor();
			} else if(p instanceof MstPacket) {
				c = ((MstPacket)p).getColor();
			} else {
				c = ColorType.BLACK;
			}
		}
	}
}
