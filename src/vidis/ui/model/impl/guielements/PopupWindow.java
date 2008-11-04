package vidis.ui.model.impl.guielements;

import java.awt.Color;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.PercentMarginLayout;

public abstract class PopupWindow extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(PopupWindow.class);
	
	public PopupWindow() {
		color1 = new Color(150, 150, 150, 150);
		color2 = new Color(150, 150, 150, 0);
		this.setLayout( new PercentMarginLayout(-0.05, -0.5, -0.35, -0.15, -0.35, -0.6));
	}
}
