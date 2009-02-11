/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.graph.layouts;

import java.util.Collection;

import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.impl.GraphCenterLayout;
import vidis.ui.model.graph.layouts.impl.GraphElectricSpringLayout;
import vidis.ui.model.graph.layouts.impl.GraphGridLayout;
import vidis.ui.model.graph.layouts.impl.GraphRandomLayout;
import vidis.ui.model.graph.layouts.impl.GraphSpiralLayout;

/**
 * this interface is implemented by all layouts for nodes
 * 
 * layouts are used to manipulate the position of the nodes
 * @author Dominik
 * @see GraphElectricSpringLayout
 * @see GraphRandomLayout
 * @see GraphSpiralLayout
 * @see GraphCenterLayout
 * @see GraphGridLayout
 */
public interface IGraphLayout {
	/**
	 * apply the graph layout to the nodes
	 * @param nodes the list of all nodes
	 */
	public void apply(Collection<SimNode> nodes) throws Exception;
	
	/**
	 * should be called if some nodes were removed or added
	 * @param nodes all nodes
	 * @throws Exception thrown upon error
	 */
	public void relayout(Collection<SimNode> nodes) throws Exception;
	
	/**
	 * the smaller the value, the more "dense" all points will be
	 * @param density the density to set (double [0..1])
	 */
	public void setNodeDensity(double density);
}
