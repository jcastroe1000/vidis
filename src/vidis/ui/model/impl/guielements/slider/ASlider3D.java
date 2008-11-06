package vidis.ui.model.impl.guielements.slider;

import java.awt.Color;

import javax.media.opengl.GL;
import javax.vecmath.Point2d;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class ASlider3D extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(ASlider3D.class);

	private BasicGuiContainer top;
	private BasicGuiContainer middle;
	private BasicGuiContainer bottom;
	
	private int scroll_min = 0;
	private int scroll_max = 0;
	private int scroll_position = 0;
	
	public ASlider3D(int min, int max) {
		super();
		
		setMin(min);
		setMax(max);
		
		top = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				super.onMouseClicked(e);
				// scroll up
				scrollDown();
			}
			@Override
			public void renderContainer(GL gl) {
				double buttonSize_x = this.getWidth();
				double buttonSize_y = this.getHeight();
				gl.glPushMatrix();
					setColor(gl);
	//				gl.glRotated(180, 1, 0, 0);
					gl.glTranslated(buttonSize_x/2, 0, 0);
					gl.glBegin(GL.GL_TRIANGLE_STRIP);
						gl.glVertex2d(0, 0);
						gl.glVertex2d(buttonSize_x/2, buttonSize_y);
						gl.glVertex2d(-buttonSize_x/2, buttonSize_y);
					gl.glEnd();
				gl.glPopMatrix();
//				super.renderContainer(gl);
			}
		};
//		top.setLayout( new PercentMarginLayout( -0.01, -0.01, -0.01, -0.9, -0.1, -1 ) );
		top.setLayout( new ILayout() {

			public double getHeight() {
				return ASlider3D.this.getWidth();
			}

			public double getWidth() {
				return ASlider3D.this.getWidth();
			}

			public double getX() {
				return 0;
			}

			public double getY() {
				return 0;
			}

			public void layout() {
				// TODO Auto-generated method stub
				
			}

			public void setGuiContainer(IGuiContainer c) {
				// TODO Auto-generated method stub
				
			}
			
		});
		top.setName( "TOP" );
		
		middle = new BasicGuiContainer() {
			@Override
			public void renderContainer(GL gl) {
				double sliderSize = this.getWidth();
				setColor(gl);
				gl.glPushMatrix();
					double top = sliderSize/2;
					double bottom = -sliderSize/2;
					double left = -sliderSize/2;
					double right = sliderSize/2;
//					gl.glRotated(90, 1, 0, 0);
					gl.glTranslated(right, top, 0);
					gl.glBegin(GL.GL_QUADS);
						gl.glVertex2d(0, top);
						gl.glVertex2d(right, 0);
						gl.glVertex2d(0, bottom);
						gl.glVertex2d(left, 0);
					gl.glEnd();
				gl.glPopMatrix();
//				super.renderContainer(gl);
			}
			@Override
			public double getHeight() {
				return ASlider3D.this.getWidth();
			}
			@Override
			public double getWidth() {
				return ASlider3D.this.getWidth();
			}
			@Override
			protected void handleResize() {
				super.handleResize();
				positionMiddle();
			}
		};
//		middle.setLayout( new PercentMarginLayout( -0.01, -0.1, -0.01, -0.1, -0.8, -1 ) );
//		middle.setLayout(new PercentMarginLayout( -0.01, -0.1 + getPositionPercentage()*0.9, -0.01, -0.1+getPositionPercentage()*0.9, 3, -1 ));
		
		middle.setName( "MIDDLE" );
		
		bottom = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				super.onMouseClicked(e);
				scrollUp();
			}
			
			@Override
			public void renderContainer(GL gl) {
				double buttonSize_x = this.getWidth();
				double buttonSize_y = this.getHeight();
				gl.glPushMatrix();
					setColor(gl);
					gl.glRotated(180, 1, 0, 0);
					gl.glTranslated(buttonSize_x/2, -buttonSize_y, 0);
					gl.glBegin(GL.GL_TRIANGLE_STRIP);
						gl.glVertex2d(0, 0);
						gl.glVertex2d(buttonSize_x/2, buttonSize_y);
						gl.glVertex2d(-buttonSize_x/2, buttonSize_y);
					gl.glEnd();
				gl.glPopMatrix();
//				super.renderContainer(gl);
			}
		};
		//bottom.setLayout( new PercentMarginLayout( -0.01, -0.9, -0.01, -0.01, -0.1, -1 ) );
		bottom.setName( "BOTTOM" );
		bottom.setLayout( new ILayout() {

			public double getHeight() {
				return ASlider3D.this.getWidth();
			}

			public double getWidth() {
				return ASlider3D.this.getWidth();
			}

			public double getX() {
				return 0;
			}

			public double getY() {
				return ASlider3D.this.getHeight() - this.getWidth();
			}

			public void layout() {
				// TODO Auto-generated method stub
				
			}

			public void setGuiContainer(IGuiContainer c) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		this.setName( "PARENT" );
		
		this.addChild( top );
		this.addChild( middle );
		this.addChild( bottom );
		
//		setPosition(getMin());
		
//		positionMiddle();
//		
//		scrollUp();
	}
	
	private void positionMiddle() {
		double x = 0;
		double spacer_top = bottom.getHeight();
		double spacer_bottom = top.getHeight();
//		double y = top.getHeight() + ( getHeight() - middle.getHeight() - top.getHeight() - bottom.getHeight() ) * ( 1d - getPositionPercentage() );
//		System.err.println(getHeight() + " / " + middle.getHeight() + " / " + spacer_top + " / " + spacer_bottom);
		double y = spacer_bottom + ( ( getHeight() - middle.getHeight() - spacer_top - spacer_bottom ) * ( 1d - getPositionPercentage() ));
		middle.setBounds(x, y, middle.getHeight(), middle.getWidth());
//		System.err.println("["+x+","+y+" | "+middle.getHeight()+","+middle.getWidth()+"]");
	}
	
	protected void setPosition(int pos) {
		pos = Math.min(Math.max(getMin(), pos), getMax());
		
		scroll_position = pos;
		
		positionMiddle();
		
//		System.err.println("now at : "+getMin()+" ("+getPositionPercentage()*100+"%) < " + getPosition() + " < " + getMax());
	}
	
	public int getMin() {
		return scroll_min;
	}
	
	public int getMax() {
		return scroll_max;
	}
	
	public void setMin(int min) {
		min = Math.min(getMax(), min);
		scroll_min = min;
	}
	
	public void setMax(int max) {
		max = Math.max(getMin(), max);
		scroll_max = max;
	}
	
	public int getPosition() {
		return scroll_position;
	}
	
	private double getPositionPercentage() {
		if(getMax()-getMin() > 0) {
			return ((double)(getPosition()-getMin())) / ((double)(getMax()-getMin()));
		} else {
			return 0;
		}
	}
	
	public void scrollUp() {
		setPosition(getPosition() - 1);
	}
	
	public void scrollDown() {
		setPosition(getPosition() + 1);
	}
	
	@Override
	public void setColor1(Color c) {
		super.setColor1(c);
		bottom.setColor1(c);
		
		top.setColor1(c);
		
		middle.setColor1(c);
	}
	
	@Override
	public void setColor2(Color c) {
		super.setColor2(c);
		bottom.setColor2(c);
		
		top.setColor2(c);
		
		middle.setColor2(c);
	}
	
	@Override
	public void renderContainer(GL gl) {
//		super.renderContainer(gl);
		setPosition(getPosition());
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent e) {
		Point2d coords = new Point2d(e.guiCoords.x - getParent().getAbsoluteX(), e.guiCoords.y - getParent().getAbsoluteY());
		double c_y = coords.y;
		double m_y = getHeight();
		if(m_y > 0) {
			double p_y = 1d - c_y/m_y;
			int a_y = (int) Math.round(getMin() + getMax() * p_y);
			setPosition(a_y);
		}
		super.onMouseClicked(e);
	}
}
