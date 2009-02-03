package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.log4j.Logger;

import vidis.data.sim.SimNode;
import vidis.ui.config.Configuration;
import vidis.ui.model.graph.layouts.AGraphLayout;
import vidis.ui.model.graph.layouts.GraphLayout;

/**
 * grid layout for nodes
 * 
 * very nice output and low processing time
 * @author Dominik
 *
 */
public class GraphGridLayout extends AGraphLayout {
	private static Logger logger = Logger.getLogger( GraphGridLayout.class );
	
	private static GraphLayout instance = null;
	private final double xMin = Configuration.GRID_STEP;
	private final double xMax = Configuration.GRID_STEP;
	private final double zMin = Configuration.GRID_STEP;
	private final double zMax = Configuration.GRID_STEP;
	private double x;
	private double z;
	
	private List<Point3d> points;
	
	private GraphGridLayout() {
		points = new LinkedList<Point3d>();
		setNodeDensity(0.3);
	}
	
	public static GraphLayout getInstance() {
		if(instance == null)
			instance = new GraphGridLayout();
		return instance;
	}

	public void setNodeDensity(double density) {
		density = Math.max(0.0, density);
		density = Math.min(1.0, density);
		x = density * (xMax - xMin) + xMin;
		z = density * (zMax - zMin) + zMin;
	}
	
	private enum State {
		RIGHT,
		UP,
		LEFT,
		DOWN
	};
	
	private State state = State.DOWN;
	
	public Point3d generatePoint() {
		// generate next point
		int currentPosition = points.size();
		Point3d currentPoint;
		if(currentPosition > 0) {
			currentPoint = points.get(currentPosition-1);
		} else {
			currentPoint = new Point3d(0,0,0);
			points.add(currentPoint);
			return currentPoint;
		}
		Point3d nextPoint = new Point3d(currentPoint);
		// test where we are
		if ( nextPoint.z == 0 ) {
			generatePoint_zEQ0(nextPoint);
		} else if (nextPoint.x == 0) {
			generatePoint_xEQ0(nextPoint);
		} else if (nextPoint.x == nextPoint.z) {
			generatePoint_xEQz(nextPoint);
		} else {
			generatePoint_else(nextPoint);
		}
		points.add(nextPoint);
		return nextPoint;
	}

	private void generatePoint_else(Point3d nextPoint) {
		if(state == State.UP) {
			// go up
			nextPoint.z += z;
		} else if (state == State.LEFT) {
			// go left
			nextPoint.x -= x;
		} else if(state ==State.RIGHT) {
			// go right
			nextPoint.x += x;
		} else if(state == State.DOWN) {
			// go down
			nextPoint.z -= z;
		}
	}

	private void generatePoint_xEQz(Point3d nextPoint) {
		// middle, if coming from left, go down
		// if coming from down, go left
		if(state == State.RIGHT) {
			state = State.DOWN;
			nextPoint.z -= z;
		} else if (state == State.UP) {
			state = State.LEFT;
			nextPoint.x -= x;
		}
	}

	private void generatePoint_xEQ0(Point3d nextPoint) {
		// left bound, go up and then right
		if(state == State.LEFT) {
			state = State.UP;
			nextPoint.z += z;
		} else if (state == State.UP) {
			state = State.RIGHT;
			nextPoint.x += x;
		}
	}

	private void generatePoint_zEQ0(Point3d nextPoint) {
		// lower bound, go right and then up
		if(state == State.DOWN) {
			state = State.RIGHT;
			nextPoint.x += x;
		} else if (state == State.RIGHT ) {
			state = State.UP;
			nextPoint.z += z;
		}
	}
	
	public void apply(Collection<SimNode> nodes) throws Exception {
		state = State.DOWN;
		points.clear();
		oldNodes.clear();
		relayout(nodes);
//		GraphCenterLayout.getInstance().apply(nodesList);
	}
	
	public void relayout(Collection<SimNode> nodes) throws Exception {
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		for(int i=0; i<nodesList.size(); i++) {
			apply(nodesList.get(i));
		}
	}
	
	private void apply(SimNode s) {
		if(oldNodes.contains(s)) {
			// got to do nothing
			logger.debug("doing nothing for node: " + s);
		} else {
			logger.debug("apply new layout for node: " + s);
			// pick random point of the pool
			Point3d p = generatePoint();
			setPosition(s, p);
			// and now add it to the old nodes
			oldNodes.add(s);
		}
	}
}