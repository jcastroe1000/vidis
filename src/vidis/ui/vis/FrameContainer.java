/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.vis;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Label;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.media.opengl.GLCanvas;

import org.apache.log4j.Logger;

public class FrameContainer extends Frame {
	
	private static final long serialVersionUID = 1L;
	private static Logger logger = Logger.getLogger( FrameContainer.class );
	
	public FrameContainer(String title){
		super(title);
		
		logger.debug( "Constructor()" );
		
		//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 300);
		
		setLayout( new FlowLayout() );
		Label l = new Label( "loading..." );
		l.setFocusable(false);
		add( l );
		
		setVisible( true );	
		this.addWindowListener( new WindowListener() {
			public void windowActivated(WindowEvent e) {
				logger.info("windowActivated");
			}
			public void windowClosed(WindowEvent e) {
				logger.info("windowClosed");
			}
			public void windowClosing(WindowEvent e) {
				logger.info("windowClosing");
				System.exit(0);
			}
			public void windowDeactivated(WindowEvent e) {
				logger.info("windowDeactivated");
			}
			public void windowDeiconified(WindowEvent e) {
				logger.info("windowDeiconified");
			}
			public void windowIconified(WindowEvent e) {
				logger.info("windowIconified");
			}
			public void windowOpened(WindowEvent e) {
				logger.info("windowOpened");
			}
			
		});
	}
	
	public void addGLCanvas( GLCanvas glCanvas ) {
		removeAll();
		setLayout( new BorderLayout() );
		add( glCanvas, BorderLayout.CENTER );
		glCanvas.setFocusable( true );
		this.setFocusable( false );
	}
	
	public void fullscreen(){
		logger.debug( "fullscreen()" );
		 GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		 gd.getAvailableAcceleratedMemory();
		
	}

}
