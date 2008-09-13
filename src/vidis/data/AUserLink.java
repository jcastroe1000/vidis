package vidis.data;

import vidis.sim.exceptions.ObstructInitCallException;
import vidis.data.mod.AUserComponent;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.sim.ISimLinkCon;
import vidis.data.var.AVariable;
import vidis.data.var.AVariable.COMMON_SCOPES;

/**
 * abstract user link represents a link by a user;
 * 
 * module writers should use this class to write their own link
 * @author dominik
 *
 */
public abstract class AUserLink extends AUserComponent implements IUserLink {
    //private ISimLinkCon simulatorComponent;
    protected ISimLinkCon simulatorComponent;

    public final void init(ISimLinkCon c) throws ObstructInitCallException {
		if (simulatorComponent != null)
		    throw new ObstructInitCallException();
		this.simulatorComponent = c;
    }

    public final IUserNode getOtherNode(IUserNode me) {
		if (getNodeA().equals(me)) {
		    return getNodeB();
		} else if(getNodeB().equals(me)) {
		    return getNodeA();
		} else {
			return null;
		}
    }

    /**
     * retrieve node A connected to this link
     * @return the node connected 
     */
    private final IUserNode getNodeA() {
    	return simulatorComponent.getNodeA();
    }

    /**
     * retrieve node B connected to this link
     * @return the node connected
     */
    private final IUserNode getNodeB() {
    	return simulatorComponent.getNodeB();
    }

    public final long getDelay() {
    	return simulatorComponent.getDelay();
    }

    public String toString() {
    	return "Link#" + hashCode();
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

    public final AVariable getVariable(String identifier) {
    	return simulatorComponent.getScopedVariable(COMMON_SCOPES.USER, identifier);
    }

    public final boolean hasVariable(String identifier) {
    	return simulatorComponent.hasScopedVariable(COMMON_SCOPES.USER, identifier);
    }
}
