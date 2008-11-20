package vidis.sim.classloader;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.LinkedList;
import java.util.List;
import java.util.jar.JarFile;

import org.apache.log4j.Logger;

/**
 * the vidis class loader
 * 
 * basically this class is the concrete implementation of
 * our class loader to load module files, not only from jar
 * archives
 * @author Dominik
 *
 */
public class VidisClassLoader extends URLClassLoader {
	private static Logger logger = Logger.getLogger(VidisClassLoader.class);
	
	private static VidisClassLoader instance;
	
	private List<JarFile> loadedJars;

	private VidisClassLoader() {
//		super(urls, ClassLoader.getSystemClassLoader());
		super(new URL[] { });
		loadedJars = new LinkedList<JarFile>();
	}
	
	public static VidisClassLoader getInstance() {
		if(instance == null)
			instance = new VidisClassLoader();
		return instance;
	}
	
	public List<JarFile> getLoadedFiles() {
		return loadedJars;
	}
	
	public void addFile(JarFile p) throws MalformedURLException {
		String urlpath = "jar:" + new File(p.getName()).toURI().toURL() + "!/";
		if(loadedJars.contains(p)) {
			logger.debug("[ReLoad JAR] " + p);
		} else {
			logger.debug("[Load JAR] " + p);
			addURL(new URL(urlpath));
			loadedJars.add(p);
		}
	}
}
