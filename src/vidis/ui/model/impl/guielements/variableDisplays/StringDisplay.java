package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.data.var.vars.FieldVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.ui.model.impl.Label;

public class StringDisplay extends Label {
	private static Logger logger = Logger.getLogger(StringDisplay.class);

	private AVariable var;
	
	private StringDisplay ( AVariable v) {
		this.var = v;
		this.setText( "Label" );
		this.setTextColor( Color.red );
	}
	
	public StringDisplay( DefaultVariable v) {
		this ( (AVariable)v );
//		System.err.println("defvar: " + v.getIdentifier());
	}
	
	public StringDisplay( MethodVariable v) {
		this ( (AVariable)v );
//		System.err.println("methvar: " + v.getIdentifier());
	}
	
	public StringDisplay( FieldVariable v) {
		this ( (AVariable)v );
//		System.err.println("fieldvar: " + v.getIdentifier());
	}
	

	
	
	@Override
	public void renderContainer(GL gl) {
		String txt = var.getIdentifier().replaceAll(var.getNameSpace(), "   ") + " -> " + var.getData().toString();
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
