/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.events.mouse;

import java.awt.event.MouseEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;

public class AMouseEvent implements IVidisEvent {
	private static Logger logger = Logger.getLogger( AMouseEvent.class );
	
	public MouseEvent mouseEvent;
	
	// for the 3d events
	public Point3d rayOrigin;
	public Vector3d ray;
	
	// for the gui events
	public Point2d guiCoords; 
	public Point2d guiCoordsRelative;

	private int id;
	
	public AMouseEvent( int type, MouseEvent m ) {
		this.id = type;
		if ( m == null ) {
			logger.error( "MouseEvent was null!!!" );
		}
		mouseEvent = m;
	}
	
	public int getID() {
		return id;
	}
	
}
