/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.var;

import java.util.List;

/**
 * This interface defines all methods used for handling our variable system
 * 
 * @author Christoph
 * @see IVariableChangeListener
 */
public interface IVariableChangeProducer {
	/**
	 * add a new variable change listener
	 * @param l the variable change listener to add
	 */
	public void addVariableChangeListener(IVariableChangeListener l);

	/**
	 * remove a variable change listener
	 * @param l the variable change listener to remove
	 */
	public void removeVariableChangeListener(IVariableChangeListener l);
	
	/**
	 * get all variable change listeners
	 * @return a list of variable change listeners
	 */
	public List<IVariableChangeListener> getVariableChangeListeners();
}