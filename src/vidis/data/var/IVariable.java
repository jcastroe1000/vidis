package vidis.data.var;

import vidis.data.var.vars.MethodVariable;

public interface IVariable extends IVariableChangeProducer {
	/**
	 * retrieves the identifier of this variable.
	 * @return string identifier
	 */
	public String getIdentifier();
	/**
	 * retrieves the namespace or scope of this variable. This
	 * could be one of: system, user
	 * @see #getNamespace(String)
	 * @return the namespace
	 */
	public String getNameSpace();
	/**
	 * updates the variable content
	 * @param data the new object
	 */
	public void update(Object data);
	/**
	 * retrieves the data type class contained within this variable.
	 * <p>
	 * This method should be used whenever type checking is required
	 * instead of getData().getClass()!
	 * </p>  
	 * @return the class object of the contained data
	 */
	public abstract Class<?> getDataType();
	/**
	 * retrieves the data contained within this variable.
	 * @return the data object to get
	 */
	public abstract Object getData();
	
	/**
	 * retrieves the data contained within this variable if
	 * the variable data depends on parameters.
	 * @see MethodVariable#getData(Object...)
	 * @param args the arguments needed
	 * @return the data object to get
	 */
	public abstract Object getData(Object... args);
}
