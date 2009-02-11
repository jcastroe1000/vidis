/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.mvc.api.Dispatcher;

public class PlayPauseStop extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(PlayPauseStop.class);

	private Button play;
	private Button pause;
	private Button stop;
	
	private boolean isPlay = false;
	private boolean isPause = false;
	
	public PlayPauseStop() {
		
		play = new Button() {
			@Override
			public void onClick() {
				Dispatcher.forwardEvent( IVidisEvent.SimulatorPlay );
				isPlay = true;
				isPause = false;
				update();
			}
		};
		play.setText( "Play" );
		play.setLayout( new PercentMarginLayout( 0,0,-0.66,0,-1,-.34 ) );
		
		pause = new Button(){
			@Override
			public void onClick() {
				Dispatcher.forwardEvent( IVidisEvent.SimulatorPause ); 
				isPause = !isPause;
				update();
			}
		};
		pause.setText( "Pause" );
		pause.setLayout( new PercentMarginLayout( -0.34,0,-0.33,0,-1,-.33 ) );
		
		stop = new Button(){
			@Override
			public void onClick() {
				Dispatcher.forwardEvent( IVidisEvent.SimulatorReload );
				isPlay = false;
				isPause = false;
				update();
			}
		};
		stop.setText( "Reset" );
		stop.setLayout( new PercentMarginLayout( -.67,0,0,0,-1,-.33 ) );
		
		this.addChild( play );
		this.addChild( pause );
		this.addChild( stop );
	}
	
	private void update(){
		if ( isPlay ) {
			play.setColor1( Color.GREEN );
		}
		else {
			play.setColor1( Color.GRAY );
		}
		if ( isPause ) {
			pause.setColor1( Color.YELLOW );
		}
		else {
			pause.setColor1( Color.GRAY );
		}
		
	}
	
	
	
	
}
