/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserPacket;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.Display;

/**
 * vector clock algorithm packet .. really cool :-)
 * @author Dominik
 *
 */
@ComponentColor(color = ColorType.GREY_LIGHT)
public class VectorClockAlgorithmPacket extends AUserPacket {
    private VectorTime time = new VectorTime();
    public VectorClockAlgorithmPacket(VectorTime time) {
    	this.time.update(time);
    }
    public VectorTime getTime() {
    	return time;
    }
    
    @Display(name="name")
    public String getName() {
    	return getTime().toString();
    }

    public String toString() {
    	return getName();
    }
}