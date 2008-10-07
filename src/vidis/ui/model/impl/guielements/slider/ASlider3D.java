package vidis.ui.model.impl.guielements.slider;

import java.awt.Color;

import javax.media.opengl.GL;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;

public abstract class ASlider3D extends BasicGuiContainer {
	protected static final int VERTICAL = 0;
	protected static final int HORIZONTAL = 1;
	
	private BasicGuiContainer line;
	private BasicGuiContainer top;
	private BasicGuiContainer bottom;
	private BasicGuiContainer middle;
	private int type;
	private double position;
	
	private void lineOnMouseClick() {
		System.err.println("line clicked");
	}
	
	private void topOnMouseClick() {
		System.err.println("top clicked");
	}
	
	private void bottomOnMouseClick() {
		System.err.println("bottom clicked");
	}
	
	private void middleOnMouseClick() {
		System.err.println("middle clicked");
	}
	
	public ASlider3D(int type) {
		this.type = type;
		
		setPosition(0.1);
		
		// instantiate line
		line = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				lineOnMouseClick();
			}
		};
		line.setColor1(Color.red);
		addChild(line);
		
		// top
		top = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				topOnMouseClick();
			}
			@Override
			public double getWidth() {
				return 0.5;
			}
			@Override
			public double getHeight() {
				return 0.8;
			}
			@Override
			public void renderContainer(GL gl) {
				gl.glPushMatrix();
					gl.glRotated(180, 1, 0, 0);
					gl.glBegin(GL.GL_TRIANGLES);
						gl.glVertex2d(0, 0);
						gl.glVertex2d(getWidth(), getHeight());
						gl.glVertex2d(-getWidth(), getHeight());
					gl.glEnd();
					
				gl.glPopMatrix();
			}
		};
		addChild(top);
		
		// bottom
		bottom = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				bottomOnMouseClick();
			}
			@Override
			public double getWidth() {
				return 0.5;
			}
			@Override
			public double getHeight() {
				return 0.8;
			}
			@Override
			public void renderContainer(GL gl) {
				gl.glPushMatrix();
					gl.glBegin(GL.GL_TRIANGLES);
						gl.glVertex2d(0, 0);
						gl.glVertex2d(getWidth(), getHeight());
						gl.glVertex2d(-getWidth(), getHeight());
					gl.glEnd();
					
				gl.glPopMatrix();
			}
		};
		bottom.setColor1(Color.green);
		addChild(bottom);
		
		// middle
		middle = new BasicGuiContainer() {
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				middleOnMouseClick();
			}
		};
		middle.setColor1(Color.pink);
		addChild(middle);
	}
	
	public boolean isVertical() {
		return type == VERTICAL;
	}
	
	public boolean isHorizontal() {
		return type == HORIZONTAL;
	}
	
	/**
	 * the position of the slider
	 * @return a double within [0..1]
	 */
	public double getPosition() {
		return position;
	}
	
	/**
	 * sets the position
	 * @param pos position within [0..1]
	 */
	public void setPosition(double pos) {
		this.position = pos;
	}
	
	@Override
	public void renderContainer(GL gl) {
		// set line size
		line.setBounds(0, 0, getParent().getHeight(), .2);
		// set middle position and size
		middle.setBounds(middle.getX(), getHeight() * getPosition(), middle.getHeight(), middle.getWidth());
		// top triangle
		top.setBounds(0, 0, top.getHeight(), top.getWidth());
		// bottom triangle
		bottom.setBounds(0, getHeight(), bottom.getHeight(), bottom.getWidth());
	}

}
