package vidis.ui.model.impl.guielements.variableDisplays;

import java.awt.Color;
import java.util.HashSet;
import java.util.Set;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.MouseClickedEvent;
import vidis.ui.model.impl.BasicGuiContainer;
import vidis.ui.model.impl.Label;

/**
 * 
 * @author Christoph
 *
 */
public class GroupDisplay extends Label {
	private static Logger logger = Logger.getLogger(GroupDisplay.class);

	private boolean showContent = true;
	private String ns = "no namespace set";
	
	
	
	private GroupDisplay () {
		this.setText( "> namespace" );
		this.setTextColor( Color.WHITE );
		this.setBackColor( Color.BLACK );
		this.setOpaque( true );
	}
	
	private Set<BasicGuiContainer> content;
	
	public GroupDisplay( String ns ) {
		this();
		this.content = new HashSet<BasicGuiContainer>();
		this.ns = ns;
	}
	
	public GroupDisplay( String ns, boolean showContent ) {
		this( ns );
		this.setShowContent( showContent );
	}
	
	@Override
	public void renderContainer(GL gl) {
		this.setText( (getShowContent()?" v ":" > ") + ns );
		super.renderContainer(gl);
	}
	
	public boolean getShowContent() {
		return showContent;
	}
	
	public void setShowContent( boolean showContent ) {
		this.showContent = showContent;
		for ( BasicGuiContainer d : content ) {
			d.setVisible( showContent );
		}
	}
	
	@Override
	protected void onMouseClicked(MouseClickedEvent e) {
		if ( showContent ) {
			setShowContent( false );
		}
		else {
			setShowContent( true );
		}
	}
	
	public void addContent( BasicGuiContainer d ) {
		this.content.add( d );
		d.setVisible( getShowContent() );
	}
	public void removeContent( BasicGuiContainer d ) {
		this.content.remove( d );
	}

	
	public Set<BasicGuiContainer> getContent() {
		return this.content;
	}
}
