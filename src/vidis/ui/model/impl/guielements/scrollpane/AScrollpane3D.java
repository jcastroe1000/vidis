package vidis.ui.model.impl.guielements.scrollpane;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.guielements.slider.VerticalSlider3D;
import vidis.ui.model.structure.IGuiContainer;

public abstract class AScrollpane3D extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(AScrollpane3D.class);
	
	private VerticalSlider3D slider;
	private BasicGuiContainer container;
	
	private List<BasicGuiContainer> elements;
	
	public AScrollpane3D() {
		elements = new ArrayList<BasicGuiContainer>();
	}
	
	/**
	 * retrieve the height of the containers content
	 * @return the height
	 */
	protected int getHeightOfElements() {
		int size = 0;
		Iterator<IGuiContainer> it = getChilds().iterator();
		while(it.hasNext()) {
			IGuiContainer element = it.next();
			size += element.getHeight();
		}
		return size;
	}
	public int countElements() {
		return getChilds().size();
	}
}
