/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.gui.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import vidis.data.mod.IUserNode;
import vidis.data.var.IVariableChangeListener;
import vidis.data.var.IVariableContainer;
import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.sim.Simulator;
import vidis.sim.exceptions.NotFoundException;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.NodeField;
import vidis.ui.model.impl.TextField;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class Menu extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Menu.class);

	private MenuItem root;
	private int index;
	private ASimObject selectedObject;
	
	public Menu( MenuItem root ) {
		this.root = root;
		this.setOpaque( false );
		addAll( root );
		this.setLayout( new ILayout() {
			public double getHeight() {
				return 1.5 + index * 1.7;
			}
			public double getWidth() {
				return 15;
			}
			public double getX() {
				return 1;
			}
			public double getY() {
				return 1;
			}
			public void layout() {
			}
			public void setGuiContainer(IGuiContainer c) {
			}
		});
	}
	
	private void addAll( MenuItem root ) {
		root.setMenu( this );
//		this.addChild( root.content );
		for ( MenuItem child : root.getChilds() ) {
			this.addChild( child.content );
			addAll( child );
		}
		
	}
	
	public void update() {
		logger.debug( "updating menu");
		index = update( root, 0, 0 );
	}
	
	/**
	 * walks through the menu tree and assigns every visible element an index
	 * @param x
	 * @param index
	 * @param offset
	 * @return
	 */
	private int update( MenuItem x, int index, double offset  ) {
//		logger.fatal( "update( " + x.getText() + ", " + index + ", " + offset + " )" );
//		logger.fatal( x.getChilds().size()==0?"no children":(x.isExpanded()?"visible":"invisible") + " children: " + x.getChilds() );
		x.setIndex( index );
		x.setOffset( offset );
		if ( x.isExpanded() ) {
			for ( MenuItem child : x.getChilds() ) {
				index ++;
				index = update( child, index, offset + 0.3 );
			}
		}
		else {
			for ( MenuItem child : x.getChilds() ) {
				hide( child );
			}
		}
		return index;
	}
	
	private void hide( MenuItem x ) {
		x.setIndex( Integer.MAX_VALUE );
		for ( MenuItem child : x.getChilds() ) {
			hide( child );
		}
	}

	private void reactOnVarChange( String id ) {
		if ( selectedObject != null ) {
			AVariable var = selectedObject.getVariableContainer().getVariableById(id);
			if ( var.getVariableType().equals( MethodVariable.class ) && var.getDataType().equals( Void.TYPE ) ) {
				updateButtons();
			}
		}
	}
	
	private IVariableChangeListener listener = new IVariableChangeListener() {
		public void variableAdded(String id) {
			reactOnVarChange(id);
		}
		public void variableChanged(String id) {
			reactOnVarChange(id);
		}
		public void variableRemoved(String id) {
			reactOnVarChange(id);
		}
		
	};
	
	public void setSelection(ASimObject object) {
		if ( selectedObject != null ) {
			selectedObject.getVariableContainer().removeVariableChangeListener( listener );
		}
		selectedObject = object;
		selectedObject.getVariableContainer().addVariableChangeListener( listener );
		
		updateButtons();
	}
	
	Map<AVariable, MenuItem> map = new HashMap<AVariable, MenuItem>();
	
	private void addVar( final AVariable var ) {
		MenuItem tmp;
		MethodVariable v = (MethodVariable) var;
		Class[] expectedParameter = v.getExpectedMethodParameterTypes();
		if ( expectedParameter.length == 1 ) {
			if (  IUserNode.class.isAssignableFrom( expectedParameter[0] ) ) {
				tmp = new MenuItem( this.root, var.getIdentifier(), null );
				final NodeField nf = new NodeField();
				MenuItem wireframe = new MenuItem( tmp, nf );
				MenuItem enter = new MenuItem ( tmp, "execute",  new MenuAction() {
					public void execute(Menu menu, MenuItem item) {
						try {
							var.getData( Simulator.getInstance().findUserNodeForId( nf.getNode().getId() ) );
						} catch (NotFoundException e) {
							logger.fatal(e);
						}
					}
				});
	//			tmp.setExpanded( false );
				this.addChild( wireframe.content );
				this.addChild( enter.content );
				wireframe.setMenu(this);
				enter.setMenu(this);
			}
			else if ( String.class.isAssignableFrom( expectedParameter[0] ) ) {
				tmp = new MenuItem( this.root, var.getIdentifier(), null );
				final TextField tf = new TextField();
				MenuItem wireframe = new MenuItem( tmp, tf );
				MenuItem enter = new MenuItem ( tmp, "execute",  new MenuAction() {
					public void execute(Menu menu, MenuItem item) {
						var.getData( tf.getText() );
					}
				});
	//			tmp.setExpanded( false );
				this.addChild( wireframe.content );
				this.addChild( enter.content );
				wireframe.setMenu(this);
				enter.setMenu(this);
			}
			else {
				tmp = new MenuItem( this.root, "not supported=" + var.getIdentifier(), null );
			}
		}
		else if ( expectedParameter.length > 0 ) {
			tmp = new MenuItem( this.root, "not supported=" + var.getIdentifier(), null );
		}
		else {
			tmp = new MenuItem( this.root, var.getIdentifier(), new MenuAction() {
				public void execute(Menu menu, MenuItem item) {
					var.getData();
				}
			});
		}
		map.put(var, tmp);
		
		this.addChild( tmp.content );
		tmp.setMenu(this);
		logger.fatal( "addVar -> update" );
		this.update();
	}
	
	private void delVar( AVariable var ) {
		MenuItem m = map.get( var );
		if ( m == null ) {
			logger.error( "m was null! This means variable '"+var+"' has no MenuItem!!" );
		}
		else {
			removeMenuItem( m );
			this.root.removeChild( m );
			logger.fatal( "delVar -> update" );
			this.update();
			map.remove( var );
		}
	}
	
	private void removeMenuItem( MenuItem m ) {
		this.removeChild( m.content );
		for ( MenuItem c : m.getChilds() ) {
			removeMenuItem( c );
		}
	}
	
	
	
	private void updateButtons() {
		List<AVariable> oldButtons = new ArrayList<AVariable>( map.keySet() );
		List<AVariable> newButtons = new ArrayList<AVariable>();
		IVariableContainer c = selectedObject.getVariableContainer();
		for ( String id : c.getVariableIds() ) {
			final AVariable var = c.getVariableById( id );
			if ( var.getVariableType().equals( MethodVariable.class ) && var.getDataType().equals( Void.TYPE ) ) {
//				logger.error( "-> " + var.getIdentifier() );
				newButtons.add( var );
			}
		}
		
		
		List<AVariable> toDelete = new ArrayList<AVariable>();
		for ( AVariable m : oldButtons ) {
			if ( newButtons.contains( m ) ) {
				// ok - remove it form new ( the rest in new is really new )
				newButtons.remove( m );
			}
			else {
				// need to delete this one
				toDelete.add( m );
			}
		}
		// delete toDelete & add newButtons
		for ( AVariable m : toDelete ) {
			delVar( m );
		}
		for ( AVariable m : newButtons ) {
			addVar( m );
		}
		
		
		
		
	}
}
