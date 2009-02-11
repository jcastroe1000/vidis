/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.xml.modules.dataStructure;

public class DocumentDataConnection {

	public static DocumentDataConnection getInstance() {
		return new DocumentDataConnection();
	}

	private DocumentDataNode a;
	private DocumentDataNode b;
	private DocumentDataLink link;

	public void setNodeA(DocumentDataNode from) {
		this.a = from;
	}

	public void setNodeB(DocumentDataNode to) {
		this.b = to;
	}

	public void setLink(DocumentDataLink link) {
		this.link = link;
	}

	public DocumentDataNode getNodeA() {
		return a;
	}
	
	public DocumentDataNode getNodeB() {
		return b;
	}
	
	public DocumentDataLink getLink() {
		return link;
	}

}
