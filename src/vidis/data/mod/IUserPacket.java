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