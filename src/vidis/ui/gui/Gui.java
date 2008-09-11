package vidis.ui.gui;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.AEventHandler;
import vidis.ui.events.GuiMouseEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.PercentMarginLayout;
import vidis.ui.model.impl.TextGuiContainer;
import vidis.ui.model.impl.guielements.Basic3DScrollPane;
import vidis.ui.model.impl.guielements.Label;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.mvc.api.Dispatcher;
import vidis.ui.vis.objects.Axis;


public class Gui extends AEventHandler {

	private static Logger logger = Logger.getLogger( Gui.class );
	
	private BasicGuiContainer mainContainer;
	
	public TextGuiContainer fps;
	
	public Gui() {
		logger.debug("Constructor()");
		mainContainer = new BasicGuiContainer();
		mainContainer.setOpaque( false );
		initializeRightPanel();
		initializeControls();
	}
	
	private void initializeRightPanel() {
		logger.debug("initializeRightPanel");
		BasicGuiContainer rightPanel = new BasicGuiContainer();
		rightPanel.setColor( Color.black );
		rightPanel.setLayout(new PercentMarginLayout(-0.7,1,1,1,-1,-0.30));
		rightPanel.addChild(new Basic3DScrollPane());
		// draw a label
		Label label = new Label("tut");
		rightPanel.addChild(label);
		mainContainer.addChild(rightPanel);
	}
	private void initializeControls(){
		logger.debug("initializeControls()");
		BasicGuiContainer container1 = new BasicGuiContainer();
		container1.setLayout(new PercentMarginLayout(1,-0.9,-0.9,1,-0.1,-0.1));
		
		TextGuiContainer playButton = new TextGuiContainer() {
			@Override
			protected void onClick(GuiMouseEvent e) {
				Dispatcher.forwardEvent( IVidisEvent.SimulatorPlay );
			}
		};
		playButton.setLayout(new PercentMarginLayout(-0.1,-0.1,-0.1,-0.1,-0.8,-0.8));
		playButton.setText("Play");

		BasicGuiContainer container2 = new BasicGuiContainer();
		container2.setLayout(new PercentMarginLayout(-0.2,-0.9,-0.8,1,-0.1,-0.1));
		
		TextGuiContainer loadButton = new TextGuiContainer();
		loadButton.setLayout(new PercentMarginLayout(-0.1,-0.1,-0.1,-0.1,-0.8,-0.8));
		loadButton.setText("Load");
		
		
		mainContainer.addChild(container1);
		container1.addChild(playButton);
		mainContainer.addChild(container2);
		container2.addChild(loadButton);
		fps = new TextGuiContainer();
		fps.setLayout(new PercentMarginLayout(1,1,-0.9,-0.9,-0.1,-0.1));
		fps.setText("0fps");
		mainContainer.addChild(fps);
	}
	
	
	@Override
	protected void handleEvent(IVidisEvent e) {
		mainContainer.fireEvent(e);
	}
	
	public void updateBounds( double x, double y, double height, double width ) {
		mainContainer.setBounds(x, y, height, width);
	}
	
	public void render( GL gl ) {
		mainContainer.render(gl);
	}

	public IVisObject getMainContainer() {
		return mainContainer;
	}
}

