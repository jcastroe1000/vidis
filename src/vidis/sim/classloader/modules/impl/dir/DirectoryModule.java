/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.classloader.modules.impl.dir;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import vidis.sim.classloader.modules.impl.AModule;
import vidis.sim.classloader.modules.impl.AModuleFile;

/**
 * this kind of module can be used to load
 * local msim files; their classes MUST be already
 * within the class-path in order to work
 * @author Dominik
 *
 */
public class DirectoryModule extends AModule {
	private File f;
	public DirectoryModule(File file) {
		f = file;
	}

	@Override
	public List<AModuleFile> getModuleFiles() {
		List<AModuleFile> moduleFiles = new LinkedList<AModuleFile>();
		
		File moduleFolder = f;
		if(moduleFolder.exists()) {
			if(moduleFolder.isDirectory()) {
				if(! moduleFolder.getName().startsWith(".")) {
					for(File moduleFile : moduleFolder.listFiles()) {
						if(moduleFile.isFile() && moduleFile.canRead() && moduleFile.getName().toLowerCase().endsWith(".msim") ) {
							moduleFiles.add(new FileModuleFile(moduleFile));
						}
					}
				}
			}
		}
		
		return moduleFiles;
	}

	@Override
	public String getName() {
		return f.getName();
	}

}
