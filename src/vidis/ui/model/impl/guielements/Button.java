package vidis.ui.model.impl.guielements;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

public class Button extends Label {
	private static Logger logger = Logger.getLogger(Button.class);
	public Button(String label) {
		super(label);
	}
	@Override
	public void renderContainer(GL gl) {
		gl.glPushMatrix();
			// render box (auﬂen ummer)
			// render internal label
			super.renderContainer(gl);
		gl.glPopMatrix();
	}
}