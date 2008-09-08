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
