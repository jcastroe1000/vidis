package vidis.sim.simulatorInternals;

import java.io.Serializable;
import java.util.List;

import vidis.data.sim.AComponent;

public interface ISimulatorData extends Serializable {
	public void executeComponents();
	public long getTime();
	public void reset();
	public void killComponents();
	public void registerComponent(AComponent component);
	public void unregisterComponent(AComponent component);
	public List<AComponent> getComponents();
}
