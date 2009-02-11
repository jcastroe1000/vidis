/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.classloader.modules.impl.dir;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import vidis.sim.classloader.modules.impl.AModuleFile;

/**
 * a file module file; a local file msim
 * @author Dominik
 *
 */
public class FileModuleFile extends AModuleFile {
	private File f;
	public FileModuleFile(File file) {
		f = file;
	}
	
	@Override
	public String getName() {
		return f.getName();
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return new FileInputStream(f);
	}
	
	@Override
	public String toString() {
		return f.getName();
	}

}
