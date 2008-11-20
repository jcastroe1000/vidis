package vidis.sim.classloader.modules.impl.dir;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.sim.classloader.modules.impl.AModule;
import vidis.sim.classloader.modules.impl.AModuleFile;

public class DirectoryModule extends AModule {
	private static Logger logger = Logger.getLogger(DirectoryModule.class);
	
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
