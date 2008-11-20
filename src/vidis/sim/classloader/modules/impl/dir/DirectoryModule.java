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
