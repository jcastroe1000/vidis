package vidis.sim.classloader.modules.interfaces;

import java.io.IOException;
import java.io.InputStream;

import vidis.sim.classloader.modules.impl.dir.FileModuleFile;
import vidis.sim.classloader.modules.impl.jar.JarModuleFile;

/**
 * This is a extension to the IModuleComponent interface and
 * adds functionality to retrieve data from a certain MSIM file.
 * This interface hides the source where this file is located. This
 * could possibly be a JAR archive, a directory or other locations.
 * 
 * @see JarModuleFile
 * @see FileModuleFile
 * 
 * @author Dominik
 *
 */
public interface IModuleFileComponent extends IModuleComponent {
	/**
	 * Returns a new InputStream pointing to the file.
	 * @return a new InputStream
	 * @throws IOException occurs if the InputStream creation fails
	 */
	public InputStream getInputStream() throws IOException;
}
