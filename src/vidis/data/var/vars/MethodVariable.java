package vidis.data.var.vars;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import vidis.data.annotation.DisplayType;
import vidis.data.var.AVariable;

/**
 * a method variable; this variable uses reflection to retrieve the
 * value, just like its little brother FieldVariable does.
 * 
 * @author Dominik
 *
 */
public class MethodVariable extends AVariable {
	/**
	 * the constructor you should use
	 * @param id the identifier of this variable
	 * @param object the object to check onto
	 * @param method the method to check for
	 */
	public MethodVariable(String id, Object object, Method method) {
		super(id);
		this.object = object;
		this.method = method;
	}
	
	/**
	 * one of the constructors, but a deprecated one, use another
	 * if possible. :-)
	 * @param id the identifier of this variable
	 * @param type the display type of this variable
	 * @param object the object to check onto
	 * @param method the method to check for
	 */
	@Deprecated
	public MethodVariable(String id, DisplayType type, Object object, Method method) {
		super(id, type);
		this.object = object;
		this.method = method;
	}
    /*public MethodVariable(Object object, Method method) {
    	this.object = object;
    	this.method = method;
    }*/

    private Object object;
    private Method method;
    
    public Class<?> getType() {
    	return method.getReturnType();
    }
    
    public Object getData() {
    	try {
			return method.invoke(object);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }
    
    public Object getData(Object... args) {
    	try {
			return method.invoke(object, args);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
    }

    /**
     * this method retrieves if this method expects parameters
     * @return true or false
     */
    public boolean getMethodExpectsParameters() {
    	return getExpectedMethodParameterTypes().length > 0;
    }

    /**
     * retrieve the expected parameter class types of the method
     * @return a array of class types
     */
    public Class<?>[] getExpectedMethodParameterTypes() {
    	return method.getParameterTypes();
    }

	public Class<? extends AVariable> getVariableType() {
		return this.getClass();
	}

	public void update(Object data) {
		this.object = data;
	}
	public void update(Object obj, Method m) {
		this.method = m;
		this.object = obj;
	}
}
