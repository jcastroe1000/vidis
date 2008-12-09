package vidis.ui.mvc;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.JobAppend;
import vidis.ui.events.jobs.IJob;
import vidis.ui.mvc.api.AController;

/**
 * a job controller that takes jobs and executes them
 * @author Dominik
 *
 */
public class JobController extends AController {
	private static Logger logger = Logger.getLogger(JobController.class);
	
	private BlockingQueue<Runnable> jobs = new ArrayBlockingQueue<Runnable>( 150 );
	private ThreadPoolExecutor executer = new ThreadPoolExecutor( 3, 3, 30, TimeUnit.SECONDS, jobs);
	
	public JobController() {
		registerEvent( IVidisEvent.AppendJob );
	}
	
	private Map<IJob, Future<?>> futureJobs = new HashMap<IJob, Future<?>>();
	private List<IJob> futureJobsToRemove = new LinkedList<IJob>();
	
	@Override
	public void handleEvent(IVidisEvent event) {
		switch ( event.getID() ) {
			case IVidisEvent.AppendJob:
				if ( event instanceof JobAppend ) {
//					jobs.offer(((JobAppend)event).getJob());
					IJob j = ((JobAppend)event).getJob();
					boolean submit = true;
					if( j.mustExecuteUniquely() ) {
						// check futures
						for(Entry<IJob, Future<?>> e : futureJobs.entrySet()) {
							if(e.getValue().isDone()) {
								// clean done jobs
								futureJobsToRemove.add(e.getKey());
							} else {
								if(e.getKey().getClass().equals(j.getClass())) {
									// cancel old job
									if(e.getValue().cancel(true)) {
										// job cancelled, fine
									} else {
										// could not be cancelled, do not submit new job
										submit = false;
									}
								}
							}
						}
						// remove unused futures
						for(IJob tmp : futureJobsToRemove)
							futureJobs.remove(tmp);
						futureJobsToRemove.clear();
					}
					if(submit) {
						Future<?> fj = executer.submit(j);
						futureJobs.put(j, fj);
					}
//					// just execute it
//					executer.execute( j );
				} else
					logger.warn("received a job that I cannot handle; claims to be append job event but the class is not compatible!");
				break;
		}
	}
}
