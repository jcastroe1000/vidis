package vidis.ui.model.structure;

import java.awt.Color;
import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.config.Configuration;
import vidis.ui.events.AEventHandler;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

import com.sun.opengl.util.j2d.TextRenderer;

public abstract class ASimObject extends AEventHandler implements ISimObject {

	private static Logger logger = Logger.getLogger( ASimObject.class );
	
	protected static TextRenderer textRenderer;
	
	
	private IVariableContainer obj;
	protected IGuiContainer guiObj; 
	
	public ASimObject( IVariableContainer c ) {
		obj = c;
	}
	
	protected AVariable getVariableById( String id ) {
		return obj.getVariableById( id );
	}
	
	protected Set<String> getVariableIds() {
		return obj.getVariableIds();
	}
	
	protected final void setColor( GL gl, Color c ) {
		gl.glColor4d( c.getRed() / 255d, c.getGreen() / 255d, c.getBlue() / 255d, c.getAlpha() / 255d );
	}
	
	protected void requireTextRenderer() {
		if ( textRenderer == null ) {
			try {
				textRenderer = new TextRenderer( ResourceManager.getFont( ResourceManager.FONT_ARIAL, 130 ) );
			}
			catch ( Exception e ) {
				logger.error( "error initializing TextRenderer", e );
			}
		}
	}
	
	protected static Point3d calculateMiddle(Point3d a, Point3d b, double heightFactor) {
		double distance = a.distance( b );
		Vector3d h = new Vector3d( 0.0, heightFactor, 0.0 );
		h.scale( distance/2.0 );
		Point3d pointM = new Point3d( a ); 
		pointM.interpolate( b, .5 );
		pointM.add( h );
		return pointM;
	}
	
	public Point3d getPosition() {
		return new Point3d( (Tuple3d)getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData() );
	}
	
	public void render( GL gl ) {
		gl.glPushMatrix();
		try {
			if(obj.hasVariable(AVariable.COMMON_IDENTIFIERS.POSITION)) {
				Tuple3d pos = (Tuple3d) obj.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
				gl.glTranslated(pos.x, pos.y, pos.z);
			}
		}
		catch ( Exception e ) {
			logger.error("error getting pos variable of "+obj +"  ", e);
		}
		try {
			renderObject(gl);
			
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );
			renderObjectText(gl);
			if(Configuration.DISPLAY_WIREFRAME)
				gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_LINE );
		}
		catch ( Exception e ) {
			logger.error( null, e );
		}
		gl.glPopMatrix();
	}
	
	public abstract void renderObject( GL gl );
	public abstract void renderObjectText( GL gl );
	
	protected IVariableContainer getVariableContainer() {
		return obj;
	}

	/**
	 * TEMP FUNCTION <DELME>
	 */
	public void hit() {
		logger.info( this + " GOT HIT * " + guiObj );
		// send gui show guiObj event
		if ( guiObj != null ) {
			VidisEvent<IGuiContainer> next = new VidisEvent<IGuiContainer>( IVidisEvent.ShowGuiContainer, guiObj );
			Dispatcher.forwardEvent( next );
		}
	}
	
	public abstract void onMouseIn();
	public abstract void onMouseOut();

	public abstract double getHitRadius();
	
}
