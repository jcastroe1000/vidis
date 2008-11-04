package vidis.ui.model.impl.guielements.popupWindows;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.Label;
import vidis.ui.model.impl.PercentMarginLayout;
import vidis.ui.model.impl.guielements.PopupWindow;
import vidis.ui.model.impl.guielements.scrollpane.ScrollPane3D;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.mvc.api.Dispatcher;

public class ApplyLayoutPopupWindow extends PopupWindow {
	private static Logger logger = Logger.getLogger(ApplyLayoutPopupWindow.class);

	private static IGuiContainer instance = null;
	
	private ScrollPane3D scrollPane;
	
	private Map<String, Integer> mapTitleInt = new HashMap<String, Integer>();
	
	public ApplyLayoutPopupWindow() {
		super("Please pick a layout to apply:");
		
		scrollPane = new ScrollPane3D();
		scrollPane.setLayout( new PercentMarginLayout(0,0,0,-0.07,-0.93,-1) );
		addChild(scrollPane);
		
		mapTitleInt.put("Electric Spring Layout", IVidisEvent.LayoutApplyGraphElectricSpring);
		mapTitleInt.put("Grid Layout", IVidisEvent.LayoutApplyGrid);
		mapTitleInt.put("Spiral Layout", IVidisEvent.LayoutApplySpiral);
		mapTitleInt.put("Random Layout", IVidisEvent.LayoutApplyRandom);
		
		for(Entry<String, Integer> entry : mapTitleInt.entrySet()) {
			createElement(scrollPane, entry.getKey());
		}
	}
	
	private void createElement(ScrollPane3D scrollPane, String title) {
		Label tmp1 = new Label(title) {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				super.onMouseClicked(e);
				// forward event
				Dispatcher.forwardEvent(ApplyLayoutPopupWindow.this.mapTitleInt.get(getText()));
				// close this window
				close();
			}
		};
		tmp1.setBounds(1, 1, 7, 18);
		scrollPane.addChild(tmp1);
	}
	
	public static IGuiContainer getInstance() {
		if(instance == null)
			instance = new ApplyLayoutPopupWindow();
		return instance;
	}
}
