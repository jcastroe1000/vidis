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
import vidis.sim.classloader.modules.interfaces.IModule;
import vidis.sim.classloader.modules.interfaces.IModuleFile;

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
	
	public List<IModuleFile> getModuleFiles() {
		List<IModuleFile> moduleFiles = new LinkedList<IModuleFile>();
		
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
	
	public List<IModule> getModules() {
		List<IModule> modules = new LinkedList<IModule>();
		File[] files = f.listFiles();
		if(files.length > 0) {
			for(int i=0; i<files.length; i++) {
				File file = files[i];
				if(file.exists()) {
					if(file.isDirectory()) {
						if(! file.getName().startsWith(".")) {
							IModule f = getImplementation(file);
							if(f != null) {
								modules.add(f);
							}
						}
					}
				}
			}
		}
		return modules;
	}
}
