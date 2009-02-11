/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
