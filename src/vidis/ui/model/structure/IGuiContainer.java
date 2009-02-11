/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.structure;

import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;

import vidis.ui.events.IEventHandler;

public interface IGuiContainer extends IVisObject, IEventHandler {

	public final double Z_OFFSET = 0.0002;
	
	public void render( GL gl );
	public void renderBox( GL gl, double d );
	
	public double getWantedHeight();
	public double getHeight();
	public double getWidth();
	
	public double getX();
	public double getY();
	
	/**
	 * returns x in the roots coordinate system
	 * @return
	 */
	public double getAbsoluteX();
	/**
	 * returns y in the roots coordinate system
	 * @return
	 */
	public double getAbsoluteY();
	
	public void setHeight( double height );
	public void setWidth( double width );
	public void setX( double x );
	public void setY( double y );
	
	public void setBounds( double x, double y, double height, double width );
	
	public void setLayout( ILayout layout );
	public ILayout getLayout();
	
	// parent child ..
	public IGuiContainer getParent();
	public Set<IGuiContainer> getChilds();
	public void addChild( IGuiContainer c );
	public void removeChild( IGuiContainer c );
	public void setParent( IGuiContainer container );
	// ----
	
	public boolean isPointInContainer( Point2d p );
	
	public void setVisible( boolean v );
	public boolean isVisible();
}
