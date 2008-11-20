package vidis.sim.classloader.modules.impl;

import java.util.List;

import vidis.sim.classloader.modules.interfaces.IModuleComponent;

public abstract class AModule implements IModuleComponent {
	public abstract List<AModuleFile> getModuleFiles();

	public abstract String getName();
	
	public boolean isModuleFile() {
		return false;
	}
	public boolean isModule() {
		return true;
	}
}
