package vidis.ui.events;

import vidis.ui.events.jobs.IJob;

public class JobAppend implements IVidisEvent {
	private int id;
	private IJob job;
	
	public JobAppend(int eventId, IJob data) {
		this.id = eventId;
		this.job = data;
	}

	public int getID() {
		return id;
	}
	
	public IJob getJob() {
		return job;
	}
}
