package vidis.modules.bullyElectionAlgorithm_v2;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class BullyElectionAlgorithmNode extends AUserNode {
    public String bully = null;
    public int checkTimeout = -1;
    public int startTimeout = -1;
    public boolean enabled = true;

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
    	if(checkTimeout > 0) {
    		checkTimeout--;
    	} else if(checkTimeout == 0) {
    		checkTimeout = -1;
    		// got no pong from the bully in time, restart election
    		restart();
    		startTimeout  = 60;
    	} else {
    		// no timeout
    	}
    	
    	if(startTimeout > 0) {
    		startTimeout--;
    	} else if(startTimeout == 0) {
    		startTimeout = -1;
    		start();
    	}
    	
//    	// default action, check from time to time
//    	if(Math.random() < 0.01) {
//    		check();
//    	}
    }
    
    @Display(name="Reset election!")
    public void restart() {
    	// reset 
    	bully = null;
    	// send everyone
    	for(IUserLink l : getConnectedLinks()) {
    		sendMy(new ElectionRestartPacket(getBullyId()), l, null);
    	}
    }
    
    @Display(name="Start election!")
    public void start() {
//    	restart();
    	// set me as bully
    	this.bully = getBullyId();
    	// now propagate election packet
    	for(IUserLink l : getConnectedLinks()) {
    		sendMy(new ElectionPacket(getBullyId()), l, null);
    	}
    }
    
    @Display(name="Check election!")
    public void check() {
    	if(gotBully()) {
    		checkTimeout = 100;
	    	for(IUserLink l : getConnectedLinks()) {
	    		sendMy(new PingPacket(getBully(), getBullyId()), l, null);
	    	}
    	} else {
    		start();
    	}
    }
    
    @Display(name="Enable/Disable")
    public void toggleEnabled() {
    	enabled = !enabled;
    }
    
    private void sendMy(ABullyPacket p, IUserLink l, Integer hops) {
    	long wait = (int)(Math.random()*2 + 0);
    	if(enabled == false) {
    		return;
    	}
    	if(hops != null) {
    		p.setHops(hops-1);
    	}
    	if(p.getHops() > 0)
    		send(p, l, wait);
    	// timeouted
    }
    
    private void receive(PongPacket p) {
    	// if i am the sender of the pong, fine
    	if(p.getSenderId().equals(getBullyId())) {
    		// fine, reset countdown
    		checkTimeout = -1;
    	} else {
    		// forward pong
			for(IUserLink l : getConnectedLinks()) {
				if(!l.equals(p.getLinkToSource())) {
					sendMy(new PongPacket(p.getBullyId(), p.getSenderId()), l, p.getHops());
				}
    		}
    	}
    }
    
    private void receive(PingPacket p) {
    	if(!gotBully() || !p.getBullyId().equals(getBully())) {
    		// restart
    		restart();
    	} else {
    		if(getBullyId().equals(p.getBullyId())) {
    			// I am the bully, respond with pong
    			sendMy(new PongPacket(getBully(), p.getSenderId()), p.getLinkToSource(), p.getMaxHops()-p.getHops());
    		} else if(!p.getSenderId().equals(getBullyId())) {
    			// propagate to everyone but sender
    			for(IUserLink l : getConnectedLinks()) {
    				if(!p.getLinkToSource().equals(l)) {
    					sendMy(new PingPacket(p.getBullyId(), p.getSenderId()), l,p.getHops());
    				}
    	    	}
    		}
    	}
    }
    
    private void receive(ElectionRestartPacket p) {
    	if(gotBully()) {
	    	// forward everyone but sender
    		for(IUserLink l : getConnectedLinks()) {
    			if(!l.equals(p.getLinkToSource())) {
    				sendMy(new ElectionRestartPacket(getBullyId()), l, p.getHops());
    			}
        	}
    	}
    	// reset 
    	bully = null;
    }
    
    private void propagateBully(String bully, IUserLink notToThisOne, Integer hops) {
    	for(IUserLink l : getConnectedLinks()) {
    		if(notToThisOne != null && !l.equals(notToThisOne)) {
    			sendMy(new ElectionPacket(bully), l, hops);
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
	    			sendMy(new ElectionPacket(getBully()), p.getLinkToSource(), p.getHops());
	    		} else {
	    			// the bully we have is smaller, accept new bully
	    			bully = p.getBullyId();
	    			// forward everybody AND sender
	    			propagateBully(bully, p.getLinkToSource(), p.getHops());
	    		}
    		}
    	} else {
    		// we have no bully
	    	if(amIBiggerBully(getBullyId(), p.getBullyId())) {
	    		// I am bigger, notify sender
	    		sendMy(new ElectionPacket(getBullyId()), p.getLinkToSource(), p.getHops());
	    		bully = getBullyId();
	    		// notify everybody else about my "victory"
	    		propagateBully(bully, p.getLinkToSource(), p.getHops());
	    	} else {
	    		// accept him
	    		bully = p.getBullyId();
	    		// I am smaller, propagate him
	    		propagateBully(bully, p.getLinkToSource(), p.getHops());
	    	}
    	}
    }
    
    public void receive(IUserPacket packet) {
    	if(enabled == false) {
    		return;
    	}
		if (packet instanceof ElectionRestartPacket) {
		    receive((ElectionRestartPacket) packet);
		} else if (packet instanceof PingPacket) {
		    receive((PingPacket) packet);
		} else if (packet instanceof ElectionPacket) {
		    receive((ElectionPacket) packet);
		} else if (packet instanceof PongPacket) {
		    receive((PongPacket) packet);
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
    
    public boolean amIBully() {
    	return gotBully() && bully.equals(getBullyId());
    }
    
    @DisplayColor
    public ColorType getNodeColor() {
    	if(!enabled)
    		return ColorType.RED;
    	else if(checkTimeout > 0)
    		return ColorType.ORANGE_LIGHT;
    	else if(startTimeout > 0)
    		return ColorType.ORANGE;
    	else if(amIBully())
    		return ColorType.GREEN;
    	else
    		return ColorType.GREY;
    }
    
    @Display ( name="name" )
    public String toString() {
    	String out = "{"+ getBullyId() +"}";
    	if ( gotBully() ) {
	    	if(bully.equals(getBullyId()))
	    		out += "-Bully=ME!";
	    	else {
	    		out+="-Bully="+bully;
	    	}
    	} else {
    		out+="-Bully=???";
    	}
    	if(checkTimeout > -1) {
			out+="-CHECK="+checkTimeout;
		}
    	if(startTimeout > -1) {
			out+="-STARTIN="+startTimeout;
		}
    	if(!enabled)
    		out+="-DISABLED";
    	return out;
    }
}
