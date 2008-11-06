package vidis.ui.model.impl.guielements.variableDisplays;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.vecmath.Tuple2d;
import javax.vecmath.Tuple2f;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;

import org.apache.log4j.Logger;

import vidis.data.var.AVariable;

/**
 * holds all known displays and decides which should be instantiated
 * @author Christoph
 *
 */
public class DisplayRegistry {
	private static Logger logger = Logger.getLogger(DisplayRegistry.class);

	@SuppressWarnings( "unchecked" )
	private static Map<Class, Display> knownTypes = new HashMap<Class, Display>();
	
	@SuppressWarnings( "unchecked" )
	public static void registerDisplay( Class clazz, Display display ) {
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
		
		TupleDisplay tuple = new TupleDisplay();
		registerDisplay( Tuple2d.class, tuple );
		registerDisplay( Tuple3d.class, tuple );
		registerDisplay( Tuple2f.class, tuple );
		registerDisplay( Tuple3f.class, tuple );
		
		registerDisplay( Collection.class, new CollectionDisplay() );
	}
	
	@SuppressWarnings( "unchecked" )
	public static Display createDisplay( AVariable var ) {
		requireInit();
		Class c = var.getData().getClass();
		logger.debug( "creating display for " + c + ":" + var );
		for ( Class key : knownTypes.keySet() ) {
			if ( key.isAssignableFrom( c ) ) {
				return knownTypes.get( key ).newInstance( var );
			}
		}
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
