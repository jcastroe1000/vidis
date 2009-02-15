/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.structure;

import java.awt.Font;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;
import javax.vecmath.Point2i;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.mvc.api.Dispatcher;
import vidis.ui.vis.camera.GuiCamera;
import vidis.util.ResourceManager;

import com.sun.opengl.util.j2d.TextRenderer;


public abstract class AGuiContainer extends AEventHandler implements IGuiContainer {

	private static Logger logger = Logger.getLogger( AGuiContainer.class );	
	
	
	protected static TextRenderer textRenderer;
	
	protected ILayout layout = null;
	private double height;
	private double width;
	private double x;
	private double y;
	
	/**
	 * only the first object in the hierarchy will get the scissor test
	 * it will be disabled for all its child's
	 */
	private boolean useScissorTest = false;
	private boolean useScissorTestNow = false;
	/**
	 * is used to determine if this object has gained the scissortest ( also see {@link #useScissorTest} )
	 * @return
	 */
	public boolean isUseScissorTestNow() {
		return useScissorTestNow;
	}
	
	protected static double textH;
	
	/**
	 * needs to be executed in a valid gl context
	 */
	protected static void initTextRenderer() {
		textRenderer = new TextRenderer(new Font(ResourceManager.FONT_VERDANA, Font.BOLD, 36), true);
		textH = textRenderer.getBounds( "pb[{" ).getHeight();
	}
	
	protected void requireTextRenderer() {
		if ( textRenderer == null ) {
			try {
				initTextRenderer();
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
		Set<IGuiContainer> copy = null;
		synchronized ( childs ) {
			copy = new HashSet<IGuiContainer>( childs );
		}
		return copy;
	}
	public IGuiContainer getParent() {
		return parent;
	}
	public void addChild( IGuiContainer c ) {
		synchronized ( childs ) {
			this.childs.add( c );
			c.setParent( this );
			if (c.getLayout() != null ) {
				c.getLayout().layout();
			}
		}
	}
	public void removeAllChilds() {
		synchronized ( childs ) {
			this.childs.clear();
		}
	}
	
	public void removeChild(IGuiContainer c) {
		c.setParent( null );
		synchronized ( childs ) {
			this.childs.remove( c );
		}
	}
	public void setParent( IGuiContainer c ) {
		this.parent = c;
	}
	// -----
	
	public void render(GL gl) {
		renderBox(gl, 0);
	}
	
	protected double myZ;
	public void renderBox(GL gl, double z) {
		myZ = z;
		gl.glPushMatrix();
		gl.glTranslated(getX(), getY(), z);
		renderContainer( gl );
		gl.glPushMatrix();
			try {
				
				useScissorTestNow = !gl.glIsEnabled(GL.GL_SCISSOR_TEST) && useScissorTest;
				if ( useScissorTestNow ) {
//					logger.info( "using scissortest...");
					Point2i pos = GuiCamera.convert3Dto2D( new Point2d( getAbsoluteX(), getAbsoluteY() ));
					Point2i hw = GuiCamera.convert3Dto2D( new Point2d( getWidth(), getHeight() ));
//					logger.info( "from " + pos + " to "+ hw );
					// set the scissor
					gl.glScissor( pos.x, pos.y, hw.x, hw.y );
					// draw the content
					gl.glEnable( GL.GL_SCISSOR_TEST );
				}
				// render them
				synchronized ( childs ) {
					for ( IGuiContainer c : childs ) {
						if ( c.isVisible() ) {
							c.renderBox(gl, z + IGuiContainer.Z_OFFSET);
						}
					}
				}
				if ( useScissorTestNow ) {
					gl.glDisable( GL.GL_SCISSOR_TEST );
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
		case IVidisEvent.MouseMovedEvent_GUI:
			handleMouseMovedEvent( (AMouseEvent) e );
			break;
		case IVidisEvent.MousePressedEvent_GUI:
		case IVidisEvent.MouseReleasedEvent_GUI:
			handleMouseEvent( (AMouseEvent) e );
			break;
		}
		
		if (forward)
			synchronized ( childs ) {
				for (IGuiContainer c : childs) {
					c.fireEvent( e );
				}
			}
	}
	
//	private boolean mouseInContainerOld = false;
//	private boolean mouseInContainerNew = false;
	
	private static Set<IGuiContainer> underMouse = new HashSet<IGuiContainer>();
	
	private void handleMouseMovedEvent( AMouseEvent e ) {
		if ( this.isVisible() ) {
			// we need to check absolute mouse moved coordinates
			// to be sure that every element receives its event
			Point2d point = e.guiCoords;
			double myX = getAbsoluteX();
			double myY = getAbsoluteY();
			// cleanup
			Set<IGuiContainer> toRemove = new HashSet<IGuiContainer>();
			for ( IGuiContainer g : underMouse ) {
				if ( !g.isVisible() ) {
					toRemove.add( g );
				}
			}
			underMouse.removeAll( toRemove );
			if ( isPointWithinRect( point, myX, myY, getWidth(), getHeight() ) ) {
				if ( ! underMouse.contains(this) ) {
					underMouse.add( this );
					onMouseEnter( e );
				}
			}
			else {
				if ( underMouse.contains( this ) ) {
					underMouse.remove( this );
					onMouseExit( e );
				}
			}
			// forward to all childs
			synchronized ( childs ) {
				for ( IGuiContainer c : childs ) {
					if ( c.isVisible() ) {
						c.fireEvent( e );
					}
				}
			}
			// forward to 3d
			if ( parent == null && underMouse.contains( this ) && underMouse.size() == 1 ) {
				AMouseEvent nextEvent = new AMouseEvent( e.getID() + 10, e.mouseEvent );
				Dispatcher.forwardEvent( nextEvent );
			}
		}
	}
	
	protected abstract void onMouseEnter( AMouseEvent e );
	protected abstract void onMouseExit( AMouseEvent e );
	
	protected abstract void onMousePressed( AMouseEvent e );
	protected abstract void onMouseReleased( AMouseEvent e );
	protected abstract void onMouseClicked( AMouseEvent e );
	
	
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
		case IVidisEvent.MousePressedEvent_GUI:
			onMousePressed( e );
			break;
		case IVidisEvent.MouseReleasedEvent_GUI:
			onMouseReleased( e );
			onMouseClicked( e );
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
					List<IGuiContainer> toFire = new ArrayList<IGuiContainer>();
					synchronized ( childs ) {
						for ( IGuiContainer c : childs ) {
							if ( c.isPointInContainer( point ) ) {
								if ( c.isVisible() ) {
									toFire.add( c );
									childFound = true;
								}
							}
						}
					}
					for ( IGuiContainer c : toFire ) {
						c.fireEvent(e);
					}
				} catch ( ConcurrentModificationException ex) {
					logger.error("I introduced this bug with adding elements within onClick events.", ex);
				}
				
				if ( ! childFound ) {
					if ( parent == null ) {
						AMouseEvent nextEvent = new AMouseEvent( e.getID() + 10, e.mouseEvent );
						Dispatcher.forwardEvent( nextEvent );
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

	public void setUseScissorTest(boolean useScissorTest) {
		this.useScissorTest = useScissorTest;
	}

	public boolean isUseScissorTest() {
		return useScissorTest;
	}
	
	private boolean visible = true;

	public void setVisible( boolean visible ) {
		this.visible = visible;
		synchronized ( childs ) {
			for ( IGuiContainer c : childs ) {
				c.setVisible( visible );
			}
		}
	}
	
	public boolean isVisible() {
		return this.visible;
	}
	
}
