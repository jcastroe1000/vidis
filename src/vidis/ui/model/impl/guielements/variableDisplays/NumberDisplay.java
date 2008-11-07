package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.util.Rounding;

public class NumberDisplay extends Display {
	private static Logger logger = Logger.getLogger(NumberDisplay.class);

	public NumberDisplay ( AVariable v) {
		super(v);
		this.setText( "Label" );
		this.setTextColor( Color.red );
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new NumberDisplay( var );
//	}
//	
	
	@Override
	public void renderContainer(GL gl) {
		if ( var != null ) {
			String txt = "   " + AVariable.getIdentifierWithoutNamespace(var.getIdentifier()) + " -> ";
			Object num = var.getData();
			if(Byte.class.isAssignableFrom(num.getClass())) {
				txt += (Byte)num;
			} else if (Integer.class.isAssignableFrom(num.getClass())) {
				txt += (Integer)num;
			} else if (Float.class.isAssignableFrom(num.getClass())) {
				txt += Rounding.round((Float)num,3);
			} else if (Double.class.isAssignableFrom(num.getClass())) {
				txt += Rounding.round((Double)num,3);
			} else if (Long.class.isAssignableFrom(num.getClass())) {
				txt += (Long)num;
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
