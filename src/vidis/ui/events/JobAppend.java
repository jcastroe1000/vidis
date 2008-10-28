package vidis.ui.events;

import vidis.ui.events.jobs.IJob;

/**
 * the job append event
 * 
 * the job scheduler handles this event by executing the job
 * @author Dominik
 *
 */
public class JobAppend implements IVidisEvent {
	private IJob job;
	
	/**
	 * this is the default constructor taking one argument
	 * @param data the job to be appended to the execution queue
	 */
	public JobAppend(IJob data) {
		this.job = data;
	}

	/**
	 * the id of this job
	 * @see IVidisEvent
	 */
	public int getID() {
		return IVidisEvent.AppendJob;
	}
	
	/**
	 * the job to be executed by the scheduler
	 * @return the job
	 */
	public IJob getJob() {
		return job;
	}
}
