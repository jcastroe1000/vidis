package vidis.data.mod;

import vidis.sim.exceptions.ObstructInitCallException;
import vidis.data.AUserNode;
import vidis.data.sim.ISimNodeCon;

/**
 * the interface for a module node; please keep in mind that
 * you should use the abstract class AUserNode to implement
 * new modules. there we implemented basic functionality in order
 * to let the simulator work flawlessly together with the module.
 * 
 * @author Dominik
 * @see AUserNode
 *
 */
public interface IUserNode extends IUserComponent {

	/**
	 * initializing function called by the API
	 * 
	 * DO NOT CALL THIS FUNCTION ON YOUR OWN!
	 * 
	 * DO NO ANNOTATE THIS METHOD
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimNodeCon simulatorComponent) throws ObstructInitCallException;

	/**
	 * receive a packet
	 * 
	 * DO NO ANNOTATE THIS METHOD
	 * @param packet the packet the user receives
	 */
	public void receive(IUserPacket packet);
	
	/**
	 * this function will be executed upon the first simulation step
	 * 
	 * here you can put all possible initialize operations like:
	 * 		* send messages
	 * 		* check variables from msim file
	 * 		* ...
	 */
	public void init();
}