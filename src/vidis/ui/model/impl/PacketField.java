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

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.mvc.api.Dispatcher;

public class PacketField extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(PacketField.class);

	float borderPercent = 0.05f;
	
	/**
	 * represents the packet
	 */
	private Packet packet = null;
	
	/**
	 * represents the shown substring 
	 */
	private String shownText = "";
	
	private boolean selecting = false;
	
	/**
	 * 
	 */
	private int shownChars = 20;
	
	/**
	 * 
	 */
	private int textOffset = 0;
	
	/**
	 * 
	 */
	private int cursorPosition = 0;
	// border colors
	private Color colorLight = Color.LIGHT_GRAY;
	private Color colorDark = Color.DARK_GRAY;
	
	// text color
	private Color textColor = Color.black;
	// background
	private Color backColor = Color.white;
	
	public PacketField() {
		setColor1( backColor );
		setColor2( backColor.darker() );
	}
	
	@Override
	public void renderContainer(GL gl) {
		double h = getHeight();
		double w = getWidth();
		double border = h * borderPercent;

		double innerH = h - 2 * border;
		double innerW = h - 2 * border;
		
		requireTextRenderer();
		
		useColor( gl, getColor() );
		// center
		gl.glBegin(GL.GL_QUADS); 
			gl.glVertex2d( border, border);
			gl.glVertex2d( border, getHeight() - border);
			gl.glVertex2d( getWidth() - border, getHeight() - border );
			gl.glVertex2d( getWidth() - border, border);	
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
		
		// text
		String text = packet==null?"<no packet selected>":"<Packet: " + packet.getId() + ">";
		
		if ( selecting ) {
			text = "[select a packet]";
		}
		
		Rectangle2D r = textRenderer.getBounds( "Apd[]" );
		float scale = 0.01f;
		
		double factor = 0.7;
		double fontScaleWidth = (w * factor) / (r.getWidth() * scale);
		double fontScaleHeight = (h * factor) / (r.getHeight() * scale);
		double fontScale = fontScaleHeight;
		
		gl.glPushMatrix();
			
		//gl.glScaled( fontScale, fontScale, 1);
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
			int endIndex = textOffset + shownChars;
			if ( endIndex > text.length() ) {
				endIndex = text.length();
			}
			String s = text.substring( textOffset, endIndex );
			textRenderer.draw3D(  s, 
					(float) ( border + border ), 
					(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
					0.5f,
					(float) (scale * fontScale) );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
		
		if ( cursorPosition >= textOffset && cursorPosition < text.length() - textOffset ) {
		// cursor position
		double cursorX = 0;
		double cursorY = border;
		double cursorW = 0.1;
		double cursorH = innerH;
		
		useColor(gl, new Color( 1f, 0f, 0f, 0.4f ) );
		gl.glPushMatrix();
			gl.glTranslated(0, 0, 0.5);
			gl.glBegin( GL.GL_QUADS );
				gl.glVertex2d( cursorX, cursorY );
				gl.glVertex2d( cursorX, cursorY + cursorH );
				gl.glVertex2d( cursorX + cursorW, cursorY + cursorH );
				gl.glVertex2d( cursorX + cursorW, cursorY );
			gl.glEnd();
		gl.glPopMatrix();
		
		}
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
		startPacketCapturing();
	}
	
	public void setPacket( Packet packet ) {
		selecting = false;
		this.packet = packet;
	}
	
	public Packet getPacket() {
		return this.packet;
	}
	
	public void startPacketCapturing() {
		selecting = true;
		VidisEvent<PacketField> next = new VidisEvent<PacketField>( IVidisEvent.StartPacketCapturing, this );
		Dispatcher.forwardEvent( next );
	}
}
