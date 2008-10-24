package vidis.modules.bullyElectionAlgorithm;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class BullyElectionAlgorithmNode extends AUserNode {
    public String bully = null;

    public String pendingBully = null;

    public boolean electionSent = false;

    public boolean gotBully() {
    	return bully != null;
    }

    public void execute() {
		if (gotBully()) {
		    // got a bully, we're fine
		} else if (electionSent) {
		    // fine, we waited for some time and no-one claimed being bully, let us be bully :-)
		    bully = getBullyId();
		    // reset electionSent flag
		    electionSent = false;
		    // propagate winning message
		    sendElectionWinPacket();
		} else if (pendingBully != null) {
		    // we were pending for a bully
		    if (gotBully() && pendingBully.equals(bully)) {
			// fine, he is bully, accept
			pendingBully = null;
		    } else {
			// problem, received no bully win packet
			// reset
			bully = null;
			pendingBully = null;
			// send election
			sendElectionPacket();
		    }
		} else {
		    // got no bully, start election
		    sendElectionPacket();
		}
    }

    private void sendElectionPacket() {
    	if( ! electionSent ) {
			for (IUserLink link : getConnectedLinks()) {
				if( ((BullyElectionAlgorithmNode)(link.getOtherNode( this ))).getBullyId().compareTo(getBullyId()) > 0) {
					send(new ElectionPacket(getBullyId()), link);
				}
			}
    	}
		electionSent = true;
		// wait for some time
		sleep(50);
    }

    private void sendElectionWinPacket() {
		for (IUserLink link : getConnectedLinks())
		    send(new ElectionWinPacket(getBullyId()), link);
    }

    private String getBullyId() {
    	return getId();
    }

    public void receive(ElectionPacket packet) {
		// someone wants to elect a new bully
		if (getBullyId().compareTo(packet.getBullyId()) < 0) {
		    // we got smaller id, we're not bully
		    // broadcast election packet
		    for (IUserLink link : getConnectedLinks())
				if (!link.equals(packet.getLinkToSource())) {
					if( ((BullyElectionAlgorithmNode)(link.getOtherNode( this ))).getBullyId().compareTo(getBullyId()) > 0) {
						send(new ElectionPacket(packet.getBullyId()), link);
					}
				}
		    // now wait for some time and accept him as bully
		    pendingBully = packet.getBullyId();
		    sleep(15);
		} else {
		    // send election
		    sendElectionPacket();
		}
    }

    public void receive(ElectionWinPacket packet) {
		if (getBullyId().compareTo(packet.getBullyId()) < 0 && ( bully != null && bully.compareTo(packet.getBullyId()) < 0)) {
		    // we got smaller id, accept
		    bully = packet.getBullyId();
		    // broadcast winner
		    for (IUserLink link : getConnectedLinks())
		    	if ( ! packet.getLinkToSource().equals(link))
		    		send(new ElectionWinPacket(packet.getBullyId()), link);
		} else {
		    // we got bigger id, broadcast election
		    sendElectionPacket();
		}
    }

    public void receive(IUserPacket packet) {
		if (packet instanceof ElectionPacket) {
		    receive((ElectionPacket) packet);
		} else if (packet instanceof ElectionWinPacket) {
		    receive((ElectionWinPacket) packet);
		} else {
	//	    Logger
	//		    .output(LogLevel.ERROR, this,
	//			    "receive 'unknown' packet from "
	//				    + packet.getSource());
		}
    }
    @Display ( name="name" )
    public String toString() {
    	if ( bully != null ) {
	    	if(bully.equals(getBullyId()))
	    		return "{"+ getBullyId() +"}-Bully=ME!";
	    	else
	    		return "{" + getBullyId() + "}-Bully="+bully;
    	} else {
    		return "{" + getBullyId() + "}-Bully=???";
    	}
    }
}
