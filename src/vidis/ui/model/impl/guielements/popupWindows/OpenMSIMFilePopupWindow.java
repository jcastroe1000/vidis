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
	
	private static IGuiContainer instance = null;
	
	private Map<String, List<File>> moduleFiles = new HashMap<String, List<File>>();
	private Map<String, File> moduleFilesMapGui = new HashMap<String, File>();
	private Map<String, Boolean> expandedModules = new HashMap<String, Boolean>();
	
	private ScrollPane3D moduleFilesScrollPane;
	
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
		List<File> modules = ResourceManager.getModules();
		for(File module : modules) {
			// restore module's file list
			if(! moduleFiles.containsKey( module )) {
				moduleFiles.put(module.getName(), new LinkedList<File>());
			}
			// add files to the list
			moduleFiles.get( module ).addAll(ResourceManager.getModuleFiles(module));
			
			// refresh gui
			Label tmpX = new Label( module.getName() ) {
				@Override
				protected void onMouseClicked(MouseClickedEvent e) {
//					super.onMouseClicked(e);
					if(expandedModules.containsKey( getText() )) {
						expandedModules.remove( getText() );
					} else {
						expandedModules.put(getText(), true);
					}
				}
			};
			tmpX.setBounds(1, 1, 4, 18);
			moduleFilesScrollPane.addChild( tmpX );
			
			if(expandedModules.containsKey( module )) {
				for(File moduleFile : moduleFiles.get( module )) {
					String title = "     " + moduleFile.getName();
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
							close();
						}
					};
					tmp.setBounds(1, 1, 7, 18);
					moduleFilesScrollPane.addChild( tmp );
				}
			}
		}
	}
	
	public OpenMSIMFilePopupWindow() {
		super("Please pick a module file You want to load");
		
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
