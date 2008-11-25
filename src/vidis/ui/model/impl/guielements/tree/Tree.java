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
