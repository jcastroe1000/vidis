package vidis.ui.events.jobs;

public interface IJob extends Runnable {

	/**
	 * Determines if this job can be run only in a semi-singleton mode.
	 * <p>
	 * Semi-singleton mode means, that if a job of the same Class is already
	 * executing or is planned for execution, this job won't be submitted.
	 * </p>
	 * @return true or false
	 */
	boolean mustExecuteUniquely();

}
