package vidis.data.sim;

import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;

/**
 * this interface defines the functions, that a user module
 * packet should implement
 * 
 * @author Dominik
 * @see IUserNode
 * @see IUserLink
 */
public interface ISimPacketCon extends IAComponentCon {

	/**
	 * retrieve who sent this packet
	 * @return the user node of source
	 */
	public IUserNode getFrom();
	
	/**
	 * retrieve to who this packet is destined
	 * @return the user node of destination
	 */
	public IUserNode getTo();

	/**
	 * retrieve the user link this packet is onto
	 * @return the user link this packet currently "surfs" on
	 */
	public IUserLink getLink();

}
