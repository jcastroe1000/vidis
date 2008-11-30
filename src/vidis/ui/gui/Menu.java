package vidis.ui.gui;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class Menu extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Menu.class);

	private MenuItem root;
	private int index;
	
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
		this.addChild( root.content );
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
}
