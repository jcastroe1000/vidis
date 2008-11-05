package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;
import javax.vecmath.Vector2d;

import org.apache.log4j.Logger;

import vidis.data.sim.SimNode;
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
	private final double xMin = 2;
	private final double xMax = 10;
	private final double yMin = 0;
	private final double yMax = 0;
	private final double zMin = 2;
	private final double zMax = 10;
	private double x;
	private double y;
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
		y = density * (yMax - yMin) + yMin;
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
			// lower bound, go right and then up
			if(state == State.DOWN) {
				state = State.RIGHT;
				nextPoint.x += x;
			} else if (state == State.RIGHT ) {
				state = State.UP;
				nextPoint.z += z;
			}
		} else if (nextPoint.x == 0) {
			// left bound, go up and then right
			if(state == State.LEFT) {
				state = State.UP;
				nextPoint.z += z;
			} else if (state == State.UP) {
				state = State.RIGHT;
				nextPoint.x += x;
			}
		} else if (nextPoint.x == nextPoint.z) {
			// middle, if coming from left, go down
			// if coming from down, go left
			if(state == State.RIGHT) {
				state = State.DOWN;
				nextPoint.z -= z;
			} else if (state == State.UP) {
				state = State.LEFT;
				nextPoint.x -= x;
			}
		} else {
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
		points.add(nextPoint);
		return nextPoint;
	}
	
	public void apply(Collection<SimNode> nodes) throws Exception {
		state = State.DOWN;
		points.clear();
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		for(int i=0; i<nodesList.size(); i++) {
			// pick random point of the pool
			Point3d p = generatePoint();
			setPosition(nodesList.get(i), p);
		}
		GraphCenterLayout.getInstance().apply(nodesList);
	}
}