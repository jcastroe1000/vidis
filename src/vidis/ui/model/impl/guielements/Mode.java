/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements;

public enum Mode {
	MINIMIZED( 2, 0 ),
	NORMAL( 4, 0 ),
	EXPANDED( 4, 11 ),
	EXPANDED2( 4, 26 );
	
	private double height;
	private double topHeight;
	private double mainHeight;
	
	private Mode( double topHeight, double mainHeight ) {
		this.topHeight = topHeight;
		this.mainHeight = mainHeight;
		this.height = topHeight + mainHeight;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getTopHeight() {
		return this.topHeight;
	}
	
	public double getMainHeight() {
		return this.mainHeight;
	}
	
	public double getTopY() {
		return this.mainHeight;
	}
	
	public double getMainY() {
		if ( mainHeight == 0 ) {
			return Double.MAX_VALUE;
		}
		else {
			return 0;
		}
	}
}
