package vidis.ui.model.impl;

import javax.media.opengl.GL;

import vidis.ui.model.structure.ASimObject;

public class PropertyPanel extends BasicGuiContainer {
//	private static Logger logger = Logger.getLogger(PropertyPanel.class);

	private ASimObject propertySource;
	
	enum State {
		NORMAL,
		MINIMIZED;
	}
	
	private State state;
	
	@Override
	public void renderContainer(GL gl) {
		switch (state) {
		case NORMAL:
			// render the box
			super.renderContainer(gl);
			
			// render the content
			gl.glPushMatrix();
				// FIXME move it to right position
				propertySource.renderObject(gl);
			gl.glPopMatrix();
			break;
		case MINIMIZED:
			break;
		}
	}
}
	
