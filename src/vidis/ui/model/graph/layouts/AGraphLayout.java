package vidis.ui.model.graph.layouts;

import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;

import vidis.data.sim.SimNode;
import vidis.data.var.vars.AVariable;
import vidis.data.var.vars.DefaultVariable;
import vidis.ui.model.graph.layouts.impl.GraphCenterLayout;
import vidis.ui.model.graph.layouts.impl.GraphElectricSpringLayout;
import vidis.ui.model.graph.layouts.impl.GraphRandomLayout;
import vidis.ui.model.graph.layouts.impl.GraphSpiralLayout;

/**
 * the abstract class for all graph layouts
 * @author Dominik
 *
 */
public abstract class AGraphLayout implements GraphLayout {
	protected List<SimNode> oldNodes = new LinkedList<SimNode>();
	
	public static void setNodeDensityToAll(double density) {
		GraphElectricSpringLayout.getInstance().setNodeDensity(density);
		GraphCenterLayout.getInstance().setNodeDensity(density);
		GraphRandomLayout.getInstance().setNodeDensity(density);
		GraphSpiralLayout.getInstance().setNodeDensity(density);
	}
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
	
	protected Point3d getPosition(SimNode node) {
		if(node.hasVariable(AVariable.COMMON_IDENTIFIERS.POSITION)) {
			AVariable posVar = node.getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION);
			return (Point3d) posVar.getData();
		} else {
			return new Point3d(0, 0, 0);
		}
	}
}
