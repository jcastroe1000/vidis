package vidis.ui.model.impl.guielements;


import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.IVariableContainer;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.guielements.variableDisplays.CompositeScrollPane;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public abstract class ObjectGuiElement extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(ObjectGuiElement.class);

	protected Mode mode = Mode.MINIMIZED;
	
	protected BasicGuiContainer top;
	protected CompositeScrollPane scrollPane;
	private IGuiContainer onScreenLabel;
	
	
	public void ObjectGuiContainer() {
		requireTextRenderer();
	}
	
	public void init() {
		setColor1( Color.white );
		setColor2( Color.white );
		this.setOpaque(true);
		onScreenLabel = new BasicGuiContainer() {
			@Override
			public double getHeight() {
				return Mode.EXPANDED.getTopHeight() * 2d/3d;
			}
			@Override
			public double getWidth() {
				return 14;
			}
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				ObjectGuiElement.this.onMouseClicked(e);
			}
			
			@Override
			public void renderContainer(GL gl) {
				super.renderContainer(gl);
				double h = getHeight();
				double w = getWidth();
				gl.glPushMatrix();
					gl.glTranslated(0, 0, 0);
					renderTextToRect(gl, getHeaderLine1(), Color.white, 0, h / 2d, w, h / 2d );
					renderTextToRect(gl, getHeaderLine2(), Color.white, 0, 0, w, h / 2d );
				gl.glPopMatrix();
			}
		};
		((BasicGuiContainer)onScreenLabel).setColor1( Color.BLACK );
		((BasicGuiContainer)onScreenLabel).setColor2( Color.BLACK );
		top = new BasicGuiContainer() {
			@Override
			public double getHeight() {
				return mode.getTopHeight();
			}
			@Override
			public double getY() {
				return mode.getTopY();
			}
			@Override
			public double getWidth() {
				return ObjectGuiElement.this.getWidth();
			}
			@Override
			protected void onMouseClicked(MouseClickedEvent e) {
				ObjectGuiElement.this.onMouseClicked(e);
			}
			
			@Override
			public void renderContainer(GL gl) {
				super.renderContainer(gl);
				switch ( mode ) {
				case MINIMIZED:
					double h2e = Mode.MINIMIZED.getHeight() / 2d;
					gl.glPushMatrix();
						gl.glTranslated(h2e, h2e, 0);
						renderObject( gl );
					gl.glPopMatrix();
					gl.glPushMatrix();
						gl.glTranslated(2 * h2e, 0, 0);
						renderTextToRect(gl, getHeaderLine1(), Color.white, 0, 0, getWidth(), mode.getTopHeight() );
					gl.glPopMatrix();
					break;
				case NORMAL:
				case EXPANDED2:
				case EXPANDED:
					double h2e3 = mode.getTopHeight() / 2d;
					double nfac3 = mode.getTopHeight() / 2d;
					gl.glPushMatrix();
						gl.glTranslated(h2e3, h2e3, 0);
						gl.glScaled(nfac3, nfac3, 1d);
						renderObject( gl );
					gl.glPopMatrix();
					gl.glPushMatrix();
						gl.glTranslated(2 * h2e3, 0, 0);
						renderTextToRect(gl, getHeaderLine1(), Color.white, 0, mode.getTopHeight() / 2d, getWidth(), mode.getTopHeight() / 2d );
						renderTextToRect(gl, getHeaderLine2(), Color.white, 0, 0, getWidth(), mode.getTopHeight() / 2d );
					gl.glPopMatrix();
					break;
				}
			}
		};
		top.setColor1( Color.DARK_GRAY );
		top.setColor2( Color.black );
		top.setOpaque( true );
		this.addChild( top );
		
		scrollPane = new CompositeScrollPane( fetchVariableContainer() );
		scrollPane.setLayout( new ILayout() {

			public double getHeight() {
				return mode.getMainHeight();
			}

			public double getWidth() {
				return ObjectGuiElement.this.getWidth();
			}

			public double getX() {
				return 0;
			}

			public double getY() {
				return mode.getMainY();
			}

			public void layout() {
				// nothing dude
			}

			public void setGuiContainer(IGuiContainer c) {
				// nothing dude
			}
			
		});
		this.addChild( scrollPane );
		
	}

	@Override
	public void setHighlighted( boolean highlighted ) {
		top.setHighlighted( highlighted );
		super.setHighlighted( highlighted );
	}
	
	@Override
	public double getWantedHeight() {
		return mode.getHeight();
	}

	@Override
	protected void onMouseClicked(MouseClickedEvent e) {
		if ( mode == Mode.MINIMIZED ) mode = Mode.NORMAL;
		else if ( mode == Mode.NORMAL ) mode = Mode.EXPANDED;
		else if ( mode == Mode.EXPANDED ) mode = Mode.EXPANDED2;
		else if ( mode == Mode.EXPANDED2 ) mode = Mode.MINIMIZED;
		
		
	}
	
	protected abstract CompositeScrollPane initScrollPane();
	protected abstract IVariableContainer fetchVariableContainer();
	
	protected abstract void renderObject( GL gl );
	protected void renderText2( GL gl ){
		renderTextToRect(gl, getHeaderLine1(), Color.white, 0, 0, getWidth(), mode.getTopHeight() );
	}
	
	private void renderTextToRect( GL gl, String text, Color textColor, double x, double y, double w, double h ) {
		Rectangle2D r = textRenderer.getBounds( text );
		float scale = 0.01f;
		double factor = 0.7;
		double fontScaleHeight = (h * factor) / (textH * scale);
		double fontScale = fontScaleHeight;
		gl.glPushMatrix();
			gl.glTranslated(x, y, 0);
			textRenderer.setColor( textColor );
			textRenderer.begin3DRendering();
			textRenderer.draw3D( text, 
					(float) (0), 
					(float) (h / 2f - r.getHeight() * scale * fontScale / 2f),
					(float) (myZ + 2 * Z_OFFSET),
					(float) (scale * fontScale) );
			textRenderer.end3DRendering();
		gl.glPopMatrix();
	}
	
	protected abstract String getHeaderLine1();
	protected abstract String getHeaderLine2();

	public IGuiContainer getOnScreenLabel() {
		return onScreenLabel;
	}
}
