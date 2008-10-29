package vidis.modules.echoAlgorithm;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.data.AUserNode;
import vidis.data.annotation.Display;
import vidis.data.annotation.DisplayColor;
import vidis.data.mod.IUserLink;
import vidis.data.mod.IUserNode;
import vidis.data.mod.IUserPacket;

/**
 * * Es gibt zwei Nachrichtentypen: Explorernachrichten, die die Knoten rot färben und Echo-Nachrichten, die die Knoten grün färben. Vor der Ausführung des Algorithmus sind alle Knoten weiß.
 *
 *   * Ein Initiator wird rot und schickt an alle seine Nachbarn einen Explorer.
 *   * Ein weißer Knoten, der einen Explorer erhält wird rot
 *   * Treffen sich zwei Explorer auf einer Kante, so werden sie verschluckt
 *   * Ein Knoten, der über all seine Kanten einen Explorer erhalten hat, wird grün und sendet ein Echo über die Kante, über die er den ersten Explorer erhalten hatte
 *   * Ein Knoten, der einen Echo erhält, wird grün und sendet einen Echo über die Kante, über die er den Explorer erhalten hatte
 *   * Der Algorithmus terminiert, wenn der Initiator das letzte Echo erhalten hat
 *		
 *		Die Kanten über die die Echo-Nachrichten gelaufen sind ergeben einen Spannbaum.
 * *
 * @see http://de.wikipedia.org/wiki/Echo-Algorithmus
 * @author Dominik
 *
 */
public class EchoNode extends AUserNode {
	private static Logger logger = Logger.getLogger(EchoNode.class);
	
	private enum State {
		WHITE( Color.WHITE ),
		RED( Color.RED ),
		GREEN( Color.GREEN );
		private Color color;
		private State( Color c ) {
			this.color = c;
		}
		public Color getColor() {
			return color;
		}
	};

	@DisplayColor()
	public Color getColor() {
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
