package vidis.data.var.vars;

import java.lang.reflect.Field;

import vidis.data.annotation.DisplayType;
import vidis.data.var.AVariable;

public class FieldVariable extends AVariable {
	public FieldVariable(String id, DisplayType type, Object obj, Field field) {
		super(id, type);
		this.object = obj;
		this.field = field;
	}

	private Object object;
	private Field field;
	private Object last_value;
	private boolean changed = false;

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
			// tmp = field.invoke(object, arguments);
			return invoke().toString();
		} catch (IllegalArgumentException e) {
			//Logger.output(LogLevel.ERROR, this, e);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public Class<? extends AVariable> getVariableType() {
		return this.getClass();
	}
	
	public void update(Object obj, Field field) {
		this.object = obj;
		this.field = field;
	}
}
