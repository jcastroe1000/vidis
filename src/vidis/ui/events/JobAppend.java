/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
