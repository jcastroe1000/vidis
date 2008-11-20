package vidis.sim.classloader.modules.interfaces;

/**
 * this interface basically does nothing but is used
 * within the gui
 * @author Dominik
 *
 */
public interface IModuleComponent {
	/**
	 * retrieve the name of this module
	 * @return the string name
	 */
	String getName();
	
	boolean isModuleFile();
	
	boolean isModule();

}
