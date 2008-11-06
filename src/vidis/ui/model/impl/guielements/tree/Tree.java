package vidis.ui.model.impl.guielements.tree;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.guielements.scrollpane.ScrollPane3D;
import vidis.ui.model.structure.AGuiContainer;

public abstract class Tree<T> extends TreeElement<T> {
	private static Logger logger = Logger.getLogger(Tree.class);
	public Tree(String name, T obj) {
		super(name, obj);
		root = this;
		scrollpane = new ScrollPane3D();
		setOpaque(false);
		expand();
	}
	private ScrollPane3D scrollpane;
	
	public TreeElement<T> createTreeElement(String name, T obj) {
		return new TreeElement<T>(this, name, obj);
	}
	public AGuiContainer getScrollPane() {
		return scrollpane;
	}
	public void refresh() {
		scrollpane.removeAllChilds();
		super.refresh(0);
	}
	protected abstract void clickedOn(TreeElement<T> element);
}
