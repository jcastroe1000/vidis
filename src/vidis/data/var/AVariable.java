package vidis.data.var;

import java.util.ArrayList;
import java.util.List;

import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;

public abstract class AVariable implements IVariableChangeProducer {
	public static final class COMMON_SCOPES {
		public static final String USER = "user";
		public static final String SYSTEM = "system";
	}
	public static final class COMMON_IDENTIFIERS {
		public static final String ID = COMMON_SCOPES.SYSTEM + ".id";
		public static final String COLOR = COMMON_SCOPES.SYSTEM + ".color";
		public static final String POSITION = COMMON_SCOPES.SYSTEM + ".position";
		public static final String PACKETSSENT = COMMON_SCOPES.SYSTEM + ".packetsSent";
		public static final String PACKETSRECEIVED = COMMON_SCOPES.SYSTEM + ".packetsReceived";
		public static final String NAME = COMMON_SCOPES.USER + ".name";
	}
	private String identifier;
//	private DisplayType displayType = DisplayType.SHOW_SWING;
	private List<IVariableChangeListener> variableChangeListeners;
	private AVariable() {
		this.variableChangeListeners = new ArrayList<IVariableChangeListener>();
	}
	public AVariable(String id) {
		this();
		setIdentifier(id);
	}
	
	/**
	 * retrieves the data contained within this variable.
	 * @return the data object to get
	 */
	public abstract Object getData();
	
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
	 * retrieve the identifier of this variable
	 * 
	 * @return string identifier
	 */
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * set the identifier for this class
	 * 
	 * @param identifier
	 *          identifier
	 */
	private void setIdentifier(String identifier) {
		this.identifier = identifier;
	}
	
	public void removeVariableChangeListener(IVariableChangeListener l) {
		variableChangeListeners.remove(l);
	}

	public void addVariableChangeListener(IVariableChangeListener l) {
		if (!variableChangeListeners.contains(l)) {
			variableChangeListeners.add(l);
		}
	}
	
	public List<IVariableChangeListener> getVariableChangeListeners() {
		return variableChangeListeners;
	}
	
	/**
	 * retrieves the variable class type of this variable.
	 * @see DefaultVariable
	 * @see FieldVariable
	 * @see MethodVariable
	 * @return the variable class type
	 */
	public abstract Class<? extends AVariable> getVariableType();

	/**
	 * retrieves the namespace or scope of this variable. This
	 * could be one of: system, user
	 * @see #getNamespace(String)
	 * @return the namespace
	 */
	public String getNameSpace() {
		return getNamespace(this.getIdentifier());
	}
	
	/**
	 * retrieves the identifier without the initial namespace.
	 * @return the identifier without namespace
	 */
	public String getIdentifierWithoutNamespace() {
		return getIdentifierWithoutNamespace(this.getIdentifier());
	}
	
	/**
	 * updates the variable content
	 * @param data the new object
	 */
	public abstract void update(Object data);
	
	/**
	 * retrieves the namespace or scope of this variable. This could
	 * be one of: system, user. This method is a generic static
	 * version used within {@link #getNameSpace()}.
	 * @param id the id to check
	 * @return the namespace of the parameter id
	 */
	public static String getNamespace( String id ) {
		String ns = "";
		int occ = id.indexOf('.');
		if (occ >= 0) {
			ns = id.substring(0, occ);
		}
		return ns;
	}
	
	/**
	 * retrieves the identifier without the initial namespace.
	 *  This method is a generic static
	 * version used within {@link #getIdentifierWithoutNamespace()}.
	 * @param id the identifier to trim the namespace from
	 * @return the identifier without namespace
	 */
	public static String getIdentifierWithoutNamespace( String id ) {
		return id.replaceFirst(getNamespace(id) + ".", "");
	}
}
