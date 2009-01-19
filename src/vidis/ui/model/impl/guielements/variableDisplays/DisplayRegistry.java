package vidis.ui.model.impl.guielements.variableDisplays;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple2i;
import javax.vecmath.Tuple3b;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple3i;

import org.apache.log4j.Logger;

import vidis.data.var.vars.AVariable;

/**
 * holds all known displays and decides which should be instantiated
 * @author Christoph
 *
 */
public class DisplayRegistry {
	private static Logger logger = Logger.getLogger(DisplayRegistry.class);

	private static Map<Class<?>, Class<? extends Display>> knownTypes = new HashMap<Class<?>, Class<? extends Display>>();
	
	public static void registerDisplay( Class<?> clazz, Class<? extends Display> display ) {
		knownTypes.put( clazz, display );
	}
	
	{
		requireInit();
	}
	
	private static void requireInit() {
		if ( ! init ) {
			init();
			init = true;
		}
	}
	
	private static boolean init = false;
	private static void init() {
		registerDisplay( String.class, StringDisplay.class );
		
		registerDisplay( Tuple2i.class, Tuple2Display.class );
		registerDisplay( Tuple2d.class, Tuple2Display.class );
		registerDisplay( Tuple2f.class, Tuple2Display.class );
		
		registerDisplay( Tuple3b.class, Tuple3Display.class );
		registerDisplay( Tuple3i.class, Tuple3Display.class );
		registerDisplay( Tuple3d.class, Tuple3Display.class );
		registerDisplay( Tuple3f.class, Tuple3Display.class );
		
		registerDisplay( Number.class, NumberDisplay.class);
		registerDisplay( Integer.TYPE, PrimitiveIntDisplay.class);
		registerDisplay( Byte.TYPE, PrimitiveByteDisplay.class);
		registerDisplay( Double.TYPE, PrimitiveDoubleDisplay.class);
		registerDisplay( Float.TYPE, PrimitiveFloatDisplay.class);
		registerDisplay( Long.TYPE, PrimitiveLongDisplay.class);
		
		registerDisplay( Map.class, MapDisplay.class);
		
		registerDisplay( Void.TYPE, MethodDisplay.class );
		
		//FIXME this thingy is buggy and running with vartest causes system instability
//		registerDisplay( Collection.class, new CollectionDisplay() );
	}
	
	public static Display createDisplay( AVariable var ) {
		requireInit();
//		if ( var instanceof MethodVariable ) {
//			logger.info( "new methodDisplay for " + var );
//			return new MethodDisplay( (MethodVariable) var );
//		}
//		else {
			Class<?> c = var.getDataType();
			logger.debug( "creating display for " + c + ":" + var );
			for ( Class<?> key : knownTypes.keySet() ) {
				if ( key.isAssignableFrom( c ) ) {
//					System.err.println("DISPLAY for "+ c +"{"+var.getIdentifier()+"} = " + key + " through " + knownTypes.get(key));
					//return knownTypes.get( key ).newInstance( var );
					return createDisplay(knownTypes.get(key), var);
				}
			}
//			System.err.println("SKIPPED DISPLAY for "+ c +"{"+var.getIdentifier()+"}");
			// Fallback to String
			return createDisplay(knownTypes.get( String.class ),  var );
//		}
	}
	
	private static Constructor<?> getRightConstructor(Class<? extends Display> c) {
		Constructor<?>[] constructors = c.getConstructors();
		// fetch correct constructor
		Constructor<?> constructor = null;
		for (int i = 0; i < constructors.length; i++) {
			// match empty constructor
			if (constructors[i].getParameterTypes().length == 1) {
				if (AVariable.class.isAssignableFrom(constructors[i].getParameterTypes()[0])) {
					constructor = constructors[i];
				}
				break;
			}
		}
		return constructor;
	}

	
	private static Display createDisplay(Class<? extends Display> clazz, AVariable var) {
		Constructor<?> constructor = getRightConstructor(clazz);
		if (constructor != null) {
			// instance IUserLink
			Object obj;
			try {
				obj = constructor.newInstance(var);
				return (Display) obj;
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			throw new RuntimeException("CANNOT CREATE CLASS: " + clazz + "; constructor not found!");
		}
		return null;
	}
	
//	public static Display createDisplay( Object data ) {
//		Class c = data.getClass();
//		for ( Class key : knownTypes.keySet() ) {
//			if ( key.isAssignableFrom( c ) ) {
//				return knownTypes.get( key ).newInstance( data );
//			}
//		}
//		// Fallback to String
//		return knownTypes.get( String.class ).newInstance( data );
//	}
}
