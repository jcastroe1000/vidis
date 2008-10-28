package vidis.ui.events;

import java.awt.event.MouseEvent;

public class MousePressedEvent extends AMouseEvent {

	public MousePressedEvent(MouseEvent m) {
		super(m);
	}

	
	public int getID() {
		return IVidisEvent.MousePressedEvent;
	}

}
