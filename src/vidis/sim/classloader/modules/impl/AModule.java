/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.classloader.modules.impl;

import java.io.File;
import java.io.IOException;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import vidis.sim.classloader.modules.impl.dir.DirectoryModule;
import vidis.sim.classloader.modules.impl.jar.JarModule;
import vidis.sim.classloader.modules.interfaces.IModule;

public abstract class AModule implements IModule {
	private static Logger logger = Logger.getLogger(AModule.class);
	
	public abstract String getName();
	
	protected IModule getImplementation(File f) {
		if(f.isDirectory()) {
			return new DirectoryModule(f);
		} else {
			if(f.getName().toLowerCase().endsWith(".jar")) {
				try {
					JarFile jf = new JarFile(f);
					return new JarModule( jf );
				} catch (IOException e) {
					logger.error(e);
				}
			}
			return null;
		}
	}
	protected IModule getImplementation(JarFile f) {
		return new JarModule(f);
	}
}
