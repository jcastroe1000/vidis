/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
