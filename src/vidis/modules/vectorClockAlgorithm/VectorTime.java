package vidis.modules.vectorClockAlgorithm;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class VectorTime {
    private Map<VectorClockAlgorithmNode, Integer> timeVector = new ConcurrentHashMap<VectorClockAlgorithmNode, Integer>();

    public VectorTime(VectorClockAlgorithmNode vectorClockAlgorithmNode, int i) {
    	update(vectorClockAlgorithmNode, i);
    }

    public VectorTime() {
		// nothing
	}

	public void update(VectorClockAlgorithmNode node, int time) {
		if (timeVector.containsKey(node)) {
		    if (timeVector.get(node).compareTo(time) < 0) {
		    	timeVector.put(node, time);
		    }
		} else {
		    timeVector.put(node, time);
		}
    }

    public void update(VectorTime remoteTimeVector) {
		for (VectorClockAlgorithmNode node : remoteTimeVector.getNodes()) {
		    update(node, remoteTimeVector.timeVector.get(node));
		}
    }

    public Set<VectorClockAlgorithmNode> getNodes() {
    	return timeVector.keySet();
    }

    public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("(");
		for (VectorClockAlgorithmNode node : getNodes()) {
		    buff.append(timeVector.get(node) + ",");
		}
		buff.append(")");
		return buff.toString();
    }

	public int getNodeTime(VectorClockAlgorithmNode node) {
		return timeVector.get(node).intValue();
	}
}
