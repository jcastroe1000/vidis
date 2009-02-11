/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.sim;

import java.util.List;

import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;

/**
 * this class provides all functions of a node that a module writer
 * may need.
 * @author dominik
 * 
 */
public interface ISimNodeCon extends IAComponentCon {

	/**
	 * retrieve all links that this node is connected to
	 * @return a list of links
	 */
	public List<IUserLink> getConnectedLinks();
	
	/**
	 * send a packet over a link
	 * @param p the packet to send
	 * @param link the link which is used to send
	 */
	public void send(IUserPacket p, IUserLink link);
	
	/**
	 * send a packet over a link; the packet send is delayed for some
	 * amount of time which may be interpreted as the processing time
	 * @param p the packet to send
	 * @param link the link which is used to send
	 * @param wait the amount of time to wait
	 */
	public void send(IUserPacket p, IUserLink link, long wait);

	/**
	 * retrieve unique identifier for this node
	 * @return string
	 */
	public String getId();

	/**
	 * Connects this node to another node.
	 * @param n the node to connect to
	 * @param lclazz the link class to use
	 * @param delay the delay of the link class
	 */
	public void connect(IUserNode n, Class<? extends IUserLink> lclazz, long delay);
	
	/**
	 * Spawns a new node.
	 */
	public IUserNode spawnNewNode();
}
