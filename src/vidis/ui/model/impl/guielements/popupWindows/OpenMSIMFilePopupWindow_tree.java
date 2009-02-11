/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.popupWindows;

import org.apache.log4j.Logger;

import vidis.sim.classloader.modules.impl.AModule;
import vidis.sim.classloader.modules.impl.AModuleFile;
import vidis.sim.classloader.modules.interfaces.IModuleComponent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.model.impl.PercentMarginLayout;
import vidis.ui.model.impl.guielements.PopupWindow;
import vidis.ui.model.impl.guielements.tree.Tree;
import vidis.ui.model.impl.guielements.tree.TreeElement;
import vidis.ui.mvc.api.Dispatcher;
import vidis.util.ResourceManager;

public class OpenMSIMFilePopupWindow_tree extends PopupWindow {
	private static Logger logger = Logger.getLogger(OpenMSIMFilePopupWindow_tree.class);
	private static OpenMSIMFilePopupWindow_tree instance;
	public static OpenMSIMFilePopupWindow_tree getInstance() {
		if( instance == null)
			instance = new OpenMSIMFilePopupWindow_tree("Please pick a module You want to load");
		return instance;
	}
	
	public OpenMSIMFilePopupWindow_tree(String title) {
		super(title);
		Tree<IModuleComponent> tree = new Tree<IModuleComponent>("Modules", null) {
			@Override
			protected void clickedOn(TreeElement<IModuleComponent> element) {
				if( element.getObject() != null) {
					logger.info("CLICKED ON: " + element.getObject().getName() );
					if(element.getObject().isModule()) {
	//					// module directory, expand/close
	//					if(element.isExpanded())
	//						element.collapse();
	//					else
	//						element.expand();
						// refresh display
	//					refresh();
					} else {
						// module file, open
						Dispatcher.forwardEvent(new VidisEvent<IModuleComponent>(IVidisEvent.SimulatorLoad, element.getObject()));
						// close this popup
						close();
					}
				} else {
					// clicked on root element
				}
			}
		};
		for( AModule module : ResourceManager.getModules() ) {
			TreeElement<IModuleComponent> treeModule = tree.createTreeElement(module.getName(), module);
			tree.addChild( treeModule );
			for( AModuleFile moduleFile : module.getModuleFiles()) {
				treeModule.addChild(
						tree.createTreeElement(moduleFile.getName(), (IModuleComponent) moduleFile)
				);
			}
		}
		tree.refresh();
		tree.setLayout( new PercentMarginLayout(0.001,0.001,0.001,0.001,-1,-1));
		tree.getScrollPane().setLayout(new PercentMarginLayout(0.001,0.001,0.001,-0.07,-0.93,-1));
		this.addChild( tree.getScrollPane() );
	}
	
	@Override
	public void setVisible(boolean visible) {
		if ( visible == true ) {
			handleReload();
		}
		super.setVisible(visible);
	}
	
	/**
	 * @dominik FIXME handle reload here
	 * called every time this dialog is shown
	 */
	private void handleReload() {
		
	}
}
