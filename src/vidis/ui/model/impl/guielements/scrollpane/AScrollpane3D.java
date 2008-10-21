package vidis.ui.model.impl.guielements.scrollpane;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.guielements.slider.VerticalSlider3D;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public abstract class AScrollpane3D extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(AScrollpane3D.class);
	
	private VerticalSlider3D slider;
	private BasicGuiContainer container;
	
	private List<IGuiContainer> childs;
	
	public AScrollpane3D() {
		childs = new ArrayList<IGuiContainer>();
		
		slider = new VerticalSlider3D(0, 1); // initially 0,1; increment later as the elements count increases
		super.addChild(slider);
		
		slider.setName("scrollpane slider");
		slider.setLayout( new ILayout() {

			public double getHeight() {
				return getParent().getHeight();
			}

			public double getWidth() {
				return 1;
			}

			public double getX() {
				return getParent().getWidth() - this.getWidth();
			}

			public double getY() {
				return 0;
			}

			public void layout() {
			}

			public void setGuiContainer(IGuiContainer c) {
			}
		});
		
		container = new BasicGuiContainer() {
			@Override
			public void renderContainer(GL gl) {
//				super.renderContainer(gl);
			}
		};
		super.addChild(container);
		
		container.setName("scrollpane container");
		container.setLayout(new ILayout() {
			public double getHeight() {
				return getParent().getHeight();
			}

			public double getWidth() {
				return getParent().getWidth() - slider.getWidth();
			}

			public double getX() {
				return 0;
			}

			public double getY() {
				return 0;
			}

			public void layout() {
			}

			public void setGuiContainer(IGuiContainer c) {
			}
		});
		
		fixSliderMinMax();
	}
	
	@Override
	public void addChild(IGuiContainer c) {
		childs.add(c);
		container.addChild(c);
		fixSliderMinMax();
	}
	
	private void fixSliderMinMax() {
		if(getHeightOfElements() > 1)
			slider.setMax(childs.size());
		else
			slider.setMax(1);
	}
	
	public void removeChild(int pos) {
		if(childs.size() > pos && pos > 0) {
			container.removeChild(childs.get(pos));
			childs.remove(pos);
		}
	}
	
	@Override
	public void removeChild(IGuiContainer c) {
		childs.remove(c);
		container.removeChild(c);
	}
	
	/**
	 * retrieve the height of the containers content
	 * @return the height
	 */
	protected int getHeightOfElements() {
		int size = 0;
		Iterator<IGuiContainer> it = container.getChilds().iterator();
		while(it.hasNext()) {
			IGuiContainer element = it.next();
			size += element.getHeight();
		}
		return size;
	}
	
	/**
	 * counts the elements that this scrollpane has
	 * @return the count of elements
	 */
	public int countElements() {
		return childs.size();
	}
	
	@Override
	public void renderContainer(GL gl) {
//		super.renderContainer(gl);
	}
	
	@Override
	public void setColor1(Color c) {
		super.setColor1(c);
		
		slider.setColor1(c);
	}
	
	@Override
	public void setColor2(Color c) {
		super.setColor2(c);
		
		slider.setColor2(c);
	}
}
