package vidis.ui.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import vidis.data.var.IVariableChangeListener;
import vidis.data.var.IVariableContainer;
import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.ui.model.impl.BasicGuiContainer;
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
		index = update( root, 0, 0 );
	}
	
	private int update( MenuItem x, int index, double offset  ) {
		x.setIndex( index );
		x.setOffset( offset );
		logger.fatal( "---> "+x.getText() + "_" + index);
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
		MenuItem tmp = new MenuItem( this.root, var.getIdentifier(), new MenuAction() {
			public void execute(Menu menu, MenuItem item) {
				var.getData();
			}
		});
		map.put(var, tmp);
		this.addChild( tmp.content );
		tmp.setMenu(this);
		this.update();
	}
	
	private void delVar( AVariable var ) {
		MenuItem m = map.get( var );
		if ( m == null ) {
			logger.error( "m was null! This means variable '"+var+"' has no MenuItem!!" );
		}
		else {
			this.removeChild( m.content );
			this.root.removeChild( m );
			this.update();
			map.remove( var );
		}
	}
	
	
	
	public void updateButtons() {
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
