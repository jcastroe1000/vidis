package vidis.ui.vis.camera;

import java.awt.Rectangle;
import java.nio.DoubleBuffer;
import java.nio.IntBuffer;

import javax.media.opengl.GL;
import javax.media.opengl.glu.GLU;
import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.AMouseEvent;
import vidis.ui.events.DummyEvent;
import vidis.ui.events.GuiMouseEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.gui.Gui;

public class GuiCamera extends AEventHandler implements ICamera {
	
	private static Logger logger = Logger.getLogger( GuiCamera.class );	
	
	/* render target */
	private Rectangle target = new Rectangle( 100, 100, 100, 100 ); // dummy init to prevent error
	private double left;
	private double right;
	private double top;
	private double bottom;
	private Gui gui;
	/* Position of the camera */
	private double posx;
	private double posy;
	private double step = 0.1; // Scrollspeed
	/* Zoom */
	private double zoom;
	/**
	 * used for calc3DMousePoint
	 */
	private DoubleBuffer model = DoubleBuffer.allocate(16), proj = DoubleBuffer
			.allocate(16);
	private IntBuffer view = IntBuffer.allocate(4);
	
	public GuiCamera( Gui gui ){
		this.gui = gui;
		this.posx = 0;
		this.posy = 0;
		this.zoom = 1.0;
	}
	public void init(GL gl) {
		gl.glClear( GL.GL_DEPTH_BUFFER_BIT );
		gl.glEnable( GL.GL_BLEND );
		gl.glDisable( GL.GL_LIGHTING );
		gl.glBlendFunc( GL.GL_SRC_ALPHA, GL.GL_ONE_MINUS_SRC_ALPHA );
		gl.glViewport((int)target.getX(), (int)target.getY(), (int)target.getWidth(), (int)target.getHeight());
		
	}
	public void applyProjectionMatrix(GL gl) {
		GLU glu = new GLU();
	
		glu.gluOrtho2D( left, right, top, bottom );
//		System.out.println("aspect="+aspect+", gluOrtho2d( "+left+", "+right+", "+bottom+", "+top+" )");
	}
	public void applyViewMatrix(GL gl) {
//		gl.glTranslated(posx, -posy +zoom/2, zoom);
		
		// matrizen auslesen f�r click berechnung
		gl.glGetDoublev(GL.GL_MODELVIEW_MATRIX, model);
		gl.glGetDoublev(GL.GL_PROJECTION_MATRIX, proj);
		gl.glGetIntegerv(GL.GL_VIEWPORT, view);
	}
	public String toString() {
		return "GuiCamera at ("+posx+", "+posy+") Zoom: "+zoom;
	}
	public void setPosition(double posx, double posy){
		this.posx = posx;
		this.posy = posy;
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
		return posy;
	}
	public void setPosy(double posy) {
		this.posy = posy;
	}
	
	
		
	
	public Rectangle getTarget() {
		return target;
	}
	public void setTarget(Rectangle target) {
		this.target = target;
	}
	public void reshape(int x, int y, int width, int height) {
		

		this.target = new Rectangle(x, y, width, height);
		// FIXME works for now
		double aspect = target.getWidth() / (double) target.getHeight();
		double dwidth = width / 10d;
		double dheight = height / 10d;
		left = -dwidth / 2d;
		right = dwidth / 2d;
		top = -dheight / 2d;
		bottom = dheight /2d;
		
//		System.out.println("bounds: x="+left+", y="+top+", height="+dheight+", width="+dwidth);
		gui.updateBounds(left, top, dheight, dwidth);
		gui.fireEvent( new DummyEvent( IVidisEvent.GuiResizeEvent ) );
		
	}
	public void handleMouseClickedEvent( AMouseEvent e ) {
		logger.debug("handleMouseClickedEvent() id="+e.getID());
		GuiMouseEvent m = new GuiMouseEvent();
		m.where = convert2Dto3D(e.mouseEvent.getX(), e.mouseEvent.getY());
		gui.fireEvent( m );
	}
	
	private Point2d convert2Dto3D( int x, int y ) {
		double xrel = (double) x / (double) target.getWidth();
		double yrel = (double) y / (double) target.getHeight();
		
		double xout = xrel * target.getWidth() / 2d;
		double yout = yrel * target.getHeight() / 2d;
		
		return new Point2d( x / 10d, y / 10d);
	}
	
	public void update() {
		// nothing to update - i think
		
	}
	
	public void handleEvent(IVidisEvent event) {
		switch (event.getID()) {
		case IVidisEvent.MouseClickedEvent:
			handleMouseClickedEvent( (MouseClickedEvent) event);
			break;
		}
	}
	
/*
	public Vector4d calc3DMousePoint(Point p) {
		DoubleBuffer point1 = DoubleBuffer.allocate(3), point2 = DoubleBuffer
				.allocate(3);
		boolean p1, p2;
		GLU glu = new GLU();
		p1 = glu.gluUnProject(p.getX(), view.get(3) - p.getY() - 1, 0.0, model,
				proj, view, point1);
		p2 = glu.gluUnProject(p.getX(), view.get(3) - p.getY() - 1, 1.0, model,
				proj, view, point2);
		if (!p1 & !p2)
			return null;
		Vector4d P1 = Vector4d.fromDoubleBuffer(point1, 1);
		Vector4d P2 = Vector4d.fromDoubleBuffer(point2, 1);
		// Click Ray
		Vector4d P1P2 = VMath.minus(P2, P1);

		Vector4d E0 = new Vector4d(0, 0, 0, 1);
		Vector4d EX = new Vector4d(1, 0, 0, 1);
		Vector4d EY = new Vector4d(0, 1, 0, 1);
		// Ebene
		Vector4d E0EX = VMath.minus(EX, E0);
		Vector4d E0EY = VMath.minus(EY, E0);

		Vector4d EBENE = VMath.kreuz(E0EX, E0EY);
		EBENE = new Vector4d(0, 0, 1, 0);
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

		return VMath.add(P1, VMath.mul(y, P1P2));
	}
	*/
}
