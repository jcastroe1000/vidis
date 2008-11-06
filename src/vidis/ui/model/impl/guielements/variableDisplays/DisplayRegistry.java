package vidis.ui.model.impl.guielements.variableDisplays;

import java.util.Collection;
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

import vidis.data.var.AVariable;

/**
 * holds all known displays and decides which should be instantiated
 * @author Christoph
 *
 */
public class DisplayRegistry {
	private static Logger logger = Logger.getLogger(DisplayRegistry.class);

	private static Map<Class<?>, Display> knownTypes = new HashMap<Class<?>, Display>();
	
	public static void registerDisplay( Class<?> clazz, Display display ) {
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
		registerDisplay( String.class, new StringDisplay() );
		
		Tuple2Display tuple2 = new Tuple2Display();
		registerDisplay( Tuple2i.class, tuple2 );
		registerDisplay( Tuple2d.class, tuple2 );
		registerDisplay( Tuple2f.class, tuple2 );
		
		Tuple3Display tuple3 = new Tuple3Display();
		registerDisplay( Tuple3b.class, tuple3 );
		registerDisplay( Tuple3i.class, tuple3 );
		registerDisplay( Tuple3d.class, tuple3 );
		registerDisplay( Tuple3f.class, tuple3 );
		
		registerDisplay( Number.class, new NumberDisplay());
		registerDisplay( Integer.TYPE, new PrimitiveIntDisplay());
		registerDisplay( Byte.TYPE, new PrimitiveByteDisplay());
		registerDisplay( Double.TYPE, new PrimitiveDoubleDisplay());
		registerDisplay( Float.TYPE, new PrimitiveFloatDisplay());
		registerDisplay( Long.TYPE, new PrimitiveLongDisplay());
		
		registerDisplay( Map.class, new MapDisplay());
		
		//FIXME this thingy is buggy and running with vartest causes system instability
//		registerDisplay( Collection.class, new CollectionDisplay() );
	}
	
	public static Display createDisplay( AVariable var ) {
		requireInit();
		Class<?> c = var.getDataType();
		logger.debug( "creating display for " + c + ":" + var );
		for ( Class<?> key : knownTypes.keySet() ) {
			if ( key.isAssignableFrom( c ) ) {
				System.err.println("DISPLAY for "+ c +"{"+var.getIdentifier()+"} = " + key + " through " + knownTypes.get(key).getClass());
				return knownTypes.get( key ).newInstance( var );
			}
		}
		System.err.println("SKIPPED DISPLAY for "+ c +"{"+var.getIdentifier()+"}");
		// Fallback to String
		return knownTypes.get( String.class ).newInstance( var );
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
