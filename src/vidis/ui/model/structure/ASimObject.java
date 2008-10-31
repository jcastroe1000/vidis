package vidis.ui.model.structure;

import java.awt.Color;
import java.nio.FloatBuffer;
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
import vidis.util.ColorGenerator;
import vidis.util.ResourceManager;

import com.sun.opengl.util.j2d.TextRenderer;

/**
 * Abstract version of Node, Packet and Link
 * @author Christoph
 *
 */
public abstract class ASimObject extends AEventHandler implements ISimObject {

	private static Logger logger = Logger.getLogger( ASimObject.class );
	
	protected static TextRenderer textRenderer;
	
	private Color color1 = Color.GREEN;
	private Color color2 = Color.BLACK;
	
	public void setColor1( Color color ) {
		this.color1 = color;
	}
	
	public void setColor2( Color color ) {
		this.color2 = color;
	}
	
	public Color getColor1() {
		return this.color1;
	}
	
	public Color getColor2() {
		return this.color2;
	}
	
	public void setColor1( Color color, boolean autoColor2 ) {
		setColor1( color );
		// FIXME set right factors
		setColor2( ColorGenerator.nearByColor(color, 12, 13, -14) );
	}
	
	protected void useMaterial( GL gl ) {
		float[] c1v = { color1.getRed() / 255f, color1.getGreen() / 255f, color1.getBlue() / 255f, color1.getAlpha() / 255f };
		gl.glMaterialfv( GL.GL_FRONT, GL.GL_AMBIENT_AND_DIFFUSE, c1v, 0 );
		float[] c2v = { color2.getRed() / 255f, color2.getGreen() / 255f, color2.getBlue() / 255f, color2.getAlpha() / 255f };
		gl.glMaterialfv( GL.GL_BACK, GL.GL_AMBIENT_AND_DIFFUSE, c2v, 0 );
	}
	
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
	
	protected final void useColor( GL gl, Color c ) {
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
