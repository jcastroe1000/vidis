/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.gui.menu;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Button;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;


public class MenuItem {
	private static Logger logger = Logger.getLogger(MenuItem.class);

	private class MenuItemLayout implements ILayout {
		
		private double h = 1.5;
		
		public MenuItemLayout( double h ) {
//			this.h = h;
		}
		
		public MenuItemLayout() {
			this( 1.5 );
		}
		
		public double getHeight() {
			return h;
		}
		public double getWidth() {
			return MenuItem.this.menu.getWidth() - offset;
		}
		public double getX() {
			return offset;
		}
		public double getY() {
			double step = h + 0.2;
			return index * step - step;
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
	
	public MenuItem( MenuItem parent ) {
		// spacer
		this.content = new BasicGuiContainer();
		this.content.setOpaque( false );
		this.content.setLayout( new MenuItemLayout() );
		this.parent = parent;
		if ( parent != null ) {
			parent.addChild( this );
		}
		this.childs = new ArrayList<MenuItem>();
	}
	
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
	
	public MenuItem( MenuItem parent, BasicGuiContainer content, double h ) {
		this.content = content;
		this.content.setLayout( new MenuItemLayout( h ) );
		this.parent = parent;
		this.setText( text );
		this.childs = new ArrayList<MenuItem>();
		if ( parent != null ) {
			parent.addChild( this );
		}
	}
	
	public MenuItem( MenuItem parent, BasicGuiContainer content ) {
		this( parent, content, 1.5 );
	}
	
	protected void expandOrCollapse() {
		this.setExpanded( ! this.expanded );
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
		logger.fatal( "expand -> update" );
		this.menu.update();
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

	public void removeChild(MenuItem m) {
		this.childs.remove( m );
	}
	
	@Override
	public String toString() {
		return this.getText();
	}

}
