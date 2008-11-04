package vidis.ui.model.impl.guielements.popupWindows;

import java.awt.Color;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.model.impl.Label;
import vidis.ui.model.impl.PercentMarginLayout;
import vidis.ui.model.impl.guielements.PopupWindow;
import vidis.ui.model.impl.guielements.scrollpane.ScrollPane3D;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

public class OpenMSIMFilePopupWindow extends PopupWindow {
	private static Logger logger = Logger.getLogger(OpenMSIMFilePopupWindow.class);
	
	private Map<String, List<File>> moduleFiles = new HashMap<String, List<File>>();
	private Map<String, File> moduleFilesMapGui = new HashMap<String, File>();
	
	private ScrollPane3D moduleFilesScrollPane;
	
	private static IGuiContainer instance = null;
	
	private int counter = 0;
	
	private void clean() {
		counter = 0;
		moduleFiles.clear();
		moduleFilesScrollPane.removeAllChilds();
		moduleFilesMapGui.clear();
	}
	
	/**
	 * refresh buffer (less IO critical operations due to this function)
	 */
	public void refresh() {
		logger.info( "refresh();" );
		// first do a clean
		clean();
		
		// then load list and refresh gui
		List<String> modules = ResourceManager.getModules();
		for(String module : modules) {
			// restore module's file list
			if(! moduleFiles.containsKey( module )) {
				moduleFiles.put(module, new LinkedList<File>());
			}
			// add files to the list
			moduleFiles.get( module ).addAll(ResourceManager.getModuleFiles(module));
			
			// refresh gui
//			Label tmp = new Label( module );
//			tmp.setBounds(1, 1, 4, 18);
//			moduleFilesScrollPane.addChild( tmp );
			
			for(File moduleFile : moduleFiles.get( module )) {
				String title = module + " -> " + moduleFile.getName();
				moduleFilesMapGui.put(title, moduleFile);
				Label tmp = new Label( title ) {
					@Override
					protected void onMouseClicked(MouseClickedEvent e) {
						super.onMouseClicked(e);
						// extract file
						File file = moduleFilesMapGui.get( getText() );
						// load msim file
						Dispatcher.forwardEvent( new VidisEvent<File>(IVidisEvent.SimulatorLoad, file) );
						// close this one
						OpenMSIMFilePopupWindow.this.getParent().removeChild( OpenMSIMFilePopupWindow.this );
					}
				};
				moduleFilesScrollPane.addChild( tmp );
			}
		}
	}
	
	public OpenMSIMFilePopupWindow() {
		super();
		Label tmp = new Label("Please pick a module file You want to load");
		tmp.setLayout( new PercentMarginLayout(0, -0.93, 0, 0, -0.07, -1) );
		tmp.setTextColor( Color.LIGHT_GRAY );
		addChild( tmp );
		
		moduleFilesScrollPane = new ScrollPane3D();
		moduleFilesScrollPane.setLayout( new PercentMarginLayout(0,0,0,-0.07,-0.93,-1) );
		
		addChild( moduleFilesScrollPane );
	}
	
	public File getSelectedMSIMFile() {
		return null;
	}
	
	@Override
	public void renderContainer(GL gl) {
		super.renderContainer(gl);
		if(counter%100 == 0)
			refresh();
		counter++;
	}

	public static IGuiContainer getInstance() {
		if(instance == null)
			instance = new OpenMSIMFilePopupWindow();
		return instance;
	}
}
