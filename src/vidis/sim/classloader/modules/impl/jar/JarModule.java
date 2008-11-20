package vidis.sim.classloader.modules.impl.jar;

import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import vidis.sim.classloader.modules.impl.AModule;
import vidis.sim.classloader.modules.impl.AModuleFile;
import vidis.sim.classloader.modules.interfaces.IModuleComponent;

public class JarModule extends AModule implements IModuleComponent {
	private static Logger logger = Logger.getLogger(JarModule.class);
	
	private JarFile f;
	
	public JarModule(JarFile file) {
		f = file;
	}

	@Override
	public List<AModuleFile> getModuleFiles() {
		List<AModuleFile> moduleFiles = new LinkedList<AModuleFile>();
		
		Enumeration<JarEntry> es = f.entries();
		while(es.hasMoreElements()) {
			JarEntry e = es.nextElement();
			if(e.getName().endsWith(".msim")) {
				// assume valid msim file
				moduleFiles.add(new JarModuleFile(f, e));
			}
		}
		return moduleFiles;
	}

	@Override
	public String getName() {
		return f.getName();
	}
}
