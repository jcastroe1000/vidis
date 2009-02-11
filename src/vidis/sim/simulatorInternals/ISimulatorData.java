/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
