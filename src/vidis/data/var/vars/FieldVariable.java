package vidis.data.var.vars;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;


/**
 * a field variable; this class uses reflection to get the
 * contents of a field variable in a certain object.
 * 
 * @author Dominik
 *
 */
public class FieldVariable extends AVariable {
	public FieldVariable(String id, Object obj, Field field) {
		super(id);
		this.object = obj;
		this.field = field;
	}
//	/**
//	 * the constructor of a field variable
//	 * @param id the id of this variable
//	 * @param type the display type of this variable
//	 * @param obj the object who has the field
//	 * @param field the field to check for
//	 */
//	public FieldVariable(String id, DisplayType type, Object obj, Field field) {
//		super(id, type);
//		this.object = obj;
//		this.field = field;
//	}

	private Object object;
	private Field field;
	private Object last_value;
	private boolean changed = false;
	private static Logger logger = Logger.getLogger(FieldVariable.class);

	private Object invoke() throws IllegalArgumentException, IllegalAccessException {
		Object tmp = field.get(object);
		if (tmp.equals(last_value)) {
		} else {
			changed = true;
			last_value = tmp;
		}
		return tmp;
	}

	public String toString() {
		try {
			return invoke().toString();
		} catch (IllegalArgumentException e) {
			return e.getMessage();
		} catch (IllegalAccessException e) {
			return "field is not public!";
		}
	}

	public boolean checkChanged() {
		if (changed) {
			changed = false;
			return true;
		}
		return false;
	}
	
	public Object getData() {
		try {
			return invoke();
		} catch (IllegalArgumentException e) {
			logger .error(e);
		} catch (IllegalAccessException e) {
			logger.error(e);
		}
		return null;
	}
	
	public Class<?> getDataType() {
		return field.getType();
	}

	public Class<? extends AVariable> getVariableType() {
		return this.getClass();
	}
	
	public void update(Object data) {
		this.object = data;
	}
	
	public void update(Object obj, Field field) {
		this.object = obj;
		this.field = field;
	}
}
