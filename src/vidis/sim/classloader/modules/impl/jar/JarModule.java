package vidis.sim.classloader.modules.impl.jar;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vidis.sim.classloader.modules.impl.AModule;
import vidis.sim.classloader.modules.impl.AModuleFile;
import vidis.sim.classloader.modules.interfaces.IModuleComponent;

/**
 * a jar msim module; can also be used to load a remote
 * jar file
 * @author Dominik
 *
 */
public class JarModule extends AModule implements IModuleComponent {
	private JarFile f;
	private File ff;
	
	public JarModule(URL url) throws IOException, URISyntaxException {
		this(url.toURI());
	}
	public JarModule(URI uri) throws IOException, URISyntaxException {
		this(new JarFile(new File(uri)));
	}
	public JarModule(JarFile file) {
		f = file;
		ff = new File(f.getName());
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
		// test if we may find a module name in the manifest
		try {
			// check mainlevel
			for(Entry<Object,Object> e : f.getManifest().getMainAttributes().entrySet()) {
				if(e.getKey().equals(new Attributes.Name("Module-Name"))) {
					return e.getValue().toString() + " (" + ff.getName() + ")";
				}
			}
			// check attributes
			for(Entry<String,Attributes> es : f.getManifest().getEntries().entrySet()) {
//				System.err.println(es.getKey() + ":"+ es.getValue().size() + ": ");
				if(es.getKey().equals("Module")) {
					// got module information
					for(Entry<Object, Object> e : es.getValue().entrySet()) {
						if(e.getKey().equals(new Attributes.Name("Module-Name"))) {
							return e.getValue().toString() + " (" + ff.getName() + ")";
						}
					}
				}
			}
//			Attributes mod = f.getManifest().getEntries().get("Module");
//			if(mod != null) {
//				if(mod.containsKey("Module-Name")) {
//					return mod.getValue("Module-Name");
//				}
//			} else {
//				System.err.println("module not found");
//			}
		} catch (IOException e) {
		}
		return ff.getName();
	}
}
