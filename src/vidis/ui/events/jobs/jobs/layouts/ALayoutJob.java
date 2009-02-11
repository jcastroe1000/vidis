/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.events.jobs.jobs.layouts;

import org.apache.log4j.Logger;

import vidis.ui.events.jobs.ILayoutJob;

/**
 * abstract layouting job
 * @author Dominik
 *
 */
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
