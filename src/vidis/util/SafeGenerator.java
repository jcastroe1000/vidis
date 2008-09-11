package vidis.util;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.vecmath.Point3d;

import vidis.data.sim.SimLink;
import vidis.data.sim.SimNode;
import vidis.util.graphs.graph.Vertex;
import vidis.util.graphs.graph.WeightedGraph;
import vidis.util.graphs.graph.WeightedGraphImpl;
import vidis.util.graphs.graph.algorithm.ShortestPathAlgorithm;
import vidis.util.graphs.graph.algorithm.ShortestPathAlgorithmDijkstra;
import vidis.util.graphs.util.HeapNodeComparator;

/**
 * very safe generator even safer for unsafe operations made safe xD
 * 
 * @author dominik
 * 
 */
public class SafeGenerator {
	private static final double aMin = 0.15;
	private static final double aMax = 1.0;
	private static final double dMin = 1.0;
	private static final double dMax = 6;
	private static double a;
	private static double d;
	static {
		setNodeDensity(.2);
	}
	private static List<Point3d> points = new LinkedList<Point3d>();

	private static double spirale_rt(double t) {
		return a * t;
	}

	private static double spirale_st(double t) {
		return a / 2 * (Math.log(Math.sqrt(t + 1) + 1) + t * Math.sqrt(t * t + 1));
	}

	private static double spirale_xt(double t) {
		return spirale_rt(t) * Math.cos(t);
	}

	private static double spirale_yt(double t) {
		return 0;
	}

	private static double spirale_zt(double t) {
		return spirale_rt(t) * Math.sin(t);
	}

	/**
	 * retrieve the next point for a node
	 * 
	 * @return a point3d
	 */
	public static Point3d nextNodePoint3d() {
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
		return tmp;
	}

	public static void reset() {
		points.clear();
	}

	/**
	 * the smaller the value, the more "dense" all points will be
	 * 
	 * @param density
	 *          the density to set (double [0..1])
	 */
	public static void setNodeDensity(double density) {
		density = Math.max(0.0, density);
		density = Math.min(1.0, density);
		a = density * (aMax - aMin) + aMin;
		d = density * (dMax - aMin) + dMin;
	}

	/**
	 * generate positions using the nodes, checking their connections and
	 * then applying some fancy algorithm over a adjacence matrix
	 * @param nodes a list of nodes (THAT MUST BE CONNECTED)
	 * @return mapping for node to a unique point in the universe
	 */
	public static void generateByDistance(List<SimNode> nodes) throws Exception {
		// init graph
		WeightedGraph graph = new WeightedGraphImpl( false );
		
		// init vertices
		Map<SimNode, Vertex> vertices = new HashMap<SimNode, Vertex>();
		for(int i=0; i<nodes.size(); i++) {
			SimNode node_a = nodes.get(i);
			if(!vertices.containsKey(node_a)) {
				Vertex vertex_a = new Vertex(node_a);
				vertices.put(node_a, vertex_a);
				graph.add(vertex_a);
			}
			for(int j=0; j<nodes.size(); j++) {
				SimNode node_b = nodes.get(j);
				if(!vertices.containsKey(node_b)) {
					Vertex vertex_b = new Vertex(node_b);
					vertices.put(node_b, vertex_b);
					graph.add(vertex_b);
				}
				SimLink link = generateByDistance_getConnectedLink(node_a, node_b);
				if(link != null) {
					// fine, they are directly connected; set distance = link.getDelay()
					graph.addEdge(vertices.get(node_a), vertices.get(node_b), link.getDelay());
				}
			}
		}
		
		// execute dijkstra on it
		ShortestPathAlgorithm spa = new ShortestPathAlgorithmDijkstra( graph, new HeapNodeComparator(-1) );
		
		for(int i=0; i<nodes.size(); i++) {
			SimNode a = nodes.get(i);
			for(int j=0; j<nodes.size(); j++) {
				SimNode b = nodes.get(j);
				if(i != j) {
					List<Vertex> path = spa.getShortestPath(vertices.get(a), vertices.get(b));
					if(path != null) {
						// FIXME find fine and nice positions for the variables
						double shortest = spa.getDistance(vertices.get(a), vertices.get(b));
//						System.err.println(shortest +" ("+a+","+b+") -> " + path);
					}
				}
			}
		}
	}

	private static SimLink generateByDistance_getConnectedLink(SimNode node_a, SimNode node_b) {
		List<SimLink> node_a_links = node_a.getConnectedLinksSim();
		for(int i=0; i<node_a_links.size(); i++) {
			SimLink link = node_a_links.get(i);
			SimNode tmp = link.getOtherNode(node_a);
			if(tmp.equals(node_b)) {
				return link;
			}
		}
		return null;
	}
}