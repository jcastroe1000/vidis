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
	}
	public void removeChild(TreeElement<T> child) {
		childs.remove(child);
	}
	public void removeChild(int i) {
		childs.remove(i);
	}
	public void refresh(int level) {
		// add myself
		String asdf = "";
		for(int i=0; i<level; i++) asdf += "  ";
		asdf += display;
		Label my = new Label( asdf ) {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				if(isExpanded()) {
					collapse();
				} else {
					expand();
				}
				root.clickedOn(TreeElement.this);
			}
		};
		my.setBounds(1, 1, 7, 18);
		root.getScrollPane().addChild( my );
		
		// show childs (add them to the scrollpane)
		if(expanded) {
			for(TreeElement<T> c : childs) {
				c.refresh(level+1);
			}
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
