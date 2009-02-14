/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.classloader.modules.interfaces;

import java.util.List;


/**
 * this interface basically does nothing but is used
 * within the gui
 * @author Dominik
 *
 */
public interface IModule extends IModuleComponent {
	/**
	 * retrieves the name of this module.
	 * This name can possibly be the filename or a identifier,
	 * description or both out of some meta information contained
	 * within JAR archives or similar.
	 * @return the string name
	 */
	String getName();
	
	/**
	 * retrieves the childs of this module or empty list if there are none.
	 * @return the childs of this module.
	 */
	List<IModule> getModules();
	
	/**
	 * retrieves the module files within this module.
	 * @return the module file childs of this module.
	 */
	List<IModuleFile> getModuleFiles();
}
