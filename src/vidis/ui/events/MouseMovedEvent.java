package vidis.ui.events;

import java.awt.event.MouseEvent;

import org.apache.log4j.Logger;


/**
 * 
 * @author christoph
 *
 */
public class MouseMovedEvent extends AMouseEvent {
	private static Logger logger = Logger.getLogger(MouseMovedEvent.class);

	public MouseMovedEvent(MouseEvent m) {
		super(m);
	}
	
	public int getID() {
		return IVidisEvent.MouseMovedEvent;
	}

}
