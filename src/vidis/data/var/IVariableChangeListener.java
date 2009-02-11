/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.var;


/**
 * the Variable Change Listener Interface
 * @author Christoph
 *
 */
public interface IVariableChangeListener {
	/**
	 * this function is the trigger that tells the variable
	 * change listener when a variable possibly changed its value
	 * @param id the unique string identifier of the variable that has changed
	 */
	public void variableChanged(String id);
	
	/**
	 * this function informs the variable change listener
	 * about a new variable
	 * @param id the unique string identifier of the variable that was added
	 */
	public void variableAdded(String id);
	
	/**
	 * this function informs the variable change listener
	 * about the removal of a variable
	 * @param id the unique string identifier of the variable that was removed
	 */
	public void variableRemoved(String id);
	
}
