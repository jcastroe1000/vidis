/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.mod;

import vidis.data.AUserPacket;
import vidis.data.exceptions.ObstructInitCallException;
import vidis.data.sim.ISimPacketCon;

/**
 * the interface for a packet in a user implemented module. just like
 * links and nodes you should NOT use this interface to implement
 * a new module, but use the abstract AUserPacket class instead as there
 * we implemented already a lot of basic and vital functionality
 * for the simulator!
 *  
 * @author dominik
 * @see AUserPacket
 */
public interface IUserPacket extends IUserComponent {

	/**
	 * Is called by the API during initialization.
	 * <p>
	 * <strong>DO NOT CALL THIS FUNCTION ON YOUR OWN!</strong>
	 * </p>
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimPacketCon simulatorComponent) throws ObstructInitCallException;

	/**
	 * Retrieves the source node of this packet.
	 * @return the module user node who sent this packet
	 */
	public IUserNode getSource();
	
	/**
	 * Retrieves the link to the source node of this packet.
	 * @return the module user link to the node who sent this packet
	 */
	public IUserLink getLinkToSource();
}