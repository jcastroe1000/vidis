package vidis.ui.model.graph.layouts.impl;

import java.util.List;

import javax.vecmath.Point3d;

import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.AGraphLayout;

/**
 * very fast and easy to calculate layout
 * 
 * it places the nodes somewhere :-)
 * 
 * @author Dominik
 *
 */
public class GraphRandomLayout extends AGraphLayout {
	private final double xMin_min = -40;
	private final double xMin_max = -40;
	private final double yMin_min = -40;
	private final double yMin_max = -40;
	private final double zMin_min = -40;
	private final double zMin_max = -40;
	private final double xMax_min = 40;
	private final double xMax_max = 40;
	private final double yMax_min = 40;
	private final double yMax_max = 40;
	private final double zMax_min = 40;
	private final double zMax_max = 40;
	
	private double xMax, xMin, yMax, yMin, zMax, zMin;
	
	public GraphRandomLayout() {
		setNodeDensity(0.4);
	}
	
	public void apply(List<SimNode> nodes) throws Exception {
		for(int i=0; i<nodes.size(); i++) {
			Point3d random = new Point3d();
			random.x = Math.random()*xMax + xMin;
			random.y = Math.random()*yMax + yMin;
			random.z = Math.random()*zMax + zMin;
			SimNode node = nodes.get(i);
			setPosition(node, random);
		}
	}

	public void setNodeDensity(double density) {
		density = Math.max(0.0, density);
		density = Math.min(1.0, density);
		xMin = density * (xMin_max - xMin_min) + xMin_min;
		xMax = density * (xMax_max - xMax_min) + xMax_min;
		yMin = density * (yMin_max - yMin_min) + yMin_min;
		yMax = density * (yMax_max - yMax_min) + yMax_min;
		zMin = density * (zMin_max - zMin_min) + zMin_min;
		zMax = density * (zMax_max - zMax_min) + zMax_min;
	}
}
