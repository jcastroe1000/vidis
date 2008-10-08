package vidis.ui.model.impl.guielements.slider;

import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.PercentMarginLayout;
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
				gl.glPushMatrix();
				Color color = Color.green;
				gl.glColor3d(color.getGreen(), color.getRed(), color.getBlue());
//				gl.glRotated(180, 1, 0, 0);
				gl.glTranslated(0, -buttonSize, 0);
				gl.glBegin(GL.GL_TRIANGLE_STRIP);
					gl.glVertex2d(buttonSize/2, 0);
					gl.glVertex2d(buttonSize/2 + 0.3, buttonSize);
					gl.glVertex2d(buttonSize/2 - 0.3, buttonSize);
				gl.glEnd();
			gl.glPopMatrix();
			}
		};
		top.setLayout( new PercentMarginLayout( -0.01, -0.01, -0.01, -0.9, -0.1, -1 ) );
		top.setName( "TOP" );
		
		middle = new BasicGuiContainer();
//		middle.setLayout( new PercentMarginLayout( -0.01, -0.1, -0.01, -0.1, -0.8, -1 ) );
//		middle.setLayout(new PercentMarginLayout( -0.01, -0.1 + getPositionPercentage()*0.9, -0.01, -0.1+getPositionPercentage()*0.9, 3, -1 ));
		positionMiddle();
		middle.setName( "MIDDLE" );
		
		bottom = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				super.onMouseClicked(e);
				scrollUp();
			}
			
			@Override
			public void renderContainer(GL gl) {
				gl.glPushMatrix();
					Color color = Color.green;
					gl.glColor3d(color.getGreen(), color.getRed(), color.getBlue());
					gl.glRotated(180, 1, 0, 0);
					gl.glTranslated(0, -buttonSize, 0);
					gl.glBegin(GL.GL_TRIANGLE_STRIP);
						gl.glVertex2d(buttonSize/2, 0);
						gl.glVertex2d(buttonSize/2 + 0.3, buttonSize);
						gl.glVertex2d(buttonSize/2 - 0.3, buttonSize);
					gl.glEnd();
				gl.glPopMatrix();
			}
		};
		//bottom.setLayout( new PercentMarginLayout( -0.01, -0.9, -0.01, -0.01, -0.1, -1 ) );
		bottom.setName( "BOTTOM" );
		bottom.setLayout( new ILayout() {

			public double getHeight() {
				return buttonSize;
			}

			public double getWidth() {
				return ASlider3D.this.getWidth();
			}

			public double getX() {
				return 0;
			}

			public double getY() {
				return ASlider3D.this.getHeight() - buttonSize;
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
		
		
	}
	
	private double buttonSize = 3;
	private double sliderSize = 1.5;
	
	private void positionMiddle() {
		middle.setBounds(0, buttonSize + ( getHeight() - sliderSize - 2 * buttonSize ) * ( 1d - getPositionPercentage() ), sliderSize, getWidth());
	}
	
	protected void setPosition(int pos) {
		pos = Math.min(Math.max(getMin(), pos), getMax());
		
		scroll_position = pos;
		
		positionMiddle();
		
		System.err.println("now at : "+getMin()+" ("+getPositionPercentage()*100+"%) < " + getPosition() + " < " + getMax());
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
	
	protected int getPosition() {
		return scroll_position;
	}
	
	private double getPositionPercentage() {
		return ((double)(getPosition()-getMin())) / ((double)(getMax()-getMin()));
	}
	
	public void scrollUp() {
		setPosition(getPosition() - 1);
	}
	
	public void scrollDown() {
		setPosition(getPosition() + 1);
	}
}
