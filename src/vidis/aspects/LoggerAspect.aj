/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
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
