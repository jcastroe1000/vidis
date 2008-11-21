package vidis.ui.model.impl.guielements.tree;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Label;

public class TreeElement<T> extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(TreeElement.class);
	
	protected List<TreeElement<T>> childs;
	protected String display;
	protected T obj;
	protected Label my;
	
	private boolean expanded;
	
	protected Tree<T> root;
	
	public TreeElement(String name, T obj) {
		expanded = false;
		this.obj = obj;
		this.display = name;
		childs = new LinkedList<TreeElement<T>>();
	}
	
	public TreeElement(Tree<T> root, String name, T obj) {
		this(name, obj);
		this.root = root;
	}

	public T getObject() {
		return obj;
	}
	public void addChild(TreeElement<T> child) {
		childs.add(child);
		root.refresh();
	}
	public void removeChild(TreeElement<T> child) {
		childs.remove(child);
		child.my.setVisible(false);
		root.refresh();
	}
	public void removeChild(int i) {
		childs.remove(i).setVisible(false);
		root.refresh();
	}
	public void refresh(int level) {
		if(my == null) {
			// add myself
			String asdf = "";
			asdf += display;
			my = new Label( asdf ) {
				@Override
				protected void onMouseClicked(MouseClickedEvent e) {
					if(isExpanded()) {
						collapse();
					} else {
						expand();
					}
					root.clickedOn(TreeElement.this);
					root.refresh();
				}
			};
			my.setBounds(1, 1, 7, 18);
		}
		if(my != null) {
			if(! root.getScrollPane().getChilds().contains(this)) {
				root.getScrollPane().addChild( my );
			} else {
				System.err.println("already in");
			}
			String asdf = "";
			for(int i=0; i<level; i++) asdf += "  ";
			asdf += display;
			my.setText(asdf);
			if(level > 0) {
				for(TreeElement<T> c : childs) {
					if(c.my != null)
						c.my.setVisible(expanded);
				}
			} else {
				my.setVisible(true);
			}
		}
		// show childs (add them to the scrollpane)
		for(TreeElement<T> c : childs) {
			c.refresh(level+1);
		}
	}

	public boolean isExpanded() {
		return expanded;
	}
	public void expand() {
		expanded = true;
	}
	public void collapse() {
		expanded = false;
	}
}
