package vidis.ui.model.impl;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.gui.MenuItem;
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
