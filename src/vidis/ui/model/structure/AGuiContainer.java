package vidis.ui.model.structure;

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.AMouseEvent;
import vidis.ui.events.GuiMouseEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MouseMovedEvent;
import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;
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
	
	protected void requireTextRenderer() {
		if ( textRenderer == null ) {
			try {
				textRenderer = new TextRenderer( ResourceManager.getFont( ResourceManager.FONT_ARIAL, 130 ) );
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
			for ( IGuiContainer c : childs ) {
				c.renderBox(gl, z + IGuiContainer.Z_OFFSET);
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
			forward = true;
			break;
		case IVidisEvent.MouseClickedEvent:
		case IVidisEvent.MousePressedEvent:
		case IVidisEvent.MouseReleasedEvent:
			handleMouseEvent( (AMouseEvent) e );
			break;
		}
		
		
		if (forward)
		for (IGuiContainer c : childs) {
			c.fireEvent( e );
		}
	}
	
	private boolean mouseInContainerOld = false;
	private boolean mouseInContainerNew = false;
	private void handleMouseMovedEvent(MouseMovedEvent e) {
		Point2d where = e.guiCoords;
		// copy old value
		mouseInContainerOld = mouseInContainerNew;
		if ( isPointInContainer( where ) ) {
			mouseInContainerNew = true;
		}
		else {
			mouseInContainerNew = false;
		}
		if ( mouseInContainerNew && !mouseInContainerOld ) {
			// ON MOUSE ENTER
			onMouseEnter();
		}
		else if ( !mouseInContainerNew && mouseInContainerOld ) {
			// ON MOUSE EXIT
			onMouseExit();
		}
		
	}
	
	protected abstract void onMouseEnter();
	protected abstract void onMouseExit();
	
	protected abstract void onMousePressed( MousePressedEvent e );
	protected abstract void onMouseReleased( MouseReleasedEvent e );
	protected abstract void onMouseClicked( MouseClickedEvent e );
	
	public boolean isPointInContainer( Point2d p ) {
		if ( 	this.getX() < p.x &&
				this.getY() < p.y &&
				this.getX() + this.getWidth() > p.x &&
				this.getY() + this.getHeight() > p.y ) {
			return true;
		}
		else {
			return false;
		}
	}

	private Point2d drawme = new Point2d(0,0);
	protected void handleMouseEvent( AMouseEvent e ){
		logger.debug("handleMouseClickedEvent() "+ e);
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
		drawme = e.guiCoords;
		for ( IGuiContainer c : childs) {
			if ( c.getX() < e.guiCoords.x &&
					c.getY() < e.guiCoords.y &&
					c.getX() + c.getWidth() > e.guiCoords.x &&
					c.getY() + c.getHeight() > e.guiCoords.y ) {
				logger.debug("  found one..");
				e.guiCoordsRelative = new Point2d(e.guiCoords.x - c.getX(), e.guiCoords.y - c.getY());
				c.fireEvent( e );
			}
		}
	}
	
	// -----
	
	
	public double getHeight() {
		if ( layout == null ) {
			return this.height;
		}
		else {
			return layout.getHeight();
		}
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
}
