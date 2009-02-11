/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

public class ElectionRestartPacket extends ABullyPacket {

    private String id;
    
    public ElectionRestartPacket(String id) {
    	this.id = id;
    }

    public String getBullyId() {
    	return id;
    }
    
    @DisplayColor
    public ColorType color = ColorType.BLUE;
    
    @Display ( name="name" )
    public String toString() {
    	return "Restart{"+getBullyId()+","+getHops()+"/"+getMaxHops()+"}";
    }

	@Override
	public int getMaxHops() {
		return 15;
	}
}