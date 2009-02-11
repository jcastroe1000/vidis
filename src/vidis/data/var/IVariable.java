/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
