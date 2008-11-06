package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.util.Rounding;

public class PrimitiveFloatDisplay extends Display {
	private static Logger logger = Logger.getLogger(PrimitiveFloatDisplay.class);

	public PrimitiveFloatDisplay() {
		// dummy constructor for registering
	}
	
	private PrimitiveFloatDisplay ( AVariable v) {
		this.var = v;
		this.setText( "Label" );
		this.setTextColor( Color.red );
	}
	
	@Override
	public Display newInstance( AVariable var ) {
		return new PrimitiveFloatDisplay( var );
	}
	
	
	@Override
	public void renderContainer(GL gl) {
		if ( var != null ) {
			String txt = "   " + AVariable.getIdentifierWithoutNamespace(var.getIdentifier()) + " -> ";
			Object num = var.getData();
			if (Float.class.isAssignableFrom(num.getClass()) || num.getClass().equals(Float.TYPE)) {
				txt += Rounding.round((Float)num, 3);
			} else {
				txt += "NAN";
			}
			//var.getData().toString();
			this.setText(txt);
			super.renderContainer(gl);
		}
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
