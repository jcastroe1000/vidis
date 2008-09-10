package vidis.util;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ImageIcon;

/**
 * This class provides the Resources in a static way to all other classes
 * 
 * @author Christoph
 * 
 */
public class ResourceManager {
	private ResourceManager() {
	}

	// private static final long initialMemory;
	// static {
	// initialMemory = getMemoryUsed();
	// DebugJoe.watchObject(new MemoryWatcher());
	// }

	private static String pathSeperator;
	private static String rootPath;
	private static String shaderPath;
	private static String dataPath;
	private static String modulesPath;
	/**
	 * static constructor getting the path seperator TODO: Warning this needs to
	 * be testet for other platforms works on Windows 2000,
	 * 
	 * NOTE BY DOMINIK: edited pathSeparator for all supported java platforms
	 * 
	 * This script is based on the fact that (hopefully) every vm has path to
	 * resources.jar in its sun.boot.class.path It simply takes the symbol
	 * before resources.jar as the path seperator
	 */
	static {
		pathSeperator = File.separator;
		rootPath = "." + pathSeperator;
		shaderPath = rootPath + "shaders" + pathSeperator;
		dataPath = rootPath + "data";
		modulesPath = dataPath + pathSeperator + "modules";
	}

	/**
	 * This Function translates the shader name into a valid path
	 * 
	 * @param name
	 * @return valid shader path
	 */
	public static String getShaderPath(String name) {
		return shaderPath + name;
	}

	public static String getShaderCode(String name) {
		// FIXME
		return "";
	}

	public static final long getMemoryTotal() {
		return Runtime.getRuntime().totalMemory();
	}

	public static final long getMemoryFree() {
		return Runtime.getRuntime().freeMemory();
	}

	public static final long getMemoryMax() {
		return Runtime.getRuntime().maxMemory();
	}

	public static final long getMemoryTotalUsed() {
		return getMemoryTotal() - getMemoryFree();
	}

	public static ImageIcon getImageIcon(String icon) {
		return new ImageIcon(dataPath + pathSeperator + "resources" + pathSeperator + icon);
	}
	
	public static List<String> getModules() {
		List<String> modules = new ArrayList<String>();
		File moduleFolder = new File(modulesPath);
		if(moduleFolder.exists()) {
			if(moduleFolder.isDirectory()) {
				for(File module : moduleFolder.listFiles()) {
					if(module.isDirectory()) {
						modules.add(module.getName());
					}
				}
			}
		}
		return modules;
	}
	
	public static List<File> getModuleFiles(String module) {
		List<File> moduleFiles = new ArrayList<File>();
		File moduleFolder = new File(modulesPath + pathSeperator + module);
		if(moduleFolder.exists()) {
			if(moduleFolder.isDirectory()) {
				for(File moduleFile : moduleFolder.listFiles()) {
					if(moduleFile.isFile() && moduleFile.canRead()) {
						moduleFiles.add(moduleFile);
					}
				}
			}
		}
		return moduleFiles;
	}

	public static File getModuleFile(String module, String moduleFile) {
		return new File(modulesPath + pathSeperator + module + pathSeperator + moduleFile);
	}
	
	// --------------------- fonts
	public static String FONT_ARIAL = "ARIAL.TTF";
	public static String FONT_ARIAL_BOLD = "ARIALBD.TTF";
	public static String FONT_ARIAL_BOLD_ITALIC = "ARIALBI.TTF";
	public static String FONT_ARIAL_ITALIC = "ARIALI.TTF";
	public static String FONT_COURIER = "COUR.TTF";
	public static String FONT_COURIER_BOLD = "COURBD.TTF";
	public static String FONT_COURIER_BOLD_ITALIC = "COURBI.TTF";
	public static String FONT_COURIER_ITALIC = "COURI.TTF";
	public static String FONT_TIMESNEWROMAN = "TIMES.TTF";
	public static String FONT_TIMESNEWROMAN_BOLD = "TIMESBD.TTF";
	public static String FONT_TIMESNEWROMAN_BOLD_ITALIC = "TIMESBI.TTF";
	public static String FONT_TIMESNEWROMAN_ITALIC = "TIMESI.TTF";
	public static String FONT_VERDANA = "verdana.TTF";
	public static String FONT_VERDANA_BOLD = "verdanab.TTF";
	public static String FONT_VERDANA_BOLD_ITALIC = "verdanaz.TTF";
	public static String FONT_VERDANA_ITALIC = "verdanai.TTF";
	
	private Map<String, Font> fontCache = new HashMap<String, Font>();
	
	private Font createFont(String filename) throws FontFormatException, IOException {
		Font font = Font.createFont(Font.TRUETYPE_FONT, new File(dataPath + pathSeperator + "resources" + pathSeperator + "fonts" + pathSeperator + filename));
		return font;
	}
	
	
	/**
	 * retrieves a font
	 * the font is created and cached by the resource manager
	 * @param filename
	 * @return
	 */
	public Font getFont(String filename) throws FontFormatException, IOException {
		Font font = fontCache.get( filename );
		if ( font == null ) {
			font = createFont( filename );
			fontCache.put( filename, font ); 
		}
		return font;
	}
	
	/**
	 * retrieves a font
	 * the font is created and cached by the resource manager, then the size is applied
	 * the size is not cached
	 * @param filename
	 * @return
	 */
	public Font getFont(String filename, float size) throws FontFormatException, IOException {
		Font font = fontCache.get( filename );
		if ( font == null ) {
			font = createFont( filename );
			fontCache.put( filename, font ); 
		}
		return font.deriveFont( size );
	}	

	// public static final long getMemoryInit() {
	// return initialMemory;
	// }

	// public static final long getMemoryUsed() {
	// return getMemoryTotalUsed() - getMemoryInit();
	// }

	// private static final String formatMemory(String name, double value) {
	// String[] unit = { "b", "kb", "mb", "gb" };
	// int i = 0;
	// while (i < unit.length && value > 1024d) {
	// i++;
	// value /= 1024d;
	// }
	// return String.format("%12s %5.3f%s", name, value, unit[i]);
	// }

	// private static class MemoryWatcher {
	// public String toString() {
	// return formatMemory("Total:", getMemoryTotal()) + "\n" +
	// formatMemory("Free:", getMemoryFree()) + "\n" + formatMemory("Max:",
	// getMemoryMax()) + "\n" + formatMemory("Init:", getMemoryInit()) + "\n" +
	// formatMemory("TotalUsed:", getMemoryTotalUsed()) + "\n" +
	// formatMemory("Used:", getMemoryUsed());
	// }
	// }
}
