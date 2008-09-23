package vidis.ui.config;

import java.io.File;

/**
 * a basic configuration class that holds all information about configurations
 * @author Dominik
 *
 */
public class Configuration {
	/**
	 * library path for vidis
	 */
	public static final String LIBRARY_PATH = "lib";
	
	/**
	 * jogl version
	 */
	public static final String JOGL_VERSION = "jogl-1.1.1";
	
	/**
	 * data path for vidis
	 */
	public static final String DATA_PATH = "data";
	
	/**
	 * log path for vidis
	 */
	public static final String LOG_PATH = DATA_PATH + File.separatorChar + "logs";
	
	/**
	 * modules path for vidis
	 */
	public static final String MODULE_PATH = DATA_PATH + File.separatorChar + "modules";
	
	/**
	 * resources path for vidis
	 */
	public static final String RESOURCE_PATH = DATA_PATH + File.separatorChar + "resources";
	
	/**
	 * shaders path for vidis
	 */
	public static final String SHADER_PATH = DATA_PATH + File.separatorChar + "shaders";

	/**
	 * jogl path for vidis
	 */
	public static final String JOGL_PATH = LIBRARY_PATH + File.separatorChar + JOGL_VERSION;
	
	/**
	 * just display wireframe
	 */
	public static boolean DISPLAY_WIREFRAME = true;
	
	/**
	 * adjustable detail level within [0 .. 1] (where 0 is poor, 1 is best)
	 * 
	 * NOTE: 1 may give best display, 0 best performance (a HEAVY load of performance), 0.5 is a good half-way
	 * 
	 * FURTHER NOTE: a value <0 is not supported and may cause software crash!
	 * 
	 * LAST NOTE: value >1 possible, but only on high-end personal computers that are REALLY fast;
	 * 			  the value of 1.0 was adapted for a Core2 Duo 2.6GHz with nVidia Quadro FX 370, this
	 *            means that your system should be equal or faster than that to use a detail level>>1
	 */
	public static double DETAIL_LEVEL = 0.5;

	/**
	 * the angle we look down (x axis)
	 */
	public static double LOOK_ANGLE_X = 0;
	
	/**
	 * the angle we look down (y axis)
	 */
	public static double LOOK_ANGLE_Y = 0;
}
