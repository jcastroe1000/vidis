package vidis.data.var;


/**
 * the Variable Change Listener Interface
 * @author Christoph
 *
 */
public interface IVariableChangeListener {
	/**
	 * this function is the trigger that tells the variable
	 * change listener when a variable possibly changed its value
	 * @param id the unique string identifier of the variable that has changed
	 */
	public void variableChanged(String id);
	
	/**
	 * this function informs the variable change listener
	 * about a new variable
	 * @param id the unique string identifier of the variable that was added
	 */
	public void variableAdded(String id);
	
	/**
	 * this function informs the variable change listener
	 * about the removal of a variable
	 * @param id the unique string identifier of the variable that was removed
	 */
	public void variableRemoved(String id);
	
}
