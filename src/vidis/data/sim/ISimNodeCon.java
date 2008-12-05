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
