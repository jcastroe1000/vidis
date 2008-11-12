package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class BullyElectionAlgorithmNode extends AUserNode {
    public String bully = null;

    public boolean gotBully() {
    	return bully != null;
    }
    
    public String getBully() {
    	return bully;
    }
    private boolean amIBiggerBully(String me, String other) {
    	return me.compareTo(other) > 0;
    }
    
    @Override
    public void init() {
    	// TODO Auto-generated method stub
    	
    }

    public void execute() {
    }
    
    @Display(name="Reset election!")
    public void restart() {
    	// reset 
    	bully = null;
    	// send everyone
    	for(IUserLink l : getConnectedLinks()) {
    		send(new ElectionRestartPacket(getBullyId()), l);
    	}
    }
    
    @Display(name="Start election!")
    public void start() {
//    	restart();
    	// now propagate election packet
    	for(IUserLink l : getConnectedLinks()) {
    		send(new ElectionPacket(getBullyId()), l);
    	}
    }
    
    @Display(name="Check election!")
    public void check() {
    	if(gotBully()) {
	    	for(IUserLink l : getConnectedLinks()) {
	    		send(new ElectionCheckPacket(getBully(), getBullyId()), l);
	    	}
    	}
    }
    
    private void receive(ElectionCheckPacket p) {
    	if(!gotBully() || !p.getBullyId().equals(getBully())) {
    		// restart
    		restart();
    	} else {
    		// propagate check
    		if(amIBiggerBully(getBullyId(), p.getSenderId())) {
    			// propagate to everyone but sender
    			for(IUserLink l : getConnectedLinks()) {
    	    		send(new ElectionCheckPacket(getBully(), getBullyId()), l);
    	    	}
    		}
    	}
    }
    
    private void receive(ElectionRestartPacket p) {
    	if(gotBully()) {
	    	// forward everyone but sender
    		for(IUserLink l : getConnectedLinks()) {
    			if(!l.equals(p.getLinkToSource())) {
    				send(new ElectionRestartPacket(getBullyId()), l);
    			}
        	}
    	}
    	// reset 
    	bully = null;
    }
    
    private void propagateBully(String bully, IUserLink notToThisOne) {
    	for(IUserLink l : getConnectedLinks()) {
    		if(notToThisOne != null && !l.equals(notToThisOne)) {
    			send(new ElectionPacket(bully), l);
    		}
    	}
    }
    
    private void receive(ElectionPacket p) {
    	if(gotBully()) {
    		if(p.getBullyId().equals(getBully())) {
    			// do nothing, we have the same
    		} else {
	    		// we already got a bully, see if the new one is bigger
	    		if(amIBiggerBully(getBully(), p.getBullyId())) {
	    			// the bully we have is bigger
	    			send(new ElectionPacket(getBully()), p.getLinkToSource());
	    		} else {
	    			// the bully we have is smaller, accept new bully
	    			bully = p.getBullyId();
	    			// forward everybody but sender
	    			propagateBully(bully, p.getLinkToSource());
	    		}
    		}
    	} else {
    		// we have no bully
	    	if(amIBiggerBully(getBullyId(), p.getBullyId())) {
	    		// I am bigger, notify sender
	    		send(new ElectionPacket(getBullyId()), p.getLinkToSource());
	    		bully = getBullyId();
	    		// notify everybody else about my "victory"
	    		propagateBully(bully, p.getLinkToSource());
	    	} else {
	    		// accept him
	    		bully = p.getBullyId();
	    		// I am smaller, propagate him
	    		propagateBully(bully, p.getLinkToSource());
	    	}
    	}
    }
    
    public void receive(IUserPacket packet) {
		if (packet instanceof ElectionRestartPacket) {
		    receive((ElectionRestartPacket) packet);
		} else if (packet instanceof ElectionCheckPacket) {
		    receive((ElectionCheckPacket) packet);
		} else if (packet instanceof ElectionPacket) {
		    receive((ElectionPacket) packet);
		} else {
	//	    Logger
	//		    .output(LogLevel.ERROR, this,
	//			    "receive 'unknown' packet from "
	//				    + packet.getSource());
		}
    }
    
    public String getBullyId() {
    	return getId();
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
