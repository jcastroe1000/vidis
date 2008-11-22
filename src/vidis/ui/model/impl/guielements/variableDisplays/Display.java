package vidis.ui.model.impl.guielements.variableDisplays;

import vidis.data.var.AVariable;
import vidis.ui.model.impl.Label;

public abstract class Display extends Label {

	protected AVariable var;
	
	public Display( AVariable var ) {
		this.var = var;
		
	}
	
	public AVariable getVariable() {
		return var;
	}

//	public abstract Display newInstance( AVariable var );
//	public abstract Display newInstance( Object data );
}