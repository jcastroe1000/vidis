package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;

public class MapDisplay extends Display {
	private static Logger logger = Logger.getLogger(MapDisplay.class);
	
	public MapDisplay ( AVariable v ) {
		super(v);
		this.setText( "Label" );
		this.setTextColor( Color.red );
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new MapDisplay( var );
//	}
//	
	
	@Override
	public void renderContainer(GL gl) {
		if ( var != null ) {
			String txt = var.getIdentifier().replaceAll(var.getNameSpace()+".", "   ") + " -> " + var.getData().toString();
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
