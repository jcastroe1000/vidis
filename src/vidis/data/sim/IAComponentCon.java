package vidis.data.sim;

import java.util.Set;

import vidis.data.var.AVariable;

/**
 * interface for all abstract user components
 * @author dpsenner
 *
 */
public interface IAComponentCon {
	/**
	 * retrieve a scoped variable
	 * @param scope the scope
	 * @param identifier the identifier
	 * @return the variable
	 */
    public AVariable getScopedVariable(String scope, String identifier);

    /**
     * retrieve if a scoped variable is set
     * @param scope the scope to check in
     * @param identifier the identifier to check for
     * @return boolean true or false
     */
    public boolean hasScopedVariable(String scope, String identifier);
    
    public Set<String> getScopedVariableIdentifiers(String scope);

    /**
     * causes the simulator component to wake up from sleep
     */
    public void interrupt();

    /**
     * causes the simulator component to sleep for a certain amount of steps
     * @param steps sleep for these steps
     */
    public void sleep(int steps);
}
