/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;
import java.awt.event.InputEvent;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.mouse.AMouseEvent;

public class Button extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(Button.class);

	private boolean pressed = false;
	
	float borderPercent = 0.05f;
	
	private String text = "BUTTON";
	
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	private Color textColor = Color.BLACK;
	
	private Label l;
	
	public Button() {
		setUseScissorTest( true );
		setColor1( Color.gray );
		setColor2( Color.gray.darker() );
		
//		l = new Label( text ) {
//			@Override
//			protected void onMouseClicked(MouseClickedEvent e) {
//				Button.this.onMouseClicked(e);
//			}
//			@Override
//			protected synchronized void onMouseEnter( MouseMovedEvent e ) {
//				Button.this.onMouseEnter( e );
//			}
//			@Override
//			protected synchronized void onMouseExit( MouseMovedEvent e ) {
//				Button.this.onMouseExit( e );
//			}
//			@Override
//			protected void onMousePressed(MousePressedEvent e) {
//				Button.this.onMousePressed(e);
//			}
//			@Override
//			protected void onMouseReleased(MouseReleasedEvent e) {
//				Button.this.onMouseReleased(e);
//			}
//		};
//		l.setLayout( new PercentMarginLayout( 0, 0, 0, 0, -1, -1 ) {
//			@Override
//			public double getX() {
//				if ( pressed ) {
//					double border = getHeight() * borderPercent;
//					return super.getX() + border/2d;
//				}
//				return super.getX();
//			}
//			@Override
//			public double getY() {
//				if ( pressed ) {
//					double border = getHeight() * borderPercent;
//					return super.getY() - border/2d;
//				}
//				return super.getY();
//			}
//		});
//		this.addChild( l );
	}
	
	@Override
	public void renderContainer(GL gl) {
		double h = getHeight();
		double w = getWidth();
		double border = h * borderPercent;
		// text
		
		requireTextRenderer();
		
		useColor( gl, getColor() );
		
		
		
		useColor( gl, getColor() );
		// center
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, getHeight() - border);
			gl.glVertex2d( getWidth() - border, getHeight() - border );
			gl.glVertex2d( getWidth() - border, border);	
		gl.glEnd();
	
		// rand
		if ( !pressed ) {
			useColor( gl, colorLight );
		}
		else {
			useColor( gl, colorDark );
		}

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
		
		if ( !pressed ) {
			useColor( gl, colorDark );
		}
		else {
			useColor( gl, colorLight );
		}
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
		
		// text
		Rectangle2D r = textRenderer.getBounds( text );
		float scale = 0.01f;
		
		double factor = 0.7;
		double fontScaleHeight = (h * factor) / (textH * scale);
		double fontScale = fontScaleHeight;
		
		gl.glPushMatrix();
			
		//gl.glScaled( fontScale, fontScale, 1);
		if ( pressed ) {
				gl.glTranslated( border/2d, -border/2d, 0 );
			}
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
			textRenderer.draw3D( text, 
					(float) (w / 2f - r.getWidth() * scale * fontScale / 2f), 
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
		pressed = true;
	}
	@Override
	protected void onMouseReleased(AMouseEvent e) {
		pressed = false;
		onClick();
	}
	
	@Override
	protected synchronized void onMouseExit( AMouseEvent e ) {
		pressed = false;
		super.onMouseExit( e );
	}
	
	@Override
	protected synchronized void onMouseEnter( AMouseEvent e ) {
		if ( (e.mouseEvent.getModifiersEx() & InputEvent.BUTTON1_DOWN_MASK) == InputEvent.BUTTON1_DOWN_MASK ) {
			pressed = true;
		}
		super.onMouseEnter( e );
	}
	
	public void setText( String text ) {
		this.text = text;
//		l.setText(text);
	}
	
	public void onClick() {
		
	}
}
