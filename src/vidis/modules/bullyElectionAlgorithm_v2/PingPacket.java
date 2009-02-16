/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;

@ComponentColor(color=ColorType.BLUE)
public class PingPacket extends ABullyPacket {

    private String id;
    private String senderId;

    public PingPacket(String id, String senderId) {
    	this.id = id;
    	this.senderId = senderId;
    }

    public String getBullyId() {
    	return id;
    }
    
    public String getSenderId() {
    	return senderId;
    }
    
    @DisplayColor
    public ColorType color = ColorType.ORANGE;
    
    @Display ( name="name" )
    public String toString() {
    	return "Ping{"+getBullyId()+","+getSenderId()+","+getHops()+"/"+getMaxHops()+"}";
    }

	@Override
	public int getMaxHops() {
		return 15;
	}
}