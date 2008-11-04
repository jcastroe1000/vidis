package vidis.ui.model.impl.guielements;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Label;
import vidis.ui.model.impl.PercentMarginLayout;
import vidis.ui.model.impl.guielements.popupWindows.OpenMSIMFilePopupWindow;
import vidis.ui.model.structure.IGuiContainer;

public abstract class PopupWindow extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(PopupWindow.class);
	
	public PopupWindow(String title) {
		color1 = new Color(150, 150, 150, 150);
		color2 = new Color(150, 150, 150, 0);
		this.setLayout( new PercentMarginLayout(-0.05, -0.5, -0.35, -0.15, -0.35, -0.6));
		
		// titlebar
		Label tmp = new Label(title);
		tmp.setLayout( new PercentMarginLayout(0, -0.93, 0, 0, -0.07, -1) );
		tmp.setTextColor( Color.LIGHT_GRAY );
		addChild( tmp );
	}
	
	protected final void close() {
		getParent().removeChild( this );
	}
}
