package vidis.sim.classloader.modules.interfaces;

public interface IModuleComponent {
	/**
	 * retrieves the name of this module.
	 * This name can possibly be the filename or a identifier,
	 * description or both out of some meta information contained
	 * within JAR archives or similar.
	 * @return the string name
	 */
	String getName();
}
