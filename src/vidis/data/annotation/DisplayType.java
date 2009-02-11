/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.data.annotation;

/**
 * this enumeration determines where a annotated variable
 * should be displayed
 * 
 * THIS ENUM IS DEPRECATED, AS IT IS THE DISPLAY ANNOTATION!
 * 
 * @author Dominik
 * @see Display
 */
@Deprecated
public enum DisplayType implements Comparable<DisplayType> {
	/**
	 * determines that it should be shown in the 3d world
	 */
	SHOW_3D,
	/**
	 * determines that it should be shown in the gui element
	 */
	SHOW_SWING,
	/**
	 * determines that it should be shown in both, 3d world and gui
	 */
	SHOW_3D_AND_SWING,
	/**
	 * determines that this option should be executable
	 */
	EXECUTEABLE;
}
