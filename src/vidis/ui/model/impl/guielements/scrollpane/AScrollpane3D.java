package vidis.ui.model.impl.guielements.scrollpane;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.model.impl.AScissorContainer;
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
			int realIndex = 0;
			for ( IGuiContainer c : childs ) {
				 if ( c.isVisible() ) {
					 realIndex++;
				 }
				 if ( c.equals( container ) ) {
					 break;
				 }
			}
			int ch_count = chosen;
			int realChosen = 0;
			for ( int i = 0; i < childs.size(); i++ ) {
				 if ( childs.get(i).isVisible() ) {
					 ch_count--;
				 }
				 if ( ch_count == 0 ) {
					 realChosen = i;
					 break;
				 }
			}
			if ( realIndex < chosen ) {
				return Double.MAX_VALUE;
			}
			else if ( realIndex == realChosen ) {
				return padding;
			}
			else {
				double sum = padding;
				for ( int i = realChosen; i <= myIndex; i++ ) {
					if ( childs.get(i).isVisible() ) {
						sum += childs.get(i).getHeight();
						sum += padding;
					}
					// when no scissor test is available omit the last elements
					if ( ! AScrollpane3D.this.isUseScissorTestNow() ) {
						if ( sum > maxHeight ) {
							return Double.MAX_VALUE;
						}
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
		setUseScissorTest( true );
		childs = new ArrayList<IGuiContainer>();
		setOpaque( false );
		
		slider = new VerticalSlider3D(0, 1); // initially 0,1; increment later as the elements count increases
		slider.setOpaque( false );
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
		super.addChild(slider);
		
		container = new BasicGuiContainer() {
			@Override
			public void renderContainer(GL gl) {
//				super.renderContainer(gl);
			}
		};
		container.setOpaque(false);
		container.setName("scrollpane container");
		container.setLayout(new ILayout() {
			public double getHeight() {
				return AScrollpane3D.this.getHeight();
			}

			public double getWidth() {
				return AScrollpane3D.this.getWidth() - slider.getWidth();
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
		super.addChild(container);
		
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
	
	@Override
	public void removeAllChilds() {
		childs.clear();
		container.removeAllChilds();
		fixSliderMinMax();
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
			slider.setMin(1);
//			slider.setMax(childs.size() - max);
			
			// theoriginal thingy
			slider.setMax( countElements() );
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
			if ( element.isVisible() ) {
				size += element.getHeight();
			}
		}
		return size;
	}
	
	/**
	 * counts the elements that this scrollpane has
	 * element must hava a size bigger than zero to be counted
	 * @return the count of elements
	 */
	public int countElements() {
		int s=0;
		for ( IGuiContainer c : childs ) {
			if ( c.isVisible() ) {
				s++;
			}
		}
		return s;
	}
	
	public void renderContainer(GL gl) {
		fixSliderMinMax();
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
