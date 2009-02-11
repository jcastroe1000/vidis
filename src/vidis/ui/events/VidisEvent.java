/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.events;


public class VidisEvent<D> implements IVidisEvent {

	private int eventId;
	private D data;
	
	public VidisEvent( int eventId, D data ) {
		this.eventId = eventId;
		this.data = data;
	}

	public int getID() {
		return eventId;
	}
	
	public D getData() {
		return data;
	}

	@Override
	public String toString() {
		if ( data != null ) {
			return "VidisEvent<"+data.getClass().getSimpleName()+"> id=" +eventId;
		}
		else {
			return "VIdisEvent id=" +eventId;
		}
	}
}
