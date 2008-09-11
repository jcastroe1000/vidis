package vidis.ui.model.structure;

import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import com.sun.opengl.util.j2d.TextRenderer;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.GuiMouseEvent;
import vidis.ui.events.IVidisEvent;
import vidis.util.ResourceManager;


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
		if ( e.getID() == IVidisEvent.GuiResizeEvent ) {
			handleResize();
			forward = true;
		}
		else if ( e.getID() == IVidisEvent.GuiMouseEvent ) {
			handleMouseEvent( (GuiMouseEvent) e );
		}
		
		if (forward)
		for (IGuiContainer c : childs) {
			c.fireEvent( e );
		}
	}
	private Point2d drawme = new Point2d(0,0);
	protected void handleMouseEvent( GuiMouseEvent e ){
		logger.debug("handleMouseClickedEvent() "+ e);
		onClick(e);
		drawme = e.where;
		for ( IGuiContainer c : childs) {
			if ( c.getX() < e.where.x &&
					c.getY() < e.where.y &&
					c.getX() + c.getWidth() > e.where.x &&
					c.getY() + c.getHeight() > e.where.y ) {
				logger.debug("  found one..");
				GuiMouseEvent next = new GuiMouseEvent();
				next.where = new Point2d(e.where.x - c.getX(), e.where.y - c.getY());
				c.fireEvent(next);
			}
		}
	}
	protected void onClick( GuiMouseEvent e ) {
		
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
