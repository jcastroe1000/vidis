package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

public class BadNode extends ANode {
	private static Logger logger = Logger.getLogger(BadNode.class);
	
	@Override
	protected NodeType getNodeType() {
		return NodeType.BAD;
	}
}
