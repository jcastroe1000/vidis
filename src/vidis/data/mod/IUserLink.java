package vidis.data.mod;

import vidis.sim.exceptions.ObstructInitCallException;
import vidis.data.AUserLink;
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
	 * initializing function called by the API
	 * 
	 * DO NOT CALL THIS FUNCTION ON YOUR OWN!
	 * @param simulatorComponent the simulator component that represents this object in the API
	 * @throws ObstructInitCallException thrown if you call this function
	 */
	public void init(ISimLinkCon simulatorComponent) throws ObstructInitCallException;

	/**
	 * retrieve the node connected to this link
	 * @param me simply tell who you are
	 * @return the user node at the other end; if you are not on this link you get NULL
	 */
	public IUserNode getOtherNode(IUserNode me);
	
	/**
	 * retrieve delay of this link
	 * @return long
	 */
	public long getDelay();
}