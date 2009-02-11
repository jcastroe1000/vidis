/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;


public class BasicMarginLayout implements ILayout {

	private IGuiContainer container;
	
	private double left;
	private double top;
	private double right;
	private double bottom;
	private double height;
	private double width;
	
	private double calcedHeight;
	private double calcedWidth;
	private double calcedX;
	private double calcedY;
	/**
	 * values under 0 are treated as autovalue
	 * @param left
	 * @param top
	 * @param right
	 * @param bottom
	 * @param height
	 * @param width
	 */
	public BasicMarginLayout( double left, double top, double right, double bottom, double height, double width ) {
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
		this.height = height;
		this.width = width;
	}
	
	public void setGuiContainer( IGuiContainer c ) {
		this.container = c;
	}
	
	public double getHeight() {
		return calcedHeight;
	}

	public double getWidth() {
		return calcedWidth;
	}

	public double getX() {
		return calcedX;
	}

	public double getY() {
		return calcedY;
	}

	public void layout() {
		// retrieve height & width from parent
		double parentHeight = container.getParent().getHeight();
		double parentWidth = container.getParent().getWidth();
//		System.out.println("ph="+parentHeight+", pw="+parentWidth);
		// y & height
		if ( top < 0 && height < 0 && bottom < 0 ) {
			calcedY = parentHeight / 3d;
			calcedHeight = parentHeight / 3d;
//			System.out.println( "fall1: cY="+calcedY+", cH="+calcedHeight );
		}
		else if ( top >= 0 && height < 0 && bottom < 0 ) {
			calcedY = top;
			calcedHeight = (parentHeight - top) / 2d;
//			System.out.println( "fall2: cY="+calcedY+", cH="+calcedHeight );
		}
		else if ( top >= 0 && height >= 0 ) {
			calcedY = top;
			calcedHeight = height;
//			System.out.println( "fall34: cY="+calcedY+", cH="+calcedHeight );
		}
		else if ( top < 0 && height >= 0 && bottom < 0 ) {
			calcedY = (parentHeight - height) / 2d;
			calcedHeight = height;
//			System.out.println( "fall5: cY="+calcedY+", cH="+calcedHeight );
		} 
		else if ( top < 0 && height >=0 && bottom >= 0 ) {
			calcedY = parentHeight - height - bottom;
			calcedHeight = height;
//			System.out.println( "fall6: cY="+calcedY+", cH="+calcedHeight );
		}
		else if ( top < 0 && height < 0 && bottom >= 0 ) {
			calcedY = (parentHeight - bottom) / 2d;
			calcedHeight = (parentHeight - bottom) / 2d;
//			System.out.println( "fall7: cY="+calcedY+", cH="+calcedHeight );
		}
		else if ( top >= 0 && height < 0 && bottom >=0 ) {
			calcedY = top;
			calcedHeight = parentHeight - top - bottom;
//			System.out.println( "fall8: cY="+calcedY+", cH="+calcedHeight );
		}
		
		// x & width
		if ( left < 0 && width < 0 && right < 0 ) {
			calcedX = parentWidth / 3d;
			calcedWidth = parentWidth / 3d;
		}
		else if ( left >= 0 && width < 0 && right < 0 ) {
			calcedX = left;
			calcedWidth = (parentWidth - left) / 2d;
		}
		else if ( left >= 0 && width >= 0 ) {
			calcedX = left;
			calcedWidth = width;
		}
		else if ( left < 0 && width >= 0 && right < 0 ) {
			calcedX = (parentWidth - width) / 2d;
			calcedWidth = width;
		} 
		else if ( left < 0 && width >=0 && right >= 0 ) {
			calcedX = parentWidth - width - right;
			calcedWidth = width;
		}
		else if ( left < 0 && width < 0 && right >= 0 ) {
			calcedX = (parentWidth - right) / 2d;
			calcedWidth = (parentWidth - right) / 2d;
		}
		else if ( left >= 0 && width < 0 && right >=0 ) {
			calcedX = left;
			calcedWidth = parentWidth - left - right;
		}
		

	}
	

}
