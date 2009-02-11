/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.sim.simulatorInternals;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import vidis.data.sim.AComponent;
import vidis.data.sim.IComponent;

public class SimulatorData implements ISimulatorData {
	private static Logger logger = Logger.getLogger(SimulatorData.class);
	/**
	 * serial version UID
	 */
	private static final long serialVersionUID = -948286353824490701L;

	public SimulatorData() {
		now = 0;
		this.components = new LinkedList<AComponent>();
	}

	private long now;
	private List<AComponent> components;

	public long getTime() {
		return now;
	}

	public void registerComponent(AComponent component) {
		logger.debug("registerComponent("+component+");");
		components.add(component);
	}

	public void unregisterComponent(AComponent component) {
		logger.debug("UNregisterComponent("+component+");");
		components.remove(component);
	}

	public void executeComponents() {
		logger.debug("simulating: " + now);
		synchronized (components) {
			for (IComponent component : components) {
				component.execute();
			}
			now++;
		}
	}

	public void reset() {
		resetTime();
	}

	private void resetTime() {
		this.now = 0;
	}

	public void killComponents() {
		synchronized (components) {
			for (IComponent component : components) {
				component.kill();
			}
		}
	}

	public List<AComponent> getComponents() {
		return components;
	}
}
