package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MousePressedEvent;
import vidis.ui.events.MouseReleasedEvent;

public class Button extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Button.class);

	float borderPercent = 0.05f;
	
	private String text = "BUTTON";
	
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	@Override
	public void renderContainer(GL gl) {
		double h = getHeight();
		double w = getWidth();
		double border = h * borderPercent;
		// text
		
		requireTextRenderer();
		
		gl.glPushMatrix();
			
			gl.glTranslated(0, h / 2d, 0);
			gl.glScaled(0.01, 0.01, 0.01);
			textRenderer.begin3DRendering();
			textRenderer.draw3D(text, 0, 0, 1, 1f);
			textRenderer.end3DRendering();
		gl.glPopMatrix();
		
		
		// center
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, getHeight() - border);
			gl.glVertex2d( getWidth() - border, getHeight() - border );
			gl.glVertex2d( getWidth() - border, border);	
		gl.glEnd();
		
		// rand
		useColor(gl, colorLight);

		//  bottom
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, h );
			gl.glVertex2d( border, h - border);
			gl.glVertex2d( w - border, h - border );
			gl.glVertex2d( w, h );	
		gl.glEnd();
		//  left
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, 0);
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, h - border );
			gl.glVertex2d( 0, h );	
		gl.glEnd();
		
		useColor(gl, colorDark);
		//  top
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( 0, 0);
			gl.glVertex2d( w, 0 );
			gl.glVertex2d( w - border, border );
			gl.glVertex2d( border, border );	
		gl.glEnd();
	//  right
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( w, 0);
			gl.glVertex2d( w, h);
			gl.glVertex2d( w - border, h - border );
			gl.glVertex2d( w - border, border );	
		gl.glEnd();
	}
	
	private void useColor( GL gl, Color c ) {
		gl.glColor4d(c.getRed()/255d, c.getGreen()/255d, c.getBlue()/255d, c.getAlpha()/255d);
	}
	
	@Override
	protected void onMousePressed(MousePressedEvent e) {
		Color m = colorLight;
		colorLight = colorDark;
		colorDark = m;
	}
	@Override
	protected void onMouseReleased(MouseReleasedEvent e) {
		Color m = colorLight;
		colorLight = colorDark;
		colorDark = m;
	}
}
