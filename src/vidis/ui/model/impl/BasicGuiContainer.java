/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.model.structure.AGuiContainer;
import vidis.util.ColorGenerator;

public class BasicGuiContainer extends AGuiContainer {

	private static Logger logger = Logger.getLogger( BasicGuiContainer.class );	
	
	private boolean opaque = true;
	
	// determines wheter color1 or color2 is used
	private boolean highlighted = false;

	
	public void setOpaque( boolean value ) {
		this.opaque = value;
	}
	
	public boolean isOpaque() {
		return opaque;
	}
	
	protected synchronized Color getColor() {
		if ( color == null ) {
			color = color1;
		}
		return color;
	}
	private Color color;
	protected Color color1;
	protected Color color2 = Color.red;
	public BasicGuiContainer() {
		color1 = ColorGenerator.generateRandomColor();
	}
	public void setColor1( Color c ) {
		this.color1 = c;
		updateColor();
	}
	public void setColor2( Color c ) {
		this.color2 = c;
		updateColor();
	}
	
	protected void setColor(GL gl) {
		try {
			gl.glColor4d(getColor().getRed()/255d, getColor().getGreen()/255d, getColor().getBlue()/255d, 0.5);
		}
		catch ( Exception e ) {
			gl.glColor4d(0d, 1d, 0d, 0.5);
			logger.error( "error setting color", e );
		}
	}
	
	void renderStrokeString(GL gl, int font, String string, double contwith) {
		// Center Our Text On The Screen
        float strwidth = glut.glutStrokeLengthf(font, string);
        
        double scale = contwith / strwidth;
        gl.glTranslated(0, 2*scale , 0); // FIXME
        gl.glScaled(scale, -scale, 1);
        // Render The Text
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            glut.glutStrokeCharacter(font, c);
        }
    }
	
	@Override
	public void renderContainer(GL gl) {
//		gl.glPushMatrix();
//		renderStrokeString(gl, GLUT.STROKE_MONO_ROMAN, "BasicGuiContainer", getWidth());
//		gl.glPopMatrix();
		gl.glPushMatrix();
			setColor(gl);
			if (opaque) {
				gl.glBegin(GL.GL_QUADS); 
					gl.glVertex2d(0, 0);
					gl.glVertex2d(0, getHeight());
					gl.glVertex2d(getWidth(), getHeight());
					gl.glVertex2d(getWidth(), 0);	
				gl.glEnd();
			}
			// debugging spheres
//			gl.glColor3d(0, 1, 0);
//			gl.glPushMatrix();
//				gl.glTranslated(0, 0, 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(getWidth(), 0, 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(getWidth(), getHeight(), 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
//			gl.glPushMatrix();
//				gl.glTranslated(0, getHeight(), 0);
//				glut.glutWireSphere(0.2, 4, 4);
//			gl.glPopMatrix();
		gl.glPopMatrix();
	}
	private void updateColor() {
		if ( isHighlighted() ) {
			color = color2;
		}
		else {
			color = color1;
		}
	}
	
	@Override
	protected synchronized void onMouseEnter( AMouseEvent e ) {
		logger.debug( "onMouseEnter() on " + this );
		setHighlighted( true );
		updateColor();
	}
	@Override
	protected synchronized void onMouseExit( AMouseEvent e ) {
		logger.debug( "onMouseExit() on " + this );
		setHighlighted( false );
		updateColor();
	}
	
	private String name;
	public void setName( String name ) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return this.name==null?super.toString():this.name;
	}

	@Override
	protected void onMouseClicked(AMouseEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMouseClicked() on " + this );
	}

	@Override
	protected void onMousePressed(AMouseEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMousePressed() on " + this );
	}

	@Override
	protected void onMouseReleased( AMouseEvent e) {
		// TODO Auto-generated method stub
		logger.info( "onMouseReleased() on " + this );
	}

	public void kill() {
		// TODO Auto-generated method stub
		
	}

	public void renderText(GL gl) {
		// TODO Auto-generated method stub
		
	}

	public boolean isHighlighted() {
		return highlighted;
	}

	public void setHighlighted(boolean highlighted) {
		this.highlighted = highlighted;
		updateColor();
	}

}
