package vidis.ui.vis.camera;

import java.awt.Point;
import java.awt.Rectangle;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.StartEvent;
import vidis.ui.events.StopEvent;
import vidis.ui.mvc.api.Dispatcher;

public class DefaultCamera extends AEventHandler implements ICamera {
	private static Logger logger = Logger.getLogger( DefaultCamera.class );
	
	/* render target */
	private Rectangle target;
	/* Position of the camera */
	private double posx;
	private double posz;
	private double step = 0.1; // Scrollspeed
	/* Zoom */
	private double zoom;
	/* the angle which we look down */
	private double anglex = 0;
	private double angley = 0;
	
	/* Actions */
	private boolean scrollLeft = false;
	private boolean scrollRight = false;
	private boolean scrollUp = false;
	private boolean scrollDown = false;
	
	private boolean zoomIn = false;
	private boolean zoomOut = false;
	
	/**
	 * used for calc3DMousePoint
	 */
	private DoubleBuffer model = DoubleBuffer.allocate(16), proj = DoubleBuffer
			.allocate(16);
	private IntBuffer view = IntBuffer.allocate(4);

	private boolean skewDown = false;
	private boolean skewUp = false;
	private boolean rotateLeft = false;
	private boolean rotateRight = false;
	
	public DefaultCamera(){
		this.posx = 0;
		this.posz = 0;
		this.zoom = 1.0;
	}
	public void init(GL gl) {
		gl.glClearColor(0.4f, 0.4f, 0.4f, 1f);
		gl.glClear(GL.GL_COLOR_BUFFER_BIT | GL.GL_DEPTH_BUFFER_BIT);
		
		gl.glEnable( GL.GL_DEPTH_TEST );
		gl.glEnable( GL.GL_LIGHTING );
		gl.glEnable( GL.GL_LIGHT0 );
//		gl.glEnable( GL.GL_LIGHT1 );
		gl.glViewport((int)target.getX(), (int)target.getY(), (int)target.getWidth(), (int)target.getHeight());
	
		
	}
	public void applyProjectionMatrix(GL gl) {
		GLU glu = new GLU();
		glu.gluPerspective( 30, (double) target.getWidth() / (double) target.getHeight(), 1.0, 90.0);
		glu.gluLookAt( 0, 5*zoom, -10*zoom, 0, 0, 0, 0, 1, 0);
	}
	public void applyViewMatrix(GL gl) {
		gl.glScaled(-1, 1, 1);
		double realX, realZ;
		realX = posx;
		//realY = zoom;
		realZ = -posz;
		//gl.glOrtho(4*zoom, 4*zoom, 4*zoom, 4*zoom, realX, realZ);
		gl.glTranslated(realX, 1.0, realZ);
		// rotieren
		gl.glRotated(anglex, 1, 0, 0);
		gl.glRotated(angley, 0, 1, 0);
		// matrizen auslesen f�r click berechnung
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, model);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, proj);
		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
		
		gl.glColor3d( 1, 0, 0);
//		gl.glBegin( GL.GL_LINES );
//			gl.glVertex3d( P1.x, P1.y, P1.z );
//			gl.glVertex3d( P2.x, P2.y, P2.z );
//		gl.glEnd();
	}
	public String toString() {
		return "DefaultCamera at ("+posx+", "+posz+") Zoom: "+zoom;
	}
	public void setPosition(double posx, double posy){
		this.posx = posx;
		this.posz = posy;
	}
	public double getZoom() {
		return zoom;
	}
	public void setZoom(double zoom) {
		this.zoom = zoom;
	}
	public double getPosx() {
		return posx;
	}
	public void setPosx(double posx) {
		this.posx = posx;
	}
	public double getPosy() {
		return posz;
	}
	public void setPosy(double posy) {
		this.posz = posy;
	}
	
	public void handleEvent(IVidisEvent event) {
		logger.debug("handleEvent( "+event+" )" );
		switch ( event.getID() ) {
		case IVidisEvent.ZoomIn:
			if(event instanceof StartEvent)
				this.zoomIn = true;
			else if(event instanceof StopEvent)
				this.zoomIn = false;
			break;
		case IVidisEvent.ZoomOut:
			if(event instanceof StartEvent)
				this.zoomOut = true;
			else if(event instanceof StopEvent)
				this.zoomOut = false;
			break;
		case IVidisEvent.SkewUp:
			if(event instanceof StartEvent)
				this.skewUp = true;
			else if(event instanceof StopEvent)
				this.skewUp = false;
			break;
		case IVidisEvent.SkewDown:
			if(event instanceof StartEvent)
				this.skewDown = true;
			else if(event instanceof StopEvent)
				this.skewDown = false;
			break;
		case IVidisEvent.RotateLeft:
			if(event instanceof StartEvent)
				this.rotateLeft = true;
			else if(event instanceof StopEvent)
				this.rotateLeft = false;
			break;
		case IVidisEvent.RotateRight:
			if(event instanceof StartEvent)
				this.rotateRight = true;
			else if(event instanceof StopEvent)
				this.rotateRight = false;
			break;
		case IVidisEvent.ScrollDown:
			if ( event instanceof StartEvent ) {
				this.scrollDown = true;
			}
			else if ( event instanceof StopEvent ) {
				this.scrollDown = false;
			}
			break;
		case IVidisEvent.ScrollLeft:
			if ( event instanceof StartEvent ) {
				this.scrollLeft = true;
			}
			else if ( event instanceof StopEvent ) {
				this.scrollLeft = false;
			}
			break;
		case IVidisEvent.ScrollUp:
			if ( event instanceof StartEvent ) {
				this.scrollUp = true;
			}
			else if ( event instanceof StopEvent ) {
				this.scrollUp = false;
			}
			break;
		case IVidisEvent.ScrollRight:
			if ( event instanceof StartEvent ) {
				this.scrollRight = true;
			}
			else if ( event instanceof StopEvent ) {
				this.scrollRight = false;
			}
			break;
		case IVidisEvent.MouseClickedEvent:
			try {
			if ( ((MouseClickedEvent)event).ray == null ) {
				calc3DMousePoint( ((MouseClickedEvent)event) );
				Dispatcher.forwardEvent( event );
			}
			}
			catch ( Exception e ) {
				logger.error( "exception", e );
			}
			break;
		}
	}
	
	public void update() {
		if ( scrollUp ) {
			this.posz+=step; 
		}
		if ( scrollDown ) {
			this.posz-=step;
		}
		if ( scrollLeft ) {
			this.posx+=step;
		}
		if ( scrollRight ) {
			this.posx-=step;
		}
		if ( zoomIn ) {
			if(zoom > 0.4)
				this.zoom-=step/3;
		}
		if ( zoomOut ) {
			if(zoom < 5)
				this.zoom+=step/3;
		}
		if ( skewUp ) {
			if(anglex < 21)
				anglex += step*6;
		}
		if ( skewDown ) {
			if(anglex > -21)
				anglex -= step*6;
		}
		if ( rotateLeft ) {
			angley += step*6;
		}
		if ( rotateRight ) {
			angley -= step*6;
		}
	}
	
	public Rectangle getTarget() {
		return target;
	}
	public void setTarget(Rectangle target) {
		this.target = target;
	}
	public void reshape(int x, int y, int width, int height) {
		this.target = new Rectangle(x, y, width, height);
		
	}

	// raypicking
	
//	public Vector4d calcPickRay( int mouseX, int mouseY ) {
//		
//	}
	Vector3d P1 = new Vector3d( 0,0,0 );
	Vector3d P2 = new Vector3d( 1,1,1 );
	
	public Vector4d calc3DMousePoint( MouseClickedEvent e ) {
		Point p = e.mouseEvent.getPoint();
		DoubleBuffer point1 = DoubleBuffer.allocate(3);
		DoubleBuffer point2 = DoubleBuffer
				.allocate(3);
		boolean p1, p2;
		GLU glu = new GLU(); 
		p1 = glu.gluUnProject(p.getX(), view.get(3) - p.getY() - 1, 0.0, model,
				proj, view, point1);
		p2 = glu.gluUnProject(p.getX(), view.get(3) - p.getY() - 1, 1.0, model,
				proj, view, point2);
		if (!p1 & !p2)
			return null;
		P1 = new Vector3d( point1.array() );
		P2 = new Vector3d( point2.array() );
		
		Vector4d Px1 = new Vector4d( P1.x, P1.y, P1.z, 0 );
		Vector4d Px2 = new Vector4d( P2.x, P2.y, P2.z, 0 );
		// Click Ray
		Vector4d P1P2 = new Vector4d();
		P1P2.sub( Px2, Px1 );

		Vector4d E0 = new Vector4d(0, 0, 0, 1);
		Vector4d EX = new Vector4d(1, 0, 0, 1);
		Vector4d EY = new Vector4d(0, 1, 0, 1);
		// Ebene
		Vector4d E0EX = new Vector4d();
		E0EX.sub( EX, E0 );
		Vector4d E0EY = new Vector4d();
		E0EY.sub( EY, E0 );

		//Vector4d EBENE = VMath.kreuz(E0EX, E0EY);
		Vector4d EBENE = new Vector4d(0, 0, 1, 0);
		// Schnittpunkt
		// 
		// Gerade:
		// P1 + v * P1P2
		// x = P1.x + v * P1P2.x
		// ...

		// Ebene:
		// EBENE.x * x + EBENE.y * y + EBENE.z * z = 0

		// EBENE.x * (P1.x + v * P1P2.x) +
		// EBENE.y * (P1.y + v * P1P2.y) +
		// EBENE.z * (P1.z + v * P1P2.z) = 0

		// EBENE.x * P1.x + EBENE.y * P1.y + EBENE.z * P1.z
		// --------------------------------------------------------- = y
		// - EBENE.x * P1P2.x - EBENE.y * P1P2.y - EBENE.z * P1P2.z

		double y = (EBENE.x * P1.x + EBENE.y * P1.y + EBENE.z * P1.z)
				/ (-EBENE.x * P1P2.x - EBENE.y * P1P2.y - EBENE.z * P1P2.z);

		Vector4d mul = new Vector4d();
		mul.scale(y, P1P2);
		Vector4d ret = new Vector4d();
		ret.add( Px1, mul);
		e.ray = new Vector4d( P1P2 );
		e.rayOrigin = new Point4d( Px1 );
		return ret;
	}
	
	
}
