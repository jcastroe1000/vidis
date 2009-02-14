/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.mouse.AMouseEvent;

/**
 * represents a common known Label to write something onto the screen
 * @author Christoph
 *
 */
public class Label extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Label.class);

	private double height = 1.5;
	
	float borderPercent = 0.00f;
	
	/**
	 * represents the 'real' text in the textbox
	 */
	private String text = "label";
	
	// border colors
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	// text color
	private Color textColor = Color.black;
	// background
	private Color backColor = Color.white;
	
	public Color getBackColor() {
		return backColor;
	}

	public void setBackColor(Color backColor) {
		this.backColor = backColor;
		setColor1( backColor );
		setColor2( backColor.darker() );
	}

	public Label() {
		setColor1( backColor );
		setColor2( backColor.darker() );
	}
	
	public Label( String text ) {
		this();
		setText( text );
	}
	
	@Override
	public void renderContainer(GL gl) {
		if ( isVisible() ) {
			if ( this.isOpaque() ) {
				super.renderContainer(gl);
			}
			double h = getHeight();
			double border = h * borderPercent;
	
			requireTextRenderer();
			
			useColor( gl, getColor() );
			
			// text
			Rectangle2D r = textRenderer.getBounds( "Apdq[]" );
			float scale = 0.01f;
			
			double factor = 0.7;
			double fontScaleHeight = (h * factor) / (r.getHeight() * scale);
			double fontScale = fontScaleHeight;
			
			gl.glPushMatrix();
				
			//gl.glScaled( fontScale, fontScale, 1);
				textRenderer.setColor( textColor );
				textRenderer.begin3DRendering();
				String s = this.text;
				textRenderer.draw3D(  s, 
						(float) ( border + border ), 
						(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
						0.01f,
						(float) (scale * fontScale) );
				textRenderer.end3DRendering();
			gl.glPopMatrix();
		}
	}
	
	private void useColor( GL gl, Color c ) {
		gl.glColor4d(c.getRed()/255d, c.getGreen()/255d, c.getBlue()/255d, c.getAlpha()/255d);
	}
	
	@Override
	public double getWantedHeight() {
		double height = 0d;
		if ( isVisible() ) {
			height = this.height;
		}
		return height;
	}
	
	@Override
	protected void onMousePressed(AMouseEvent e) {
	}
	@Override
	protected void onMouseReleased(AMouseEvent e) {
	}
	@Override
	protected void onMouseClicked(AMouseEvent e) {
	}
	
	public void setText( String text ) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	public void setTextColor(Color c) {
		this.textColor = c;
		
	}
}
