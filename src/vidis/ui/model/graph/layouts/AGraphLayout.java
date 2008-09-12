package vidis.ui.model.graph.layouts;

import javax.vecmath.Point3d;

import vidis.data.sim.SimNode;
import vidis.data.var.AVariable;
import vidis.data.var.vars.DefaultVariable;

/**
 * the abstract class for all graph layouts
 * @author Dominik
 *
 */
public abstract class AGraphLayout {
	/**
	 * set the position for a node
	 * @param node the node to set the position
	 * @param pos the position
	 */
	protected void setPosition(SimNode node, Point3d pos) {
		if(node.hasVariable(AVariable.COMMON_IDENTIFIERS.POSITION)) {
			AVariable posVar = node.getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION);
			if(posVar instanceof DefaultVariable) {
				((DefaultVariable)posVar).update(pos);
			} else {
				node.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.POSITION, pos));
			}
		} else {
			node.registerVariable(new DefaultVariable(AVariable.COMMON_IDENTIFIERS.POSITION, pos));
		}
	}
}
