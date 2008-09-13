package vidis.data.annotation;

/**
 * this enumeration determines where a annotated variable
 * should be displayed
 * 
 * THIS ENUM IS DEPRECATED, AS IT IS THE DISPLAY ANNOTATION!
 * 
 * @author Dominik
 * @see Display
 */
@Deprecated
public enum DisplayType implements Comparable<DisplayType> {
	/**
	 * determines that it should be shown in the 3d world
	 */
	SHOW_3D,
	/**
	 * determines that it should be shown in the gui element
	 */
	SHOW_SWING,
	/**
	 * determines that it should be shown in both, 3d world and gui
	 */
	SHOW_3D_AND_SWING,
	/**
	 * determines that this option should be executable
	 */
	EXECUTEABLE;
}
