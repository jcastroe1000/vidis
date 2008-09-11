package vidis.ui.model.structure;

import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.events.AEventHandler;
import vidis.util.ResourceManager;

import com.sun.opengl.util.j2d.TextRenderer;

public abstract class ASimObject extends AEventHandler implements ISimObject {

	private static Logger logger = Logger.getLogger( ASimObject.class );
	
	protected static TextRenderer textRenderer;
	
	
	private IVariableContainer obj; // FIXME
	private IGuiContainer guiObj; // FIXME
	
	public ASimObject( IVariableContainer c ) {
		obj = c;
	}
	
	protected AVariable getVariableById( String id ) {
		return obj.getVariableById( id );
	}
	
	protected Set<String> getVariableIds() {
		return obj.getVariableIds();
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
	
	public void render( GL gl ) {
		gl.glPushMatrix();
		try {
			Tuple3d pos = (Tuple3d) obj.getVariableById( AVariable.COMMON_IDENTIFIERS.POSITION ).getData();
			gl.glTranslated(pos.x, pos.y, pos.z);
		}
		catch ( Exception e ) {
			logger.error("error getting pos variable of "+obj +"  ", e);
		}
		try {
			renderObject(gl);
		}
		catch ( Exception e ) {
			logger.error( null, e );
		}
		gl.glPopMatrix();
	}
	
	public abstract void renderObject( GL gl );
	
	protected IVariableContainer getVariableContainer() {
		return obj;
	}

}
