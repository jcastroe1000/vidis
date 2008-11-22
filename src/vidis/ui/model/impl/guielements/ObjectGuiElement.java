package vidis.ui.model.impl.guielements;


import java.awt.Color;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.IVariableContainer;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.MouseMovedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Node;
import vidis.ui.model.impl.guielements.variableDisplays.CompositeScrollPane;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public abstract class ObjectGuiElement extends BasicGuiContainer {
	private static Logger logger = Logger.getLogger(ObjectGuiElement.class);

	protected Mode mode = Mode.MINIMIZED;
	
	protected BasicGuiContainer top;
	protected CompositeScrollPane scrollPane;
	
	public void ObjectGuiContainer() {
	}
	
	public void init() {
		this.color1 = Color.gray;
		this.color2 = Color.gray.brighter();
		this.setOpaque(true);
		
		
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
		};
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
	public void renderContainer(GL gl) {
		requireTextRenderer();
		
		double textH = this.textH * 0.01;
		switch ( mode ) {
		case MINIMIZED:
			double h2e = Mode.MINIMIZED.getHeight() / 2d;
			gl.glPushMatrix();
				gl.glTranslated(h2e, h2e, 0);
				renderObject( gl );
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glTranslated(2 * h2e, h2e-textH/2.0, 0);
				renderText2( gl );
			gl.glPopMatrix();
			break;
		case NORMAL:
		case EXPANDED2:
		case EXPANDED:
			double h2e3 = mode.getTopHeight() / 2d;
			double nfac3 = mode.getTopHeight() / 2d;
			gl.glPushMatrix();
				gl.glTranslated(h2e3, h2e3 + mode.getTopY(), 0);
				gl.glScaled(nfac3, nfac3, 1d);
				renderObject( gl );
			gl.glPopMatrix();
			gl.glPushMatrix();
				gl.glScaled(10, 10, 1);
				gl.glTranslated(2 * h2e3, mode.getHeight()-textH/2.0-1d, 0);
				
				renderText2( gl );
			gl.glPopMatrix();
			break;
		}
		
		super.renderContainer(gl);
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
	protected abstract void renderText2( GL gl );
	
}
