package vidis.sim.classloader.modules.impl.jar;

import java.io.IOException;
import java.io.InputStream;
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
