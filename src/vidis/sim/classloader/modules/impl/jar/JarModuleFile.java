package vidis.sim.classloader.modules.impl.jar;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map.Entry;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import vidis.sim.classloader.modules.impl.AModuleFile;

/**
 * a jar module file; thus meaning a module file
 * contained within a jar file
 * @author Dominik
 *
 */
public class JarModuleFile extends AModuleFile {
	private JarFile f;
	private JarEntry e;
	
	public JarModuleFile(JarFile f, JarEntry e) {
		this.f = f;
		this.e = e;
	}
	@Override
	public String getName() {
		try {
			for(Entry<String,Attributes> es : f.getManifest().getEntries().entrySet()) {
//				System.err.println(es.getKey() + ":"+ es.getValue().size() + ": ");
				if(es.getKey().equals(this.e.getName())) {
//					 got module information
					for(Entry<Object, Object> e : es.getValue().entrySet()) {
						if(e.getKey().equals(new Attributes.Name("MSIM-Name"))) {
							return e.getValue().toString() + " (" + this.e.getName() + ")";
						}
					}
				}
			}
		} catch (IOException e1) {
		}
		return e.getName();
	}
	@Override
	public InputStream getInputStream() throws IOException {
		return f.getInputStream(e);
	}
	
	@Override
	public String toString() {
		return f.getName() + " : " + e.getName();
	}
}
