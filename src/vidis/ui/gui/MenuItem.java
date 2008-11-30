package vidis.ui.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Button;
import vidis.ui.model.impl.CheckBox;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;


public class MenuItem {
	private static Logger logger = Logger.getLogger(MenuItem.class);

	private class MenuItemLayout implements ILayout {
		public double getHeight() {
			return 1.5;
		}
		public double getWidth() {
			return MenuItem.this.menu.getWidth() - offset;
		}
		public double getX() {
			return offset;
		}
		public double getY() {
			return index * 1.7;
		}
		public void layout() {
		}
		public void setGuiContainer(IGuiContainer c) {
		}
	}
	private Menu menu;
	private boolean expanded = true;
	private MenuAction menuAction;
	private double offset = 0;
	private int index;
	private String text;
	private MenuItem parent;
	private List<MenuItem> childs;
	
	BasicGuiContainer content;
	
	public MenuItem( MenuItem parent, String text, MenuAction x ) {
		this.menuAction = x;
		this.content = new Button() {
			@Override
			public void onClick() {
				logger.fatal( "MENUITEM WAS CLICKED");
				if ( MenuItem.this.menuAction != null ) {
					MenuItem.this.menuAction.execute( MenuItem.this.menu, MenuItem.this );
				}
				else {
					expandOrCollapse();
				}
			}
		};
		//this.content = new CheckBox();
		
		this.content.setLayout( new MenuItemLayout() );
		((Button)this.content).setText( text );
		//((CheckBox)this.content).setText( text );
		this.parent = parent;
		this.setText( text );
		this.childs = new ArrayList<MenuItem>();
		if ( parent != null ) {
			parent.addChild( this );
		}
	}
	
	protected void expandOrCollapse() {
		this.expanded = !this.expanded;
		this.menu.update();
	}

	public void addChild( MenuItem child ) {
		this.childs.add( child );
	}

	public MenuItem getParent() {
		return parent;
	}

	public List<MenuItem> getChilds() {
		return childs;
	}
	
	public void setIndex( int index ) {
		this.index = index;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}
	
	public double getOffset() {
		return offset;
	}

	public void setOffset(double offset) {
		this.offset = offset;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public Menu getMenu() {
		return menu;
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}


}
