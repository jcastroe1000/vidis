package vidis.ui.model.impl.guielements.variableDisplays;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableChangeListener;
import vidis.data.var.IVariableContainer;
import vidis.data.var.vars.DefaultVariable;
import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.guielements.scrollpane.AScrollpane3D;

public class CompositeScrollPane extends AScrollpane3D implements IVariableChangeListener {
	private static Logger logger = Logger.getLogger(CompositeScrollPane.class);
	
	private IVariableContainer vcontainer;
	
	private Map<String, BasicGuiContainer> mapVariableContainer;
	
	public CompositeScrollPane ( IVariableContainer vcontainer ) {
		mapVariableContainer = new HashMap<String, BasicGuiContainer>();
		
		this.vcontainer = vcontainer;
		
		vcontainer.addVariableChangeListener( this );
	}

	private BasicGuiContainer createNewDisplay( String id ) {
		AVariable var = this.vcontainer.getVariableById( id );
		if ( var.getVariableType().equals( FieldVariable.class ) ) {
			return new StringDisplay( (FieldVariable)var );
		}
		else if (var.getVariableType().equals( DefaultVariable.class )) {
			return new StringDisplay( (DefaultVariable) var );
		}
		else if ( var.getVariableType().equals( MethodVariable.class ) ) {
			return new StringDisplay( (MethodVariable) var );
		}
		return new StringDisplay( new DefaultVariable("fuck", "you") );
	}
	
	public void variableAdded(String id) {
		if( mapVariableContainer.containsKey(id) ) {
			// do nothing dude
		} else {
			BasicGuiContainer varContainer = createNewDisplay( id );
			mapVariableContainer.put( id, varContainer );
			this.addChild( varContainer );
		}
	}

	public void variableChanged(String id) {
		variableAdded(id);
		
	}

	public void variableRemoved(String id) {
		if( mapVariableContainer.containsKey(id) ) {
			this.removeChild( mapVariableContainer.remove( id ) );
		}
	}
}
