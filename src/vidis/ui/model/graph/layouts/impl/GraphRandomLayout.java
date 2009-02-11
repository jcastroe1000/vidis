/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.log4j.Logger;

import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.AGraphLayout;
import vidis.ui.model.graph.layouts.IGraphLayout;

/**
 * very fast and easy to calculate layout
 * 
 * it places the nodes somewhere :-)
 * 
 * @author Dominik
 *
 */
public class GraphRandomLayout extends AGraphLayout {
	private final double helper = 10;
	
	private final double xMin_min = -helper;
	private final double xMin_max = -helper;
	private final double yMin_min = 0;
	private final double yMin_max = 0;
	private final double zMin_min = -helper;
	private final double zMin_max = -helper;
	private final double xMax_min = helper;
	private final double xMax_max = helper;
	private final double yMax_min = 0;
	private final double yMax_max = 0;
	private final double zMax_min = helper;
	private final double zMax_max = helper;
	
	private double xMax, xMin, yMax, yMin, zMax, zMin;

	private static Logger logger = Logger.getLogger(GraphRandomLayout.class);
	
	private GraphRandomLayout() {
		setNodeDensity(0.4);
	}

	private static IGraphLayout instance = null;
	
	public static IGraphLayout getInstance() {
		if(instance == null)
			instance = new GraphRandomLayout();
		return instance;
	}
	
	public void apply(Collection<SimNode> nodes) throws Exception {
		logger.debug("generate positions in {["+xMin+".."+xMax+"],["+yMin+".."+yMax+"],["+zMin+".."+zMax+"]}");
		oldNodes.clear();
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		for(int i=0; i<nodesList.size(); i++) {
			positionNode(nodesList.get(i));
		}
		GraphCenterLayout.getInstance().apply(nodesList);
	}
	
	public void relayout(Collection<SimNode> nodes) throws Exception {
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		for(int i=0; i<nodesList.size(); i++) {
			SimNode s = nodesList.get(i);
			if(oldNodes.contains(s)) {
				// do nothing
			} else {
				// get a nice new position for it
				positionNode(s);
			}
		}
	}
	
	private void positionNode(SimNode node) {
		Point3d random = new Point3d();
		random.x = Math.random()*xMax + xMin;
		random.y = Math.random()*yMax + yMin;
		random.z = Math.random()*zMax + zMin;
		setPosition(node, random);
		oldNodes.add(node);
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
