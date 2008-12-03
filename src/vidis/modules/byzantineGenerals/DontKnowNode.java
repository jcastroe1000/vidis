package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

public class DontKnowNode extends ANode {
	private static Logger logger = Logger.getLogger(DontKnowNode.class);
	
	@Override
	protected NodeType getNodeType() {
		return NodeType.DONTKNOW;
	}
}
