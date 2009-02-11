/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.var;

import java.util.Set;

import vidis.data.var.vars.AVariable;


/**
 * This interface defines all methods used for handling our variable system
 * 
 * @author Dominik
 * 
 */
public interface IVariableContainer extends IVariableChangeProducer {
	/**
	 * get a variable usind its unique string identifier
	 * @param id the id to search for
	 * @return the variable associated with id or null if it does not exist
	 */
	public AVariable getVariableById(String id);

	/**
	 * get all variable ids defined in this variable container
	 * @return a set of variable unique string identifiers
	 */
	public Set<String> getVariableIds();

	/**
	 * register a new variable
	 * @param var the variable to register
	 */
	public void registerVariable(AVariable var);

	/**
	 * check if this variable container has defined a variable with
	 * the requested id
	 * @param id check for
	 * @return true or false
	 */
	public boolean hasVariable(String id);
}