package vidis.ui.model.structure;

import java.util.Set;

import javax.media.opengl.GL;
import javax.vecmath.Tuple3d;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.events.AEventHandler;

public abstract class ASimObject extends AEventHandler implements ISimObject {

	private static Logger logger = Logger.getLogger( ASimObject.class );
	
	
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

}
