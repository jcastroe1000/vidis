package aspects;

import org.apache.log4j.Logger;

public aspect LoggerAspect issingleton() {
	
	
	// FIXME not active because of the d in dvidis
	pointcut anyMethod( Object o ) : execution( * dvidis..*(..) ) && this( o );
	  
	@SuppressWarnings( "unchecked" )
	before( Object o ) : anyMethod( o ) {
		Class clazz = o.getClass();
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		Logger.getLogger( clazz ).debug( "("+thisJoinPoint.getStaticPart().getSourceLocation()+") entering Method " + methodName );
	}
	
	@SuppressWarnings( "unchecked" )
	after( Object o ) returning : anyMethod( o ) {
		Class clazz = o.getClass();
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		Logger.getLogger( clazz ).debug( "("+thisJoinPoint.getStaticPart().getSourceLocation()+") leaving Method " + methodName );
	}
	
	@SuppressWarnings( "unchecked" )
	after( Object o ) throwing : anyMethod( o ) {
		Class clazz = o.getClass();
		String className = clazz.getName();
		String methodName = thisJoinPointStaticPart.getSignature().getName();
		Logger.getLogger( clazz ).error( "("+thisJoinPoint.getStaticPart().getSourceLocation()+")" + " left through exception " + className+ " " + methodName );
		
	}
}
