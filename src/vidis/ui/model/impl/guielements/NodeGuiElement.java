package vidis.ui.model.impl.guielements;

import javax.media.opengl.GL;

import vidis.ui.model.impl.Node;
import vidis.ui.model.structure.AGuiContainer;

public class NodeGuiElement extends AGuiContainer {
	//private static Logger logger = Logger.getLogger(NodeGuiElement.class);
	
	private Node node;
	
	public NodeGuiElement(Node node) {
		this.node = node;
	}
	
	private Node getNode() {
		return node;
	}
	
	@Override
	public void renderContainer(GL gl) {
		// draw heading
		// FIXME
		// draw content, need to get variables
		getNode();
	}

}