package vidis.ui.model.impl.guielements.variableDisplays;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.MethodVariable;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.Button;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.ILayout;

public class MethodDisplay extends Display {
	private static Logger logger = Logger.getLogger(MethodDisplay.class);

	private Button exec;
	
	public MethodDisplay ( AVariable v ) {
		super(v);
		final MethodVariable var = (MethodVariable) v;
		exec = new Button() {
			protected void onMouseClicked(MouseClickedEvent e) {
				if ( var.getMethodExpectsParameters() ) {
					// FIXME
				}
				else {
					var.getData();
				}
			}
		};
		
		exec.setText( v.getIdentifier() );
		this.setText( "" );
		this.addChild( exec );
		exec.setLayout(new ILayout() {
			public double getHeight() {
				return 1.5;
			}
			public double getWidth() {
				return MethodDisplay.this.getWidth();
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
	}
	
	@Override
	public double getWantedHeight() {
		return 1.5;
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new CollectionDisplay( var );
//	}
	
	@Override
	public void renderContainer(GL gl) {
		super.renderContainer(gl);
	}
}
