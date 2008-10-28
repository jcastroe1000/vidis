package vidis.ui.events;

import org.apache.log4j.Logger;

public class JobAppend implements IVidisEvent {
	private static Logger logger = Logger.getLogger(JobAppend.class);
	
	private int id;
	private Runnable job;
	
	public JobAppend(int eventId, Runnable data) {
		this.id = eventId;
		this.job = data;
	}

	public int getID() {
		return id;
	}
	
	public Runnable getJob() {
		return job;
	}
}
