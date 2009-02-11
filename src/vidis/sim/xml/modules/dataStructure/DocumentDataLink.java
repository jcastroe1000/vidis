/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.xml.modules.dataStructure;

import java.util.HashMap;

public class DocumentDataLink {
	private String id;
	private String classpath;
	private HashMap<String, String> variables;
	private long delay;
	private DocumentDataLink() {
		variables = new HashMap<String, String>();
	}
	public static DocumentDataLink getInstance() {
		return new DocumentDataLink();
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getClasspath() {
		return classpath;
	}
	public void setClasspath(String classpath) {
		this.classpath = classpath;
	}
	public void addVariable(String varName, String varValue) {
		getVariables().put(varName, varValue);
	}
	public HashMap<String, String> getVariables() {
		return variables;
	}
	public void setDelay(long delay) {
		this.delay = delay;
	}
	public long getDelay() {
		return delay;
	}
}