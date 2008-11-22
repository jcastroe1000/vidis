package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple2i;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;

public class Tuple2Display extends Display {
	private static Logger logger = Logger.getLogger(Tuple2Display.class);

	public Tuple2Display ( AVariable v ) {
		super(v);
		this.setText( "Label" );
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new Tuple2Display( var );
//	}
	
	private String convertUnknownTupleToString() {
		Object tuple = var.getData();
		if ( tuple instanceof Tuple2d ) {
			return ((Tuple2d)tuple).toString();
		} else if ( tuple instanceof Tuple2f ) {
			return ((Tuple2f)tuple).toString();
		} else if ( tuple instanceof Tuple2i ) {
			return ((Tuple2i)tuple).toString();
		} else {
			return "ERROR";
		}
	}

	
	
	@Override
	public void renderContainer(GL gl) {
		String txt = var.getIdentifier().replaceAll(var.getNameSpace()+".", "   ") + " [T]-> " + convertUnknownTupleToString();
		this.setText(txt);
		super.renderContainer(gl);
	}
//	@Override
//	public void renderContainer(GL gl) {
//		String txt = var.getIdentifier() + " -> " + var.getData().toString();
//		double scale = 0.01;
//		height = textH * scale;
//		double hoffset = height / 2d;
//		gl.glPushMatrix();
//			gl.glTranslated(0, 0, 0);
//			gl.glScaled( scale, scale, scale );
//			textRenderer.begin3DRendering();
//			textRenderer.setUseVertexArrays(false);
//			textRenderer.draw3D( txt, 0f, 0f, 0f, 1f );
//			textRenderer.end3DRendering();
//		gl.glPopMatrix();
//		//super.renderContainer(gl);
//	}
}
