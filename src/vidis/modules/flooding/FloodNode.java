/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.flooding;

import vidis.data.AUserNode;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

@ComponentInfo(name = "FloodNode")
public class FloodNode extends AUserNode {
	
	@Display(name = "flood sent")
	public boolean floodSent = false;
	
	public void execute() {
		if (!floodSent) {
			for (IUserLink link : this.getConnectedLinks()) {
				send(new FloodPacket(this, 0), link, 1 + (long) (Math.random() * 2));
			}
			floodSent = true;
		} else {
			// no action
		}
	}
	
	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	private void receive(FloodPacket packet) {
		// check if the sender was me
		if (packet.getCreator().equals(this)) {
			// Logger.output(LogLevel.DEBUG, this, "rcv(OWN) => SINK");
		} else {
			// send on all links but from who the flood packet came a answer
			for (IUserLink link : this.getConnectedLinks()) {
				if (!packet.getLinkToSource().equals(link)) {
					// Logger.output(LogLevel.DEBUG, this, "rcv('" + packet.getCreator() +
					// "') => flood '" + link.getOtherNode(this) + "'!");
					send(new FloodPacket(packet), link, 1 + (long) (Math.random() * 2));
				} else {
					// Logger.output(LogLevel.DEBUG, this, "rcv('" + packet.getCreator() +
					// "') => no flood '" + link.getOtherNode(this) + "'!");
				}
			}
		}
	}

	public void receive(IUserPacket packet) {
		if (packet instanceof FloodPacket) {
			receive((FloodPacket) packet);
		} else {
			//Logger.output(LogLevel.ERROR, this, "receive 'unknown' packet from " + packet.getSource());
		}

	}
}
