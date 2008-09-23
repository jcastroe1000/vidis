package vidis.data;

import java.util.List;

import vidis.data.mod.AUserComponent;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;
import vidis.data.sim.ISimNodeCon;
import vidis.data.var.AVariable;
import vidis.data.var.AVariable.COMMON_SCOPES;
import vidis.sim.exceptions.ObstructInitCallException;

/**
 * abstract user node represents a node by a user;
 * 
 * module writers should use this class to write their own node
 * @author dominik
 *
 */
public abstract class AUserNode extends AUserComponent implements IUserNode {

    protected ISimNodeCon simulatorComponent;

    public final void init(ISimNodeCon c) throws ObstructInitCallException {
		if (simulatorComponent != null)
		    throw new ObstructInitCallException();
		simulatorComponent = c;
    }

    /**
     * retrieve all the connected links to this node
     * @return list of links
     */
    protected final List<IUserLink> getConnectedLinks() {
    	return simulatorComponent.getConnectedLinks();
    }

    /**
     * send a packet over a specified link
     * @param p packet to send
     * @param l link to use for sending
     */
    protected final void send(IUserPacket p, IUserLink l) {
    	simulatorComponent.send(p, l, 0);
    }

    /**
     * send a packet over a specified link and waiting for some
     * amount of time to send which may interpreted as processing
     * time
     * @param p the packet to send
     * @param l the link to use for send
     * @param wait the amount of time to wait / process
     */
    protected final void send(IUserPacket p, IUserLink l, long wait) {
    	simulatorComponent.send(p, l, wait);
    }

    public String toString() {
    	return "Node#" + getId();
    }

    public final void interrupt() {
		try {
		    simulatorComponent.interrupt();
		} catch (NullPointerException e) {
		    // nothing
		}
    }

    public final void sleep(int steps) {
		try {
		    simulatorComponent.sleep(steps);
		} catch (NullPointerException e) {
		    // nothing
		}
    }
    
    protected String getId() {
    	return simulatorComponent.getId();
    }

    public final AVariable getVariable(String identifier) {
    	return simulatorComponent.getScopedVariable(COMMON_SCOPES.USER, identifier);
    }

    public final boolean hasVariable(String identifier) {
    	return simulatorComponent.hasScopedVariable(COMMON_SCOPES.USER, identifier);
    }
}
