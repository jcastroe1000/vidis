package vidis.ui.model.impl.guielements.scrollpane;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.guielements.slider.VerticalSlider3D;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public abstract class AScrollpane3D extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(AScrollpane3D.class);
	
	private VerticalSlider3D slider;
	private BasicGuiContainer container;
	
	private double padding = 0.2;
	
	private List<IGuiContainer> childs;
	
	class ScrollPaneLayout implements ILayout {
		private IGuiContainer container;
		public double getHeight() {
			return container.getWantedHeight();
		}
		public double getWidth() {
			return AScrollpane3D.this.container.getWidth() - 2d*padding;
		}
		public double getX() {
			return padding;
		}
		public double getY() {
			return AScrollpane3D.this.getHeight() - calcY() - getHeight();
		}
		
		private double calcY() {
			double maxHeight = AScrollpane3D.this.container.getHeight();
			int myIndex = childs.indexOf( container );
			int chosen = slider.getPosition();
			if ( myIndex < chosen ) {
				return Double.MAX_VALUE;
			}
			else if ( myIndex == chosen ) {
				return padding;
			}
			else {
				double sum = padding;
				for ( int i = chosen; i <= myIndex; i++ ) {
					sum += childs.get(i).getHeight();
					sum += padding;
					if ( sum > maxHeight ) {
						return Double.MAX_VALUE;
					}
				}
				return sum - childs.get( myIndex ).getHeight();
			}
		}
		
		public void layout() {
			// do nothing since all getters are 'live'
		}
		public void setGuiContainer(IGuiContainer c) {
			this.container = c;
		}
		
	}
	
	
	public AScrollpane3D() {
		childs = new ArrayList<IGuiContainer>();
		
		slider = new VerticalSlider3D(0, 1); // initially 0,1; increment later as the elements count increases
		super.addChild(slider);
		
		slider.setName("scrollpane slider");
		slider.setLayout( new ILayout() {

			public double getHeight() {
				return AScrollpane3D.this.getHeight();
//				return getParent().getHeight();
			}

			public double getWidth() {
				return 1;
			}

			public double getX() {
				return AScrollpane3D.this.getWidth() - this.getWidth();
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
		if ( childs.contains( c ) ) {
			return;
		}
		else {
			c.setLayout( new ScrollPaneLayout() );
			childs.add(c);
			container.addChild(c);
			fixSliderMinMax();
		}
	}
	
	private void fixSliderMinMax() {
		if(getHeightOfElements() > 1) {
			// calculate minimum (this is the first N elements that are displayable)
			// calculate maximum (this is the size-the last elements that are displayable)
//			double minH=0, maxH = 0;
//			int min=0, max=0;
//			for(int i=0; i<childs.size(); i++) {
//				IGuiContainer elementN = childs.get(i);
//				if( minH < container.getHeight() ) {
//					minH += elementN.getHeight();
//					min++;
//				}
//			}
//			for(int i=childs.size()-1; i>0; i--) {
//				IGuiContainer elementN = childs.get(i);
//				if( maxH < container.getHeight() ) {
//					maxH += elementN.getHeight();
//					max++;
//				}
//			}
//			slider.setMin(min);
//			slider.setMax(childs.size() - max);
			
			// theoriginal thingy
			slider.setMax(childs.size());
		}
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
		super.renderContainer(gl);
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
	
	public void setPadding( double padding ) {
		this.padding = padding;
	}
	
	public double getPadding() {
		return this.padding;
	}
}
