package vidis.data.mod;

import vidis.data.var.AVariable;

/**
 * basic module component interface that all user implemented modules must implement
 * @author Dominik
 *
 */
public interface IUserComponent {
    /**
     * This function can be used to retrieve a user-scope set variable from
     * the Simulator.
     * 
     * If you specified a variable in the .msim configuration file you will
     * retrieve it here as a String
     * @param <T> generic return type
     * @param clazz the expected return class-type
     * @param identifier the identifier of the requested variable
     * @throws NullPointerException thrown if not variable found
     * @throws ClassCastException thrown if expected type (clazz) does not match the variable class type
     * @return null or object
     */
    public AVariable getVariable(String identifier)
	    throws NullPointerException, ClassCastException;

    /**
     * checks if a variable exists in the user-scope
     * @param identifier the identifier of the variable
     * @return true or false
     */
    public boolean hasVariable(String identifier);

    /**
     * this function is executed on each simulation step
     * 
     * NOTE: it is impossible to tell which of the components executes first!
     * NOTE2: do NOT use Thread.sleep() or something like that - it will only hang the simulator
     * NOTE3: use built-in sleep() and interrupt() functions!
     */
    public void execute();

    /**
     * let this user component sleep for some time
     * @param steps simulator steps to sleep
     */
    public void sleep(int steps);

    /**
     * interrupt a sleep of this component
     * 
     * if component does not sleep, no action is made
     */
    public void interrupt();
    
}
