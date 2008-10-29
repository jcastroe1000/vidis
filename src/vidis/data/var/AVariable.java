package vidis.data.var;

import java.util.ArrayList;
import java.util.List;

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
	
//	@Deprecated
//	public AVariable(String id, DisplayType type) {
//		this(id);
////		setDisplayType(type);
//	}
	
	public abstract Object getData();
	
//	@Deprecated
//	private void setDisplayType(DisplayType type) {
//		this.displayType = type;
//	}
//	
//	@Deprecated
//	public DisplayType getDisplayType() {
//		return displayType;
//	}
	
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
	
	public abstract Class<? extends AVariable> getVariableType();
	

	public String getNameSpace() {
		String ns = "";
		int occ = getIdentifier().indexOf('.');
		if (occ >= 0) {
			ns = getIdentifier().substring(0, occ);
		}
		return ns;
	}
	
	/**
	 * updates the variable content
	 * @param data the new object
	 */
	public abstract void update(Object data);
	
	
	public static String getNamespace( String id ) {
		String ns = "";
		int occ = id.indexOf('.');
		if (occ >= 0) {
			ns = id.substring(0, occ);
		}
		return ns;
	}
}
