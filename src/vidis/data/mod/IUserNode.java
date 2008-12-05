package vidis.data.mod;

import vidis.data.AUserNode;
import vidis.data.exceptions.ObstructInitCallException;
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
	 * Is the initializing function called by the API.
	 * <p><strong>DO NOT CALL THIS FUNCTION ON YOUR OWN!</strong></p>
	 * <p><strong>DO NOT ANNOTATE THIS METHOD</strong></p>
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimNodeCon simulatorComponent) throws ObstructInitCallException;

	/**
	 * Is called when a packet arrives.
	 * 
	 * <p>You'll have to implement this function.
	 * <p><strong>DO NO ANNOTATE THIS METHOD</strong></p>
	 * </p>
	 * @param packet the packet the user receives
	 */
	public void receive(IUserPacket packet);
	
	/**
	 * Is executed upon the first simulation step.
	 * <p>here you can put all possible initialize operations like:
	 * <ul>
	 * 		<li>send messages</li>
	 * 		<li>check variables from msim file</li>
	 * 		<li>...</li>
	 * </ul>
	 * </p>
	 */
	public void init();
}