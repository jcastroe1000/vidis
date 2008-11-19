package vidis.modules.echoAlgorithm;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
import vidis.data.annotation.ColorType;
import vidis.data.annotation.ComponentColor;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;

/**
 * Es gibt zwei Nachrichtentypen: Explorernachrichten, die die Knoten rot faerben und Echo-Nachrichten, die die Knoten gruen faerben. Vor der Ausfuehrung des Algorithmus sind alle Knoten weiss.
 * @see http://de.wikipedia.org/wiki/Echo-Algorithmus
 * @author Dominik
 *
 */
@ComponentColor(color=ColorType.BLACK)
public class EchoNode extends AUserNode {
	private static Logger logger = Logger.getLogger(EchoNode.class);
	
	private enum State {
		WHITE( ColorType.WHITE ),
		RED( ColorType.RED ),
		GREEN( ColorType.GREEN );
		private ColorType color;
		private State( ColorType c ) {
			this.color = c;
		}
		public ColorType getColor() {
			return color;
		}
	};

	@DisplayColor()
	public ColorType getColor() {
		return state.getColor();
	}
	
	private State state = State.WHITE;
	private IUserNode firstNeighbour = null;
	private int counter = 0;
	
	private boolean thisOneSends() {
		return hasVariable("initer");
	}
	
	/**
	 * executed at first simulation step
	 */
	public void init() {
		if ( thisOneSends() ) {
			for ( IUserLink l : getConnectedLinks() ) {
				send( new ExplorePacket(), l);
			}
			state = State.RED;
		}
	}

	public void receive(IUserPacket packet) {
		if ( packet instanceof ExplorePacket ) {
			if ( state == State.WHITE ) {
				state = State.RED;
				for ( IUserLink l : getConnectedLinks() ) {
					if ( ! l.equals(packet.getLinkToSource()) ) {
						send( new ExplorePacket(), l);
					}
				}
				firstNeighbour = packet.getSource();
			}
			counter++;
		}
		if ( packet instanceof EchoPacket || counter == getConnectedLinks().size() ) {
			state = State.GREEN;
			if ( thisOneSends() ) {
				logger.info("WE'RE DONE!");
			} else {
				for ( IUserLink l : getConnectedLinks() ) {
					if ( firstNeighbour.equals( l.getOtherNode(this) ) ) {
						send ( new EchoPacket(), l );
					}
				}
			}
		}
	}

	public void execute() {
	}

	@Display(name="name")
	public String toString() {
		return (thisOneSends() ? "MASTER" : "SLAVE") + super.toString() + "." + state + "." + counter;
	}
}
