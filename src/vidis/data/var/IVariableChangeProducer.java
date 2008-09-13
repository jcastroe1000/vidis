package vidis.data.var;

import java.util.List;

/**
 * This interface defines all methods used for handling our variable system
 * 
 * @author Christoph
 * @see IVariableChangeListener
 */
public interface IVariableChangeProducer {
	/**
	 * add a new variable change listener
	 * @param l the variable change listener to add
	 */
	public void addVariableChangeListener(IVariableChangeListener l);

	/**
	 * remove a variable change listener
	 * @param l the variable change listener to remove
	 */
	public void removeVariableChangeListener(IVariableChangeListener l);
	
	/**
	 * get all variable change listeners
	 * @return a list of variable change listeners
	 */
	public List<IVariableChangeListener> getVariableChangeListeners();
}