package vidis.modules.vectorClockAlgorithm;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class VectorClockAlgorithmNode extends AUserNode {
    private VectorTime localTimeVector = new VectorTime(this, 0);

    @Display(name = "name")
    public String toString()  {
    	return getId() + "["+getTimeVector()+"]";
    }
    
    public VectorTime getTimeVector() {
    	return localTimeVector;
    }
    
    public int getLocalTime() {
    	return localTimeVector.getNodeTime(this);
    }
    
    public void execute() {
		// in 3% of all cases do 'some action' and increase local time
		// then notify the neighbors
		if (Math.random() < 0.03d) {
		    increaseLocalTime();
		    for (IUserLink link : this.getConnectedLinks()) {
		    	send(link, new VectorClockAlgorithmPacket(getTimeVector()));
		    }
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
