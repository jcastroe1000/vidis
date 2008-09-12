package vidis.ui.model.graph.layouts;

import java.util.List;

import vidis.data.sim.SimNode;

/**
 * this interface is implemented by all layouts for nodes
 * 
 * layouts are used to manipulate the position of the nodes
 * @author Dominik
 *
 */
public interface GraphLayout {
	/**
	 * apply the graph layout to the nodes
	 * @param nodes the list of all nodes
	 */
	public void apply(List<SimNode> nodes) throws Exception;
	
	/**
	 * the smaller the value, the more "dense" all points will be
	 * @param density the density to set (double [0..1])
	 */
	public void setNodeDensity(double density);
}
