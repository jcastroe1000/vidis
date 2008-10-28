package vidis.ui.mvc;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import vidis.ui.events.IVidisEvent;
import vidis.ui.events.JobAppend;
import vidis.ui.mvc.api.AController;

public class JobController extends AController {
	private static Logger logger = Logger.getLogger(JobController.class);
	
	private BlockingQueue<Runnable> jobs = new ArrayBlockingQueue<Runnable>( 150 );
	private ThreadPoolExecutor executer = new ThreadPoolExecutor( 3, 3, 30, TimeUnit.SECONDS, jobs) {
		@Override
		protected void afterExecute(Runnable r, Throwable t) {
			super.afterExecute(r, t);
			logger.debug("finished job: " + r);
		}
		@Override
		protected void beforeExecute(Thread t, Runnable r) {
			super.beforeExecute(t, r);
			logger.debug("starting job: " + r + " @ " + t);
		}
	};
	
	public JobController() {
		registerEvent( IVidisEvent.AppendJob );
	}
	
	@Override
	public void handleEvent(IVidisEvent event) {
		switch ( event.getID() ) {
			case IVidisEvent.AppendJob:
				if ( event instanceof JobAppend )
//					jobs.offer(((JobAppend)event).getJob());
					executer.execute( ((JobAppend)event).getJob() );
				else
					logger.warn("received a job that I cannot handle; claims to be append job event but the class is not compatible!");
				break;
		}
	}
}
