package vidis.ui.events.jobs.jobs.layouts;

import org.apache.log4j.Logger;

import vidis.ui.events.jobs.ILayoutJob;

public abstract class ALayoutJob implements ILayoutJob {
	private static Logger logger = Logger.getLogger(ALayoutJob.class);
	public void run() {
		try {
			getLayout().apply(getNodes());
		} catch (Exception e) {
			logger.error(e);
		}
	}
	public boolean mustExecuteUniquely() {
		return false;
	}

}
