/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.classloader.modules.interfaces;

import java.io.IOException;
import java.io.InputStream;

import vidis.sim.classloader.modules.impl.dir.FileModuleFile;
import vidis.sim.classloader.modules.impl.jar.JarModuleFile;

/**
 * This is a extension to the IModuleComponent interface and
 * adds functionality to retrieve data from a certain MSIM file.
 * This interface hides the source where this file is located. This
 * could possibly be a JAR archive, a directory or other locations.
 * 
 * @see JarModuleFile
 * @see FileModuleFile
 * 
 * @author Dominik
 *
 */
public interface IModuleFileComponent extends IModuleComponent {
	/**
	 * Returns a new InputStream pointing to the file.
	 * @return a new InputStream
	 * @throws IOException occurs if the InputStream creation fails
	 */
	public InputStream getInputStream() throws IOException;
}
