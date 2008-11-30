package vidis.ui.gui;

import org.apache.log4j.Logger;

public abstract class MenuAction {
	private static Logger logger = Logger.getLogger(MenuAction.class);

	public abstract void execute( Menu menu, MenuItem item );
}
