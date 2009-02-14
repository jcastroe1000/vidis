package vidis.modules.tommy;

import java.util.List;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserPacket;

public class TommyNode extends AUserNode {
	private static Logger logger = Logger.getLogger(TommyNode.class);
	
	@Display(name="header1")
	public String header = "";
	
	private IUserLink neighborForward;
	private IUserLink neighborBackward;
	private int range;
	
	@Override
	public void init() {
		try {
			range = Integer.parseInt((String)getVariable("range").getData());
			header = "Node " + range;
			
			List<IUserLink> links = getConnectedLinks();
			if(((TommyNode)links.get(0).getOtherNode(this)).getRange() > range) {
				if(((TommyNode)links.get(0).getOtherNode(this)).getRange() > ((TommyNode)links.get(1).getOtherNode(this)).getRange()) {
					neighborForward = links.get(0);
					neighborBackward = links.get(1);
				} else {
					neighborForward = links.get(1);
					neighborBackward = links.get(0);
				}
			} else {
				if(((TommyNode)links.get(0).getOtherNode(this)).getRange() > ((TommyNode)links.get(1).getOtherNode(this)).getRange()) {
					neighborForward = links.get(1);
					neighborBackward = links.get(0);
				} else {
					neighborForward = links.get(0);
					neighborBackward = links.get(1);
				}
				
			}
		} catch(Exception ex) {
			System.out.println("Glump mann");
		}
	}

	public void receive(IUserPacket packet) {
		header = "Node" + range;
		
		if(packet instanceof TommyPacketSearch) {
			TommyPacketSearch tps = (TommyPacketSearch)packet;
			
			if(tps.getSearch() >= range && tps.getSearch() < range+1) {
				send(new TommyPacketResponse(tps.getId()), neighborBackward);
			} else {
				send(new TommyPacketSearch(tps), neighborForward);
			}
		} else if(packet instanceof TommyPacketResponse) {
			TommyPacketResponse tpr = (TommyPacketResponse)packet;
			
			if(tpr.getId() == range) {
				header += ", JIPPIE received";
			} else {
				send(new TommyPacketResponse(tpr), neighborBackward);
			}
		}
	}
	
	@Display(name="search")
	public void start(String search) {
		Float value = Float.parseFloat(search);
		if(value >= range && value < range+1) {
			header += "GOT IT by myself";
			
			return;
		}
		
		try {
			send(new TommyPacketSearch(Float.parseFloat(search), range), neighborForward);
		} catch(Exception ex) {
			System.out.println("QUATSCH mann");
		}
	}

	public void execute() {
		// TODO Auto-generated method stub

	}
	
	public int getRange() {
		return range;
	}

}
