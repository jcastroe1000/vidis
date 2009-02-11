/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.var.vars;

import vidis.data.var.IVariableChangeListener;

/**
 * a default variable implementation
 * 
 * @author Dominik
 *
 */
public class DefaultVariable extends AVariable {
	
	/**
	 * the data of this variable
	 */
	private Object data;

	/**
	 * the public constructor of this variable
	 * @param id the id of this variable
	 * @param value the value associated with this variable
	 */
	public DefaultVariable(String id, Object value) {
		super(id);
		setData(value);
	}

//	/**
//	 * the public constructor of this variable
//	 * 
//	 * PLEASE NOTE: THIS CONSTRUCTOR IS DEPRECATED AND SHOULD NO LONGER
//	 * 				BE USED AT IT WILL BE REMOVED SOON!
//	 * @param id the id of this variable
//	 * @param type the display type of this variable (DEPRECATED)
//	 * @param value the value of this variable
//	 */
//	@Deprecated
//	public DefaultVariable(String id, DisplayType type, Object value) {
//		super(id, type);
//		this.setData(value);
//	}

	/**
	 * retrieve the class type of the data stored in this variable
	 * 
	 * @return class object
	 */
	public Class<?> getDataType() {
		return data.getClass();
	}

	/**
	 * retrieve the data stored in this variable
	 * 
	 * @return the data stored
	 */
	public Object getData() {
		return data;
	}

	/**
	 * set the data of this variable
	 * 
	 * @param data
	 *          the data
	 */
	private void setData(Object data) {
		this.data = data;
	}

	/**
	 * update variable data; SHOULD BE DONE ALWAYS IN FAVOR OF CREATING NEW
	 * VARIABLE!
	 * 
	 * @param data the new value
	 */
	public void update(Object data) {
		if (data != null && !data.equals(getData())) {
			setData(data);
			// changed
			synchronized (this.getVariableChangeListeners()) {
				for (IVariableChangeListener l : this.getVariableChangeListeners()) {
					// System.err.println(l.toString() + ".variableChanged(" +
					// getIdentifier() + ")");
					l.variableChanged(getIdentifier());
				}
			}
		}
	}

	/**
	 * a common to string method
	 */
	public String toString() {
		return "(" + getDataType().getName() + ")" + getIdentifier() + "=" + getData().toString();
	}

	@Override
	public Class<? extends AVariable> getVariableType() {
		return this.getClass();
	}
}
