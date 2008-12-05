package vidis.ui.events.jobs;

import org.apache.log4j.Logger;

public abstract class ARelayoutJob implements ILayoutJob {
	private static Logger logger = Logger.getLogger(ARelayoutJob.class);
	public void run() {
		try {
			getLayout().relayout(getNodes());
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
