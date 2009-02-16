/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.gui.menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

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
			if (var != null) {
 				if ( var.getVariableType().equals( MethodVariable.class ) && var.getDataType().equals( Void.TYPE ) ) {
					updateButtons();
				}
			}
		}
	}
	
	private Queue<String> varChanges = new LinkedList<String>();
	
	public void reactOnVarChanges() {
		synchronized (varChanges) {
			while ( !varChanges.isEmpty() ) {
				reactOnVarChange(varChanges.poll());
			}
		}
	}
	
	private IVariableChangeListener listener = new IVariableChangeListener() {
		public void variableAdded(String id) {
			varChanges.add(id);
		}
		public void variableChanged(String id) {
			varChanges.add(id);
		}
		public void variableRemoved(String id) {
			varChanges.add(id);
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
	
	Map<AVariable, MenuItem> map = new ConcurrentHashMap<AVariable, MenuItem>();
	
	private void putMap( AVariable v, MenuItem m ) {
		logger.info( "putting into map: var="+v+", menuitem="+m + " / " + Thread.currentThread().getName() );
		map.put( v, m );
	}
	
	private void addVar( final AVariable var ) {
		MenuItem tmp;
		MethodVariable v = (MethodVariable) var;
		Class<?>[] expectedParameter = v.getExpectedMethodParameterTypes();
		if ( expectedParameter.length > 0 ) {
			tmp = new MenuItem( this.root, var.getIdentifierWithoutNamespace(), null );
			final List<BasicGuiContainer> params = new LinkedList<BasicGuiContainer>();
			for(int i=0; i<expectedParameter.length; i++) {
				Class<?> param = expectedParameter[i];
				// now pick right menu thingy
				if (  IUserNode.class.isAssignableFrom( param ) ) {
					final NodeField nf = new NodeField();
					MenuItem wireframe = new MenuItem( tmp, nf );
					this.addChild( wireframe.content );
					wireframe.setMenu(this);
					logger.debug("add nodefield as param");
					params.add(nf);
				} else if ( String.class.isAssignableFrom( expectedParameter[0] ) ) {
					final TextField tf = new TextField();
					MenuItem wireframe = new MenuItem( tmp, tf );
		//			tmp.setExpanded( false );
					this.addChild( wireframe.content );
					wireframe.setMenu(this);
					logger.debug("add textfield as param");
					params.add(tf);
				}
				else {
					tmp = new MenuItem( this.root, "not supported=" + var.getIdentifierWithoutNamespace(), null );
				}
			}
			logger.debug("added params: " + params.size());
			// add confirm/execute button
			MenuItem enter = new MenuItem ( tmp, "execute",  new MenuAction() {
				public void execute(Menu menu, MenuItem item) {
					boolean execute = true;
					// get data from fields
					ArrayList<Object> data = new ArrayList<Object>();
					for(BasicGuiContainer c : params) {
						if(c instanceof NodeField) {
							// must be a node instance
							NodeField nf = (NodeField) c;
							try {
								IUserNode p = Simulator.getInstance().findUserNodeForId( nf.getNode().getId() );
								data.add(p);
							} catch (NotFoundException e) {
								logger.fatal(e);
								execute = false;
							} catch ( NullPointerException e ) {
								logger.info( "No parameter provided!" );
								execute = false;
							}
							
						} else if(c instanceof TextField) {
							// must be a text instance
							TextField tf = (TextField) c;
							data.add(tf.getText());
						} else {
							logger.fatal("cannot determine field instance!");
							execute = false;
						}
					}
					if(execute) {
						// now check each parameter for null arguments
						for(Object p : data) {
							if(p == null) {
								logger.warn("calling execute with a null argument!");
							}
						}
						if(execute) {
							// now execute it if passed
							logger.debug("execute function with params: " + data);
							Object result = var.getData( data.toArray() );
							logger.debug("executed function, result=" + result);
						}
					}
				}
			});
//			tmp.setExpanded( false );
			this.addChild( enter.content );
			enter.setMenu(this);
		} else {
			tmp = new MenuItem( this.root, var.getIdentifierWithoutNamespace(), new MenuAction() {
				public void execute(Menu menu, MenuItem item) {
					var.getData();
				}
			});
		}
		if ( tmp != null ) {
//			map.put(var, tmp);
			putMap(var, tmp);
		
			this.addChild( tmp.content );
			tmp.setMenu(this);
			logger.fatal( "addVar -> update"+ " / "   + Thread.currentThread().getName() );
			this.update();
		}
	}
	
	private void delVar( AVariable var ) {
		MenuItem m = map.get( var );
		if ( m == null ) {
			logger.error( "m was null! This means variable '"+var+"' has no MenuItem!!"+ " / "  + Thread.currentThread().getName());
		}
		else {
			removeMenuItem( m );
			this.root.removeChild( m );
			logger.fatal( "delVar -> update"+ " / "   + Thread.currentThread().getName() );
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
		try {	
			
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
//			for ( AVariable m : toDelete ) {
//				delVar( m );
//			}
			for ( AVariable m : newButtons ) {
				addVar( m );
			}
		}
		catch ( Exception e ) {
			logger.fatal( "MENU BUG?!", e );
		}
		finally {
			if ( toDelete != null ) {
				for ( AVariable m : toDelete ) {
					delVar( m );
				}
			}
		}
	}
	
	public void resetMenu() {
		List<AVariable> delete = new ArrayList<AVariable>( map.keySet() );
		
		for ( AVariable v : delete ) {
			delVar( v );
		}
	}
}
