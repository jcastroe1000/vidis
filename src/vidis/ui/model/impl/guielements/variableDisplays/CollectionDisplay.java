/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;
import java.util.Collection;
import java.util.Iterator;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.ui.model.impl.Label;
import vidis.ui.model.impl.guielements.scrollpane.AScrollpane3D;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class CollectionDisplay extends Display {
	private static Logger logger = Logger.getLogger(CollectionDisplay.class);

	private Label label = new Label();
	private AScrollpane3D list = new AScrollpane3D() {
		
	};
	
	public CollectionDisplay ( AVariable v ) {
		super(v);
		this.setText( "" );
		//this.setHeight( 5 );
		this.addChild( label );
		this.addChild( list );
		label.setLayout(new ILayout() {
			public double getHeight() {
				return 1.5;
			}
			public double getWidth() {
				return CollectionDisplay.this.getWidth();
			}
			public double getX() {
				return 0;
			}
			public double getY() {
				return 4.5;
			}
			public void layout() {
			}
			public void setGuiContainer(IGuiContainer c) {
			}
		});
		label.setColor1( Color.red );
		label.setOpaque( true );
		list.setColor1( Color.blue );
		list.setOpaque( false );
		list.setLayout(new ILayout() {
			public double getHeight() {
				return 4.5;
			}
			public double getWidth() {
				return CollectionDisplay.this.getWidth();
			}
			public double getX() {
				return 0;
			}
			public double getY() {
				return 0;
			}
			public void layout() {
			}
			public void setGuiContainer(IGuiContainer c) {
			}
		});
	}
	
	@Override
	public double getWantedHeight() {
		return 6;
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new CollectionDisplay( var );
//	}
	
	@Override
	public void renderContainer(GL gl) {
		label.setText( var.getIdentifier().replaceAll(var.getNameSpace()+".", "   ") + ":" );
		
		Collection c = (Collection) var.getData();
		Iterator it = c.iterator();
		list.removeAllChilds();
		int i = 0;
		while ( it.hasNext() ) { 
//			logger.error( "doing something! ");
			list.addChild( DisplayRegistry.createDisplay( new DefaultVariable( "ID"+i++, it.next() )));
		}
		super.renderContainer(gl);
	}
}
