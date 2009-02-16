/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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

	public static final double GRID_STEP = 2.0;
	
	/**
	 * just display wireframe
	 */
	public static boolean DISPLAY_WIREFRAME = false;
	
	public static boolean NICE_LINKS = true;
	public static boolean NICE_NODES = true;
	
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
	public static double DETAIL_LEVEL = .0;
	public static boolean USE_AUTOMATIC_DETAIL_LEVEL = true;
	public static int USE_AUTOMATIC_DETAIL_LEVEL_COUNTER = 250;

	/**
	 * the angle we look down (x axis)
	 */
	public static double LOOK_ANGLE_X = 0;
	
	/**
	 * the angle we look down (y axis)
	 */
	public static double LOOK_ANGLE_Y = 0;
}
