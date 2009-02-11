/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.sim;

import java.util.List;

import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;

/**
 * this class provides all necessary functions for the user
 * to implement a nice module
 * @author dominik
 *
 */
public interface ISimLinkCon extends IAComponentCon {

	/**
	 * retrieve the node A connected at one side of the link
	 * @return the Node connected at one side
	 */
	public IUserNode getNodeA();
	
	/**
	 * retrieve the node B connected at the other side of the link
	 * @return the Node connected at the other side
	 */
	public IUserNode getNodeB();
	
	/**
	 * get delay of this link
	 * @return long
	 */
	public long getDelay();

	/**
	 * retrieve all packets on this link
	 * @return a list of packets
	 */
	public List<IUserPacket> getPacketsOnLink();

	/**
	 * drops a packet the link
	 * @param packet a packet specified
	 */
	public void dropPacketOnLink(IUserPacket packet);
	
	/**
	 * drops all packets on the link
	 */
	public void dropPacketsOnLink();

	/**
	 * retrieve the id of this thingy
	 * @return the string unique identifier
	 */
	public String getId();

	/**
	 * Disconnects this link.
	 */
	public void disconnect();
}
