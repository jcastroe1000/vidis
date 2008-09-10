package vidis.data.mod;

import vidis.sim.exceptions.ObstructInitCallException;
import vidis.data.sim.ISimPacketCon;

/**
 * 
 * @author dominik
 *
 */
public interface IUserPacket extends IUserComponent {

	/**
	 * initializing function called by the API
	 * 
	 * DO NOT CALL THIS FUNCTION ON YOUR OWN!
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimPacketCon simulatorComponent) throws ObstructInitCallException;

	public IUserNode getSource();
	
	public IUserLink getLinkToSource();
}