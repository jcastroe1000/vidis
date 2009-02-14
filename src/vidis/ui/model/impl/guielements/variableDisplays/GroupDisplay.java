/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.mouse.AMouseEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Label;

/**
 * 
 * @author Christoph
 *
 */
public class GroupDisplay extends Label {
	private static Logger logger = Logger.getLogger(GroupDisplay.class);

	private boolean showContent = true;
	private String ns = "no namespace set";
	
	
	
	private GroupDisplay () {
		this.setText( "> namespace" );
		this.setTextColor( Color.WHITE );
		this.setBackColor( Color.BLACK );
		this.setOpaque( true );
	}
	
	private Set<BasicGuiContainer> content;
	
	public GroupDisplay( String ns ) {
		this();
		this.content = new HashSet<BasicGuiContainer>();
		this.ns = ns;
	}
	
	public GroupDisplay( String ns, boolean showContent ) {
		this( ns );
		this.setShowContent( showContent );
	}
	
	@Override
	public void renderContainer(GL gl) {
		this.setText( (getShowContent()?" v ":" > ") + ns );
		super.renderContainer(gl);
	}
	
	public boolean getShowContent() {
		return showContent;
	}
	
	public void setShowContent( boolean showContent ) {
		this.showContent = showContent;
		for ( BasicGuiContainer d : content ) {
			d.setVisible( showContent );
		}
	}
	
	@Override
	protected void onMouseClicked(AMouseEvent e) {
		if ( showContent ) {
			setShowContent( false );
		}
		else {
			setShowContent( true );
		}
	}
	
	public void addContent( BasicGuiContainer d ) {
		this.content.add( d );
		d.setVisible( getShowContent() );
	}
	public void removeContent( BasicGuiContainer d ) {
		this.content.remove( d );
	}

	
	public Set<BasicGuiContainer> getContent() {
		return this.content;
	}
}
