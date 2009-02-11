/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.tree;

import vidis.ui.model.impl.guielements.scrollpane.ScrollPane3D;
import vidis.ui.model.structure.AGuiContainer;

public abstract class Tree<T> extends TreeElement<T> {
//	private static Logger logger = Logger.getLogger(Tree.class);
	public Tree(String name, T obj) {
		super(name, obj);
		root = this;
		scrollpane = new ScrollPane3D();
		setOpaque(false);
		expand();
		refresh(); // initial refresh
	}
	private ScrollPane3D scrollpane;
	
	public TreeElement<T> createTreeElement(String name, T obj) {
		TreeElement<T> tmp = new TreeElement<T>(this, name, obj);
		refresh();
		return tmp;
	}
	public AGuiContainer getScrollPane() {
		return scrollpane;
	}
	public void refresh() {
		// TODO XXX this is the strange kack-ab bug
//		scrollpane.removeAllChilds();
		super.refresh(0);
	}
	protected abstract void clickedOn(TreeElement<T> element);
}
