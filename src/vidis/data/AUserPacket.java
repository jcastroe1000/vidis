package vidis.data;

import java.util.Set;

import vidis.data.mod.AUserComponent;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;
import vidis.data.sim.ISimPacketCon;
import vidis.data.var.AVariable;
import vidis.data.var.AVariable.COMMON_SCOPES;
import vidis.sim.exceptions.ObstructInitCallException;

/**
 * abstract user packet represents a packet by a user;
 * 
 * module writers should use this class to write their own packet
 * 
 * @author dominik
 * 
 */
public abstract class AUserPacket extends AUserComponent implements IUserPacket {

    protected ISimPacketCon simulatorComponent;

    public void init(ISimPacketCon simulatorComponent) throws ObstructInitCallException {
		if (this.simulatorComponent != null) {
	//	    Logger
	//		    .output(LogLevel.ERROR, this, "init(" + simulatorComponent
	//			    + "), but already registered at "
	//			    + this.simulatorComponent);
		    throw new ObstructInitCallException();
		}
		this.simulatorComponent = simulatorComponent;
    }

    public final IUserNode getSource() {
    	return simulatorComponent.getFrom();
    }

    public IUserLink getLinkToSource() {
    	return simulatorComponent.getLink();
    }

    public final void execute() {
    	// no action at packets :-)
    }

//    public String toString() {
//    	if(simulatorComponent != null)
//    		return simulatorComponent.toString();
//    	return super.toString();
//    }

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
    	if(simulatorComponent != null)
    		return simulatorComponent.getScopedVariable(COMMON_SCOPES.USER, identifier);
    	return null;
    }

    public final boolean hasVariable(String identifier) {
    	if(simulatorComponent != null)
    		return simulatorComponent.hasScopedVariable(COMMON_SCOPES.USER, identifier);
    	return false;
    }
    
    public final Set<String> getVariableIdentifiers() {
    	if(simulatorComponent != null)
    		return simulatorComponent.getScopedVariableIdentifiers(COMMON_SCOPES.USER);
    	return null;
    }
}
