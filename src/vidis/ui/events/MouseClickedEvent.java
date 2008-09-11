package vidis.ui.events;

import java.awt.event.MouseEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Point4d;
import javax.vecmath.Vector4d;

public class MouseClickedEvent extends AMouseEvent {

	public MouseClickedEvent(MouseEvent m) {
		super(m);
	}

	public Point4d rayOrigin; // ?
	public Vector4d ray; // ?
	public Point2d guiCoords; 
	
	public int getID() {
		return IVidisEvent.MouseClickedEvent;
	}

}
