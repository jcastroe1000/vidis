package vidis.ui.events;

import javax.vecmath.Point2d;

@Deprecated
public class GuiMouseEvent implements IVidisEvent {
//	private static Logger logger = Logger.getLogger(GuiMouseEvent.class);

	public Point2d where;
	
	
	public int getID() {
		return IVidisEvent.GuiMouseEvent;
	}

	@Override
	public String toString() {
		return "VidisMouseEvent @ "+where;
	}

}
