/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.sim;

import java.util.Set;

import vidis.data.var.vars.AVariable;

/**
 * interface for all abstract user components
 * @author Dominik
 *
 */
public interface IAComponentCon {
	/**
	 * retrieve a scoped variable
	 * @param scope the scope
	 * @param identifier the identifier
	 * @return the variable
	 */
    public AVariable getScopedVariable(String scope, String identifier);

    /**
     * retrieve if a scoped variable is set
     * @param scope the scope to check in
     * @param identifier the identifier to check for
     * @return boolean true or false
     */
    public boolean hasScopedVariable(String scope, String identifier);
    
    /**
     * retrieve all variable identifiers within a scope
     * @param scope the scope to check in
     * @return a set of unique variable identifiers
     */
    public Set<String> getScopedVariableIdentifiers(String scope);

    /**
     * causes the simulator component to wake up from sleep
     */
    public void interrupt();

    /**
     * causes the simulator component to sleep for a certain amount of steps
     * @param steps sleep for these steps
     */
    public void sleep(int steps);
}
