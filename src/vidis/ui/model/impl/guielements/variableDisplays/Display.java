/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.variableDisplays;

import vidis.data.var.vars.AVariable;
import vidis.ui.model.impl.Label;

public abstract class Display extends Label {

	final static String prefix = "   ";
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