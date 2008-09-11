package vidis.ui.model.impl.guielements;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;

public class Label extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Label.class);
	
	private String label;
	
	public Label(String label) {
		requireTextRenderer();
		setLabel(label);
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	@Override
	public void renderContainer(GL gl) {
		gl.glPushMatrix();
			//gl.glTranslated(0, 0, textRenderer.getBounds(label).getHeight());
			gl.glScaled(0.01, 0.01, 0.01);
			textRenderer.begin3DRendering();
			textRenderer.draw3D(label, 0, 0, 0, 1f);
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
}
