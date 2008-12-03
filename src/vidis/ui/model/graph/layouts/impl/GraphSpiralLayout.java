package vidis.ui.model.graph.layouts.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javax.vecmath.Point3d;

import org.apache.log4j.Logger;

import vidis.data.sim.SimNode;
import vidis.ui.model.graph.layouts.AGraphLayout;
import vidis.ui.model.graph.layouts.GraphLayout;

/**
 * spiral layout for nodes
 * 
 * very nice output and low processing time
 * @author Dominik
 *
 */
public class GraphSpiralLayout extends AGraphLayout {
	private static Logger logger = Logger.getLogger( GraphSpiralLayout.class );
	private static GraphLayout instance = null;
	private final double aMin = 0.15;
	private final double aMax = 1.0;
	private final double dMin = 1.0;
	private final double dMax = 6;
	private double a;
	private double d;
	
	private List<Point3d> points;
	
	private GraphSpiralLayout() {
		points = new LinkedList<Point3d>();
		setNodeDensity(0.3);
	}
	
	public static GraphLayout getInstance() {
		if(instance == null)
			instance = new GraphSpiralLayout();
		return instance;
	}

	public void setNodeDensity(double density) {
		density = Math.max(0.0, density);
		density = Math.min(1.0, density);
		a = density * (aMax - aMin) + aMin;
		d = density * (dMax - aMin) + dMin;
	}
	
	private double spirale_rt(double t) {
		return a * t;
	}

	private double spirale_st(double t) {
		return a / 2 * (Math.log(Math.sqrt(t + 1) + 1) + t * Math.sqrt(t * t + 1));
	}

	private double spirale_xt(double t) {
		return spirale_rt(t) * Math.cos(t);
	}

	private double spirale_yt(double t) {
		return 0;
	}

	private double spirale_zt(double t) {
		return spirale_rt(t) * Math.sin(t);
	}

	/**
	 * retrieve the next point for a node
	 * 
	 * @return a point3d
	 */
	public Point3d nextNodePoint3d() {
		Point3d tmp = new Point3d();
		double pi64 = (Math.PI / 64);
		// distance
		double t = 0.0;
		while (spirale_st(t) <= (points.size() + 1) * d) {
			t += pi64;
		}
		tmp.x = spirale_xt(t);
		tmp.y = spirale_yt(t);
		tmp.z = spirale_zt(t);
		points.add(tmp);
		logger.debug(tmp);
		return tmp;
	}
	
	public void apply(Collection<SimNode> nodes) throws Exception {
		// reset stuff
		points.clear();
		oldNodes.clear();
		// call relayout
		relayout(nodes);
		// apply center layout
		GraphCenterLayout.getInstance().apply(nodes);
	}
	
	private void apply(SimNode s) {
		setPosition(s, nextNodePoint3d());
		oldNodes.add(s);
	}
	
	public void relayout(Collection<SimNode> nodes) throws Exception {
		List<SimNode> nodesList = new ArrayList<SimNode>(nodes);
		for(int i=0; i<nodesList.size(); i++) {
			SimNode s = nodesList.get(i);
			if(oldNodes.contains(s)) {
				// already has a position
			} else {
				// get a new one for it
				apply(s);
			}
		}
	}
}
