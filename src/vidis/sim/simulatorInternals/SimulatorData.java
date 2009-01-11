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
