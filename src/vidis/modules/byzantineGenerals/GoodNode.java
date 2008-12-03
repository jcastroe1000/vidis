package vidis.modules.byzantineGenerals;

import org.apache.log4j.Logger;

public class GoodNode extends ANode {
	private static Logger logger = Logger.getLogger(GoodNode.class);
	
	@Override
	protected NodeType getNodeType() {
		return NodeType.GOOD;
	}
}
