/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.modules.vectorClockAlgorithm;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * vector time class .. coolest of all
 * 
 * stores time vectors for nodes
 * @author Dominik
 *
 */
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
		int i=0;
		for (VectorClockAlgorithmNode node : getNodes()) {
			if(i > 0)
		    	buff.append(",");
		    buff.append(timeVector.get(node));
		    i++;
		}
		buff.append(")");
		return buff.toString();
    }

	public int getNodeTime(VectorClockAlgorithmNode node) {
		return timeVector.get(node).intValue();
	}
}
