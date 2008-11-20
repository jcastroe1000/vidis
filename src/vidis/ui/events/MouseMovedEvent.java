package vidis.ui.events;

import java.awt.event.MouseEvent;


/**
 * 
 * @author christoph
 *
 */
public class MouseMovedEvent extends AMouseEvent {
	public MouseMovedEvent(MouseEvent m) {
		super(m);
	}
	
	public int getID() {
		return IVidisEvent.MouseMovedEvent;
	}

}
