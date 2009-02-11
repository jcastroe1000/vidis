/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.flooding;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.Display;

@ComponentColor(color = ColorType.GREY_LIGHT)
public class FloodPacket extends AUserPacket {
	@Display(name = "sent by")
	public FloodNode whoSentThis;
	@Display(name = "hop count")
	public int hopCount;

	public FloodPacket(FloodNode whoSentThis, int hopCount) {
		this.whoSentThis = whoSentThis;
		this.hopCount = hopCount;
	}

	public FloodPacket(FloodPacket packet) {
		this(packet.whoSentThis, packet.hopCount + 1);
	}

	@Display(name = "name")
	public String getName() {
		return "flood(" + getCreator().getVariable("name").getData().toString() + ")";
	}

	public String toString() {
		return getName();
	}

	public FloodNode getCreator() {
		return this.whoSentThis;
	}
}