/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.aspects;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.media.opengl.GL;

import org.apache.log4j.Logger;

public aspect ProfilingAspect {
	private static Logger profileLogger = Logger.getLogger( "vidis.profiler" );
	private Map<Class, Long> timelog = new ConcurrentHashMap<Class, Long>();	
	
	private void log( Class clazz, String method, long time ) {
		profileLogger.error( "" + clazz.getSimpleName() +" " + method + " -> " + time + "ms" );
	}
	
	pointcut renderMethod( GL gl ) : execution( void dvidis..*(..) ) && args( gl );
	
	void around( GL gl ) : renderMethod( gl ) {
		long time = System.currentTimeMillis();
		proceed( gl );
		time = System.currentTimeMillis() - time;
		Class clazz = thisJoinPoint.getSignature().getDeclaringType();
		String method = thisJoinPoint.getSignature().getName();
		log( clazz, method, time );
	}
}
