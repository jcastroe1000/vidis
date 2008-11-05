package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector2d;

import org.apache.log4j.Logger;

import vidis.data.sim.SimNode;
import vidis.data.var.AVariable;
import vidis.ui.model.graph.layouts.AGraphLayout;
import vidis.ui.model.graph.layouts.GraphLayout;

/**
 * center layout for nodes
 * 
 * basically takes the nodes, calculates middle point and moves them
 * towards 0,0,0
 * 
 * @author Dominik
 *
 */
public class GraphCenterLayout extends AGraphLayout {
	private static Logger logger = Logger.getLogger( GraphCenterLayout.class );
	
	private static GraphLayout instance = null;
	
	private GraphCenterLayout() {
		setNodeDensity(0.3);
	}
	
	public static GraphLayout getInstance() {
		if(instance == null)
			instance = new GraphCenterLayout();
		return instance;
	}

	public void setNodeDensity(double density) {
		density = Math.max(0.0, density);
		density = Math.min(1.0, density);
	}
	
	private Point3d calculateCenterPoint(List<SimNode> nodes) {
		Point3d center = new Point3d(0, 0, 0);
		if(nodes.size() > 0) {
			for(int i=0; i<nodes.size(); i++) {
				SimNode node = nodes.get(i);
				Object posO = node.getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION).getData();
				if(posO instanceof Point3d) {
					Point3d posP3D = (Point3d)posO;
					center.add(posP3D);
				}
			}
			center.x = center.x / nodes.size();
			center.y = center.y / nodes.size();
			center.z = center.z / nodes.size();
		}
		return center;
	}
	
	private void addPoint(Point3d point, List<SimNode> nodes) {
		for(int i=0; i<nodes.size(); i++) {
			SimNode node = nodes.get(i);
			Object posO = node.getVariableById(AVariable.COMMON_IDENTIFIERS.POSITION).getData();
			if(posO instanceof Point3d) {
				Point3d posP3D = (Point3d)posO;
				posP3D.add( point );
				setPosition(node, posP3D);
			}
		}
	}
	
	public void apply(Collection<SimNode> nodes) throws Exception {
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		Point3d centerPoint = calculateCenterPoint(nodesList);
		// now subtract the offset between centerPoint and 0,0,0 from each nodes position
		Point3d zero = new Point3d(0, 0, 0);
		zero.sub( centerPoint );
		addPoint(zero, nodesList);
		
	}
}