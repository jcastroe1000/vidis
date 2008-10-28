package vidis.ui.events;

import java.awt.event.MouseEvent;

public class MouseReleasedEvent extends AMouseEvent {

	public MouseReleasedEvent(MouseEvent m) {
		super(m);
	}
	
	public int getID() {
		return IVidisEvent.MouseReleasedEvent;
	}

}
