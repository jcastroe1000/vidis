package vidis.data.sim;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.ComponentInfo;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayType;
import vidis.data.mod.IUserComponent;
import vidis.data.var.AVariable;
import vidis.data.var.IVariableChangeListener;
import vidis.data.var.AVariable.COMMON_SCOPES;
import vidis.data.var.vars.DefaultVariable;
import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;

public abstract class AComponent implements IComponent, IAComponentCon, IVariableChangeListener {

    // -- IVariableContainer fields -- //
    private List<IVariableChangeListener> variableChangeListeners;
    private Map<String, AVariable> vars;

    private int sleep = -1;

    public AComponent() {
		// vars instanzieren
		init();
    }

    private void init() {
		if (this.vars == null)
		    this.vars = new ConcurrentHashMap<String, AVariable>();
		if (this.variableChangeListeners == null)
		    this.variableChangeListeners = new ArrayList<IVariableChangeListener>();
    }

    public void kill() {
		this.vars.clear();
		this.variableChangeListeners.clear();
    }

    private void initVarsMethods() {
		for (Method m : getUserLogic().getClass().getMethods()) {
		    initVarsMethods(m);
		}
    }

    protected abstract IUserComponent getUserLogic();

    private void initVarsMethods(Method m) {
		for (Annotation a : m.getAnnotations()) {
		    if (a.annotationType().equals(Display.class)) {
				// add variable for this method
				Display t = (Display) a;
				String id = COMMON_SCOPES.USER + "." + t.name();
				if (hasVariable(id)) {
				    // only update
				    ((MethodVariable)getVariableById(id)).update(getUserLogic(), m);
				} else {
				    registerVariable(new MethodVariable(id, getUserLogic(), m));
				}
		    } else {
		    	//Logger.output(LogLevel.WARN, this, "unknown method-annotation "
				//+ a + " encountered!");
		    }
		}
    }

    private void initVarsClass() {
		if (getUserLogic().getClass().getAnnotations().length > 0) {
		    for (Annotation a : getUserLogic().getClass().getAnnotations()) {
				if (a.annotationType().equals(ComponentColor.class)) {
				    ComponentColor aa = (ComponentColor) a;
				    String id = AVariable.COMMON_IDENTIFIERS.COLOR;
				    if (hasVariable(id)) {
				    	((DefaultVariable)getVariableById(id)).update(aa.color());
				    } else {
				    	registerVariable(new DefaultVariable(id, DisplayType.SHOW_SWING, aa.color()));
				    }
				} else if (a.annotationType().equals(ComponentInfo.class)) {
				    ComponentInfo aa = (ComponentInfo) a;
				    String id = COMMON_SCOPES.USER + ".name";
				    if (hasVariable(id)) {
				    	((DefaultVariable)getVariableById(id)).update(aa.name());
				    } else {
				    	registerVariable(new DefaultVariable(id, DisplayType.SHOW_SWING, aa.name()));
				    }
				} else {
				    //Logger.output(LogLevel.WARN, this,
					//	  "unknown class-annotation " + a
					//		  + " encountered!");
				}
		    }
		}
    }

    private void initVarsFields() {
		for (Field f : getUserLogic().getClass().getDeclaredFields()) {
		    for (Annotation a : f.getAnnotations()) {
				if (a.annotationType().equals(Display.class)) {
				    Display d = (Display) a;
				    String ns = "";
				    if (Modifier.isPublic(f.getModifiers())) {
				    	ns = COMMON_SCOPES.USER + ".";
				    } else {
				    	ns = COMMON_SCOPES.SYSTEM + ".";
				    }
				    String id = ns + d.name();
				    if (hasVariable(id)) {
						// only update
						((FieldVariable)getVariableById(id)).update(getUserLogic(), f);
				    } else {
				    	registerVariable(new FieldVariable(id, d.type(), getUserLogic(), f));
				    }
				} else {
//				    Logger.output(LogLevel.WARN, this,
//						  "unknown field-annotation " + a
//							  + " encountered!");
				}
		    }
		}
    }

    protected void initVars() {
		// vars initialisieren
    	initVarsClass();
		initVarsMethods();
		initVarsFields();
    }

    public final void registerVariable(AVariable var) {
		String id = var.getIdentifier();
		AVariable old = vars.put(id, var);
		if (old == null) {
		    // new variable
		    var.addVariableChangeListener(this);
		    variableAdded(id);
		} else {
		    old.removeVariableChangeListener(this);
		    variableChanged(id);
		}
	
		// if (hasVariable(var.getIdentifier())) {
		// getVariableById(Object.class, var.getIdentifier()).update(var.getData());
		// } else {
		// Variable<?> old = vars.put(var.getIdentifier(), var);
		// if (old != null) {
		// if (!old.equals(var)) {
		// Logger.output(LogLevel.WARN, this, "setVar() ==> obj changed ref-id");
		// // changed ref!
		// synchronized (this.variableChangeListeners) {
		// for (IVariableChangeListener l : this.variableChangeListeners) {
		// l.variableChanged(var.getIdentifier());
		// }
		// }
		// }
		// } else {
		// // added
		// synchronized (this.variableChangeListeners) {
		// for (IVariableChangeListener l : this.variableChangeListeners) {
		// l.variableAdded(var.getIdentifier());
		// }
		// }
		// }
		// }
    }

    public final boolean hasVariable(String identifier) {
    	return this.vars.containsKey(identifier);
    }

    public String toString() {
		if (hasVariable(AVariable.COMMON_IDENTIFIERS.ID)) {
			AVariable varA = getVariableById(AVariable.COMMON_IDENTIFIERS.ID);
			if(varA instanceof DefaultVariable) {
				return ((DefaultVariable)varA).getData().toString();
			} else if(varA instanceof FieldVariable) {
				return ((FieldVariable)varA).getData().toString();
			} else if(varA instanceof MethodVariable) {
				return ((MethodVariable)varA).getData().toString();
			}
		}
		return getUserLogic().toString();
    }

    public AVariable getVariableById(String id) throws ClassCastException {
    	return this.vars.get(id);
    }

    public final AVariable getScopedVariable(String scope, String identifier) {
    	return getVariableById(scope + "." + identifier);
    }

    public final boolean hasScopedVariable(String scope, String identifier) {
    	return hasVariable(scope + "." + identifier);
    }
    
    public Set<String> getScopedVariableIdentifiers(String scope) {
    	Set<String> ids = getVariableIds();
    	Set<String> ssids = new HashSet<String>();
    	for(String id : ids) {
    		if(id.startsWith(scope))
    			ssids.add(id);
    	}
    	return ssids;
    }

    public Set<String> getVariableIds() {
    	return this.vars.keySet();
    }

    public void addVariableChangeListener(IVariableChangeListener l) {
		synchronized (variableChangeListeners) {
		    if (!variableChangeListeners.contains(l)) {
		    	variableChangeListeners.add(l);
		    }
		}
    }

    public void removeVariableChangeListener(IVariableChangeListener l) {
		variableChangeListeners.remove(l);
    }

    public void variableAdded(String id) {
		// System.out.println("AComponent.variableAdded()");
		synchronized (variableChangeListeners) {
		    for (IVariableChangeListener l : variableChangeListeners)
			l.variableAdded(id);
		}
    }

    public void variableChanged(String id) {
		// System.out.println("AComponent.variableChanged()");
		synchronized (variableChangeListeners) {
		    for (IVariableChangeListener l : variableChangeListeners)
			l.variableChanged(id);
		}
    }

    public void variableRemoved(String id) {
		// System.out.println("AComponent.variableRemoved()");
		synchronized (variableChangeListeners) {
		    for (IVariableChangeListener l : variableChangeListeners)
			l.variableRemoved(id);
		}
    }
    
    public List<IVariableChangeListener> getVariableChangeListeners() {
    	return variableChangeListeners;
    }

    public void execute() {
		// variableChanged call for every variable that changed since last step
		// at the moment it is called for every variable, since it would cost more
		// time and memory to evaluate which variables state changed
		synchronized (vars) {
			for (String varId : vars.keySet()) {
				variableChanged(varId);
			}
		}
		// Logger.output(LogLevel.DEBUG, this, "vars[" + vars.size() + "],
		// varsListener["+variableChangeListeners.size()+"]");
		if (isSleeping())
		    sleep--;
    }

    public void checkVariablesChanged() {
    	initVarsFields();
    }

    public void sleep(int steps) {
    	sleep = steps;
    }

    public void interrupt() {
    	sleep = -1;
    }

    public boolean isSleeping() {
    	return sleep >= 0;
    }
}
