package vidis.ui.events;

import java.awt.event.MouseEvent;

import javax.vecmath.Point2d;
import javax.vecmath.Point3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

public abstract class AMouseEvent implements IVidisEvent {
	private static Logger logger = Logger.getLogger( AMouseEvent.class );
	
	public MouseEvent mouseEvent;
	
	public boolean rayCalculated = false;
	public Point3d rayOrigin; // ?
	public Vector3d ray; // ?
	public Point2d guiCoords; 
	public Point2d guiCoordsRelative;

	public boolean forwardTo3D = false;
	
	public AMouseEvent( MouseEvent m ) {
		if ( m == null ) {
			logger.error( "MouseEvent was null!!!" );
		}
		mouseEvent = m;
	}
	
}
