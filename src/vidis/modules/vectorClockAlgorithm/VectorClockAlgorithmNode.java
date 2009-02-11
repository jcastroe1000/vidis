/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

/**
 * vector clock algorithm node, even cooler :-)
 * @author Dominik
 *
 */
public class VectorClockAlgorithmNode extends AUserNode {
    private VectorTime localTimeVector = new VectorTime(this, 0);
    
    @Display(name="Automatische Events")
	public boolean autoEvents = false;

    @Display(name = "name")
    public String toString()  {
    	return getId() + "["+getTimeVector()+"]";
    }
    
    @Override
    public void init() {
    	// TODO Auto-generated method stub
    	
    }
    
    @Display(name="Erzeuge Event")
    public void erzeugeEvent() {
    	macheEventAktion();
    }
    
    @Display(name="Autom.Events")
    public void toggleAutoEvents() {
    	autoEvents = !autoEvents;
    }
    
    private void macheEventAktion() {
    	increaseLocalTime();
	    for (IUserLink link : this.getConnectedLinks()) {
	    	send(link, new VectorClockAlgorithmPacket(getTimeVector()));
	    }
    }
    
    public VectorTime getTimeVector() {
    	return localTimeVector;
    }
    
    public int getLocalTime() {
    	return localTimeVector.getNodeTime(this);
    }
    
    public void execute() {
    	if(autoEvents && Math.random() < 0.03) {
    		macheEventAktion();
    	}
    }

    private void increaseLocalTime() {
    	getTimeVector().update(this, getLocalTime()+1);
	}

	/**
     * simply wraps the send functionality to unify advanced parameters such as processing time
     * @param link the link to send over
     * @param packet the packet to send
     */
    private void send(IUserLink link, VectorClockAlgorithmPacket packet) {
    	send(packet, link, 1 + (long) (Math.random() * 2));
    }

    private void receive(VectorClockAlgorithmPacket packet) {
    	increaseLocalTime();
		VectorTime remoteTimeVector = packet.getTime();
		// update my time despite on the time we receive
		getTimeVector().update(remoteTimeVector);
    }

    public void receive(IUserPacket packet) {
		if (packet instanceof VectorClockAlgorithmPacket) {
		    receive((VectorClockAlgorithmPacket) packet);
		} else {
		    //Logger.output(LogLevel.ERROR, this, "receive 'unknown' packet from " + packet.getSource());
		}
    }
}
