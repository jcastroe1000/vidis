package vidis.ui.events.jobs;

import org.apache.log4j.Logger;

public abstract class ALayoutJob implements ILayoutJob {
	private static Logger logger = Logger.getLogger(ALayoutJob.class);
	public void run() {
		try {
			getLayout().apply(getNodes());
		} catch (Exception e) {
			logger.error(e);
		}
	}

}
