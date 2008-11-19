package vidis.ui.model.structure;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.AMouseEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MouseMovedEvent;
import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

import com.sun.opengl.util.j2d.TextRenderer;


public abstract class AGuiContainer extends AEventHandler implements IGuiContainer {

	private static Logger logger = Logger.getLogger( AGuiContainer.class );	
	
	
	protected TextRenderer textRenderer;
	
	protected ILayout layout = null;
	private double height;
	private double width;
	private double x;
	private double y;
	protected double textH;
	
	protected void requireTextRenderer() {
		if ( textRenderer == null ) {
			try {
				textRenderer = new TextRenderer( ResourceManager.getFont( ResourceManager.FONT_ARIAL, 130 ) );
				textH = textRenderer.getBounds("pb[{").getHeight();
			}
			catch ( Exception e ) {
				logger.error( "error initializing TextRenderer", e );
				System.err.println(e);
			}
		}
	}
	
	public void setLayout( ILayout layout ) {
		this.layout = layout;
		if ( layout != null ) {
			this.layout.setGuiContainer( this );
		}
	}
	public ILayout getLayout() {
		return layout;
	}
	
	public void setBounds(double x, double y, double height, double width) {
		this.height = height;
		this.width = width;
		this.x = x;
		this.y = y;
	}
	
	// childs & parent
	private Set<IGuiContainer> childs = new HashSet<IGuiContainer>();
	private IGuiContainer parent;
	
	public Set<IGuiContainer> getChilds() {
		return childs;
	}
	public IGuiContainer getParent() {
		return parent;
	}
	public void addChild( IGuiContainer c ) {
		this.childs.add( c );
		c.setParent( this );
		if (c.getLayout() != null ) {
			c.getLayout().layout();
		}
	}
	public void removeAllChilds() {
		this.childs.clear();
	}
	
	public void removeChild(IGuiContainer c) {
		c.setParent( null );
		this.childs.remove( c );
	}
	public void setParent( IGuiContainer c ) {
		this.parent = c;
	}
	// -----
	
	public void render(GL gl) {
		renderBox(gl, 0);
	}
	
	public void renderBox(GL gl, double z) {
		gl.glPushMatrix();
		gl.glTranslated(getX(), getY(), z);
		renderContainer( gl );
		gl.glPushMatrix();
			try {
				// render them
				for ( IGuiContainer c : childs ) {
					if ( c.isVisible() ) {
						c.renderBox(gl, z + IGuiContainer.Z_OFFSET);
					}
				}
			} catch (ConcurrentModificationException e) {
				// well, may happen but is not that severe
				logger.warn( e );
			}
		gl.glPopMatrix();
		
//		Maus debugging:
//		gl.glPushMatrix();
//			GLUT glut = new GLUT();
//			gl.glTranslated(drawme.x, drawme.y, 0);
//			glut.glutWireSphere(1, 4, 4);
//		gl.glPopMatrix();
		gl.glPopMatrix();
	}
	
	public abstract void renderContainer( GL gl );
	
	protected void handleResize() {
		if ( layout != null ) {
			layout.layout();
		}
	}
	protected void handleEvent( IVidisEvent e ) {
		boolean forward = false;
		switch ( e.getID() ) {
		case IVidisEvent.GuiResizeEvent:
			handleResize();
			forward = true;
			break;
//		case IVidisEvent.GuiMouseEvent:
//			handleMouseEvent( (GuiMouseEvent) e );
//			break;
		case IVidisEvent.MouseMovedEvent:
			handleMouseMovedEvent( (MouseMovedEvent) e );
			break;
		case IVidisEvent.MouseClickedEvent:
		case IVidisEvent.MousePressedEvent:
		case IVidisEvent.MouseReleasedEvent:
			handleMouseEvent( (AMouseEvent) e );
			break;
		}
		
		
		if (forward)
		for (IGuiContainer c : childs) {
			if ( c.isVisible() ) {
				c.fireEvent( e );
			}
		}
	}
	
//	private boolean mouseInContainerOld = false;
//	private boolean mouseInContainerNew = false;
	
	private static Set<IGuiContainer> underMouse = new HashSet<IGuiContainer>();
	
	private void handleMouseMovedEvent( MouseMovedEvent e ) {
		if ( this.isVisible() ) {
			// we need to check absolute mouse moved coordinates
			// to be sure that every element receives its event
			Point2d point = e.guiCoords;
			double myX = getAbsoluteX();
			double myY = getAbsoluteY();
			if ( isPointWithinRect( point, myX, myY, getWidth(), getHeight() ) ) {
				if ( ! underMouse.contains(this) ) {
					underMouse.add( this );
					onMouseEnter();
				}
			}
			else {
				if ( underMouse.contains( this ) ) {
					underMouse.remove( this );
					onMouseExit();
				}
			}
			// forward to all childs
			for ( IGuiContainer c : childs ) {
				if ( c.isVisible() ) {
					c.fireEvent( e );
				}
			}
			
			if ( parent == null && underMouse.contains( this ) && underMouse.size() == 1 ) {
				e.forwardTo3D = true;
				Dispatcher.forwardEvent( e );
			}
		}
	}
	
	protected abstract void onMouseEnter();
	protected abstract void onMouseExit();
	
	protected abstract void onMousePressed( MousePressedEvent e );
	protected abstract void onMouseReleased( MouseReleasedEvent e );
	protected abstract void onMouseClicked( MouseClickedEvent e );
	
	
	/**
	 */
	public boolean isPointInContainer( Point2d p ) {
		double myX = getAbsoluteX();
		double myY = getAbsoluteY();
		return isPointWithinRect( p, myX, myY, getWidth(), getHeight() );
	}
	
	/**
	 * checks whether a given point p is within the specified rect
	 * @return true if p is in the rect; false otherwise
	 */
	public boolean isPointWithinRect( Point2d p, double rX, double rY, double rW, double rH ) {
		if ( 	rX < p.x &&
				rY < p.y &&
				rX + rW > p.x &&
				rY + rH > p.y ) {
			return true;
		}
		else {
			return false;
		}
	}

	private void dispatchMouseEvent( AMouseEvent e ) {
		logger.info("dispatchMouseEvent() " + e + ", " + this);
		switch ( e.getID() ) {
		case IVidisEvent.MouseClickedEvent:
			onMouseClicked( (MouseClickedEvent) e );
			break;
		case IVidisEvent.MousePressedEvent:
			onMousePressed( (MousePressedEvent) e );
			break;
		case IVidisEvent.MouseReleasedEvent:
			onMouseReleased( (MouseReleasedEvent) e );
			break;
		}
	}
	
	private Point2d drawme = new Point2d(0,0);
	
	protected void handleMouseEvent( AMouseEvent e ){
		logger.info("handleMouseEvent() "+ e + ", " + this);
		
		if ( this.isVisible() ) {
			Point2d point = e.guiCoords;
			double myX = getAbsoluteX();
			double myY = getAbsoluteY();
			if ( isPointWithinRect( point, myX, myY, getWidth(), getHeight() ) ) {
				// forward to childs
				boolean childFound = false;
				try {
					for ( IGuiContainer c : childs ) {
						if ( c.isPointInContainer( point ) ) {
							if ( c.isVisible() ) {
								c.fireEvent( e );
								childFound = true;
							}
						}
					}
				} catch ( ConcurrentModificationException ex) {
					logger.error("I introduced this bug with adding elements within onClick events.", ex);
				}
				
				if ( ! childFound ) {
					if ( parent == null ) {
						e.forwardTo3D = true;
						Dispatcher.forwardEvent( e );
					}
					dispatchMouseEvent( e );
				}
			}
		}
	}
	
	public double getHeight() {
		if ( layout == null ) {
			return this.height;
		}
		else {
			return layout.getHeight();
		}
	}
	
	public double getWantedHeight() {
		return this.height;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public double getWidth() {
		if ( layout == null ) {
			return width;
		}
		else {
			return layout.getWidth();
		}
	}

	public void setWidth(double width) {
		this.width = width;
	}
	
	public double getX() {
		if ( layout == null ) {
			return x;
		}
		else {
			return layout.getX();
		}
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		if ( layout == null ) {
			return y;
		}
		else {
			return layout.getY();
		}
	}

	public void setY(double y) {
		this.y = y;
	}
	
	public double getAbsoluteX() {
		if ( parent == null ) {
			return 0;
		}
		else {
			return parent.getAbsoluteX() + this.getX();
		}
	}
	
	public double getAbsoluteY() {
		if ( parent == null ) {
			return 0;
		}
		else {
			return parent.getAbsoluteY() + this.getY();
		}
	}
	
	public boolean isTextRenderable() {
		return false;
	}
}
