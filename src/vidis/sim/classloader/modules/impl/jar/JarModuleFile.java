package vidis.sim.classloader.modules.impl.jar;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

import vidis.sim.classloader.modules.impl.AModuleFile;

public class JarModuleFile extends AModuleFile {
	private static Logger logger = Logger.getLogger(JarModuleFile.class);
	
	private JarFile f;
	private JarEntry e;
	
	private File ee;

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
