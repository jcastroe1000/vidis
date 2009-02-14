/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.mouse.AMouseEvent;

public class CheckBox extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(CheckBox.class);

	private boolean checked = false;
	
	float borderPercent = 0.1f;
	
	private String text = "BUTTON";
	
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	private Color textColor = Color.BLACK;
	
	public CheckBox() {
		setColor1( Color.gray );
		setColor2( Color.gray.darker() );
	}
	
	private static double checkBoxSize = 1;
	
	@Override
	public void renderContainer(GL gl) {

		// background
//		useColor( gl, Color.blue );
//		gl.glPushMatrix();
//		gl.glTranslated(0, 0, -0.01);
//		gl.glBegin(GL.GL_QUADS); 
//			gl.glVertex2d( 0, 0);
//			gl.glVertex2d( 0, getHeight());
//			gl.glVertex2d( getWidth(), getHeight());
//			gl.glVertex2d( getWidth(), 0);	
//		gl.glEnd();
//		gl.glPopMatrix();
	
		requireTextRenderer();
		
		gl.glPushMatrix();
		
			gl.glTranslated(0, (getHeight() - checkBoxSize) / 2d, 0);
			
			useColor( gl, getColor() );
			// drawing the box
			gl.glPushMatrix();
				double h = checkBoxSize;
				double w = checkBoxSize;
				double border = h * borderPercent;
				useColor( gl, getColor() );
				
				if ( !checked ) {
					useColor( gl, Color.white );
				}
				else {
					useColor( gl, Color.red );
				}
				
				// center
				gl.glBegin(GL.GL_QUADS); 
					gl.glVertex2d( border, border);
					gl.glVertex2d( border, h - border);
					gl.glVertex2d( w - border, h - border );
					gl.glVertex2d( w - border, border);	
				gl.glEnd();
			
				// rand
					useColor( gl, colorDark );
		
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
				
					useColor( gl, colorLight );
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
			gl.glPopMatrix();
			
		gl.glPopMatrix();
		
		// text
		Rectangle2D r = textRenderer.getBounds( text );
		float scale = 0.05f;
		
		h = getHeight();
		w = getWidth();
		
		double factor = 0.7;
		double fontScaleWidth = (getWidth() * factor) / (r.getWidth() * scale);
		double fontScaleHeight = (h * factor) / (textH * scale);
		double fontScale = fontScaleHeight;
		
		gl.glPushMatrix();
			
		//gl.glScaled( fontScale, fontScale, 1);
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
//			textRenderer.draw3D( text, 
//					(float) (2 * checkBoxSize), 
//					(float) (getHeight() / 2f - r.getHeight() * scale * fontScale / 2f),
//					0.5f,
//					(float) (scale * fontScale) );
//			textRenderer.end3DRendering();
			textRenderer.draw3D( text, 
					(float) (2 * checkBoxSize), 
					(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
					0.5f,
					(float) (scale * fontScale) );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
	
	private void useColor( GL gl, Color c ) {
		gl.glColor4d(c.getRed()/255d, c.getGreen()/255d, c.getBlue()/255d, c.getAlpha()/255d);
	}
	
	@Override
	protected void onMousePressed(AMouseEvent e) {
	}
	@Override
	protected void onMouseReleased(AMouseEvent e) {
	}
	@Override
	protected void onMouseClicked(AMouseEvent e) {
		if ( checked == false ) {
			checked = true;
		}
		else {
			checked = false;
		}
		notifyCheckChangeListeners();
	}
	
	public void setText( String text ) {
		this.text = text;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked( boolean checked ) {
		this.checked = checked;
		notifyCheckChangeListeners();
	}
	
	private List<CheckChangeListener> checkChangeListener = new ArrayList<CheckChangeListener>();
	public void addCheckChangeListener( CheckChangeListener c ) {
		synchronized ( this.checkChangeListener ) {
			this.checkChangeListener.add( c );
		}
	}
	public void removeCheckChangeListener( CheckChangeListener c ) {
		synchronized ( this.checkChangeListener ) {
			this.checkChangeListener.remove( c );
		}
	}
	private void notifyCheckChangeListeners() {
		synchronized ( this.checkChangeListener ) {
			for ( CheckChangeListener c : this.checkChangeListener ) {
				c.onCheckCange( checked );
			}
		}
	}
	
	@Override
	protected void onMouseEnter( AMouseEvent e ) {
		textColor = Color.GRAY;
	}
	
	@Override
	protected void onMouseExit( AMouseEvent e ) {
		textColor = Color.BLACK;
	}
}
