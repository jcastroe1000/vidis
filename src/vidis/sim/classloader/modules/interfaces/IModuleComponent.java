package vidis.sim.classloader.modules.interfaces;


/**
 * this interface basically does nothing but is used
 * within the gui
 * @author Dominik
 *
 */
public interface IModuleComponent {
	/**
	 * retrieves the name of this module.
	 * This name can possibly be the filename or a identifier,
	 * description or both out of some meta information contained
	 * within JAR archives or similar.
	 * @return the string name
	 */
	String getName();
	
	/**
	 * retrieves if this is a module file (MSIM-File).
	 * @return true ifit is a loadable module file (MSIM-File)
	 */
	boolean isModuleFile();
	
	/**
	 * retrieves if this is a module container (no MSIM-File).
	 * @return true if it is a module container (no MSIM-File)
	 */
	boolean isModule();
	
}
