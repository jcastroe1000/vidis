package vidis.data.mod;

import vidis.data.AUserLink;
import vidis.data.exceptions.ObstructInitCallException;
import vidis.data.sim.ISimLinkCon;

/**
 * this interface defines a basic functionality for all user
 * module links; anyway for implementation of a new link in a
 * ne module, the class AUserLink should be used instead.
 * 
 * we implemented there the basic functionality so that simulator
 * and module can flawlessly work together
 * 
 * @author Dominik
 * @see AUserLink
 */
public interface IUserLink extends IUserComponent {

	/**
	 * This one is the initializing function called by the API.
	 * <p><strong>DO NOT CALL THIS FUNCTION ON YOUR OWN!</strong></p>
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimLinkCon simulatorComponent) throws ObstructInitCallException;

	/**
	 * Retrieve the node connected to this link.
	 * @param me simply tell who you are (usually IUserNode.this)
	 * @return the user node at the other end; if you are not on this link you get NULL
	 */
	public IUserNode getOtherNode(IUserNode me);
	
	/**
	 * Retrieve delay of this link.
	 * @return a long representing the delay in simulation steps
	 */
	public long getDelay();

	/**
	 * Disconnects both nodes attached to this link and destroys this link.
	 */
	public void disconnect();
}