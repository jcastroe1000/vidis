package vidis.ui.events;

import java.awt.event.MouseEvent;

public class MouseClickedEvent extends AMouseEvent {

	public MouseClickedEvent(MouseEvent m) {
		super(m);
	}

	public int getID() {
		return IVidisEvent.MouseClickedEvent;
	}

}
