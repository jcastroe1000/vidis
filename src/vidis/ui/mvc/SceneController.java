package vidis.ui.mvc;

import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.media.opengl.GL;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCanvas;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLCapabilitiesChooser;
import javax.media.opengl.GLContext;
import javax.media.opengl.GLEventListener;
import javax.vecmath.Point3d;
import javax.vecmath.Point4d;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

import org.apache.log4j.Logger;

import vidis.ui.config.Configuration;
import vidis.ui.events.AMouseEvent;
import vidis.ui.events.CameraEvent;
import vidis.ui.events.IVidisEvent;
import vidis.ui.events.MouseClickedEvent;
import vidis.ui.events.ObjectEvent;
import vidis.ui.events.VidisEvent;
import vidis.ui.input.InputListener;
import vidis.ui.model.impl.Link;
import vidis.ui.model.impl.Node;
import vidis.ui.model.impl.NodeField;
import vidis.ui.model.impl.Packet;
import vidis.ui.model.impl.PacketField;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IGuiContainer;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.mvc.api.AController;
import vidis.ui.mvc.api.Dispatcher;
import vidis.ui.vis.Light;
import vidis.ui.vis.camera.GuiCamera;
import vidis.ui.vis.camera.ICamera;
import vidis.ui.vis.objects.Axis;
import vidis.ui.vis.objects.Grid;
import vidis.ui.vis.shader.ShaderFactory;

import com.sun.opengl.util.Animator;

public class SceneController extends AController implements GLEventListener {

	private static Logger logger = Logger.getLogger( SceneController.class );
	private static Logger glLogger = Logger.getLogger( "vis.opengl" );
	
	private List<ICamera> cameras = new LinkedList<ICamera>();
	
	
	private List<IVisObject> objects = Collections.synchronizedList( new LinkedList<IVisObject>() );
	private List<IVisObject> objectsToDel = Collections.synchronizedList( new ArrayList<IVisObject>() );
	private List<IVisObject> objectsToAdd = Collections.synchronizedList( new ArrayList<IVisObject>() );
	
	
	private GLCanvas canvas;
	
	/**
	 * Used to animate the scene
	 */
	private Animator animator;
	
	private int wantedFps = 25;
	
	private long startTime;
	
	private int fps_log_max = 30;
	private List<Double> fps_log = new LinkedList<Double>();
	
	private NodeField nodeCapturingSource = null;
	private PacketField packetCapturingSource = null;
	
	public SceneController() {
		logger.debug( "Constructor()" );
		addChildController( new CameraController() );
		addChildController( new GuiController() );
		
		registerEvent( IVidisEvent.InitScene );
		
		registerEvent( IVidisEvent.CameraRegister,
					   IVidisEvent.CameraUnregister );
		
		registerEvent( IVidisEvent.ObjectRegister, 
					   IVidisEvent.ObjectUnregister );
		
		registerEvent( IVidisEvent.MouseClickedEvent,
					   IVidisEvent.MouseMovedEvent );
		
		registerEvent( IVidisEvent.StartNodeCapturing,
					   IVidisEvent.StartPacketCapturing );
		
	}
	
	@Override
	public void handleEvent( IVidisEvent event ) {
		logger.debug( "handleEvent( "+event+" )" );
		switch ( event.getID() ) {
		case IVidisEvent.InitScene:
			initialize();
			Dispatcher.forwardEvent( new VidisEvent<GLCanvas>( IVidisEvent.InitWindow, canvas ) );
			
			Dispatcher.forwardEvent( IVidisEvent.InitCamera );
			Dispatcher.forwardEvent( IVidisEvent.InitGui );
			break;
		case IVidisEvent.CameraRegister:
			registerCamera( ((CameraEvent)event).getCamera() );
			break;
		case IVidisEvent.CameraUnregister:
			unregisterCamera( ((CameraEvent)event).getCamera() );
			break;	
		case IVidisEvent.ObjectRegister:
			registerObject( ((ObjectEvent)event).getObject() );
			break;
		case IVidisEvent.ObjectUnregister:
			unregisterObject( ((ObjectEvent)event).getObject() );
			break;
		case IVidisEvent.MouseClickedEvent:
		case IVidisEvent.MouseMovedEvent:
			if ( ((AMouseEvent)event).ray != null ) {
//				logger.info("handling Mouse event");
				handleMouseEvent( (AMouseEvent)event );
			}
			break;
		case IVidisEvent.StartNodeCapturing:
			nodeCapturingSource = ((VidisEvent<NodeField>)event).getData();
			break;
		case IVidisEvent.StartPacketCapturing:
			packetCapturingSource = ((VidisEvent<PacketField>)event).getData();
			break;
		}	
		forwardEventToChilds( event );
	}

	private void initialize() {
		GraphicsDevice sd[] = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices();
		logger.info( "Graphics Devices: " );
		for ( GraphicsDevice d : sd ) {
			logger.info( " -<> "+ d );
			
		}
		GraphicsDevice graphicsDevice = sd[0];
		GraphicsConfiguration graphicsConfiguration = graphicsDevice.getDefaultConfiguration();
		GLCapabilities glCapabilities = new GLCapabilities();
		glCapabilities.setDoubleBuffered( true );
		glCapabilities.setHardwareAccelerated( true );
		
		GLCapabilitiesChooser chooser = new GLCapabilitiesChooser() {
			public int chooseCapabilities(GLCapabilities desired,
					GLCapabilities[] available,
					int windowSystemRecommendedChoice) {
				return 1;
			}
		};
		GLContext glContext = GLContext.getCurrent();
		
		canvas = new GLCanvas();
		canvas.addGLEventListener( this );
		
		InputListener l = new InputListener();
		canvas.addKeyListener(l);
		canvas.addMouseWheelListener(l);
		canvas.addMouseMotionListener(l);
		canvas.addMouseListener(l);
		
		// add some default objects
		
		registerObject( new Grid() );
		registerObject( new Axis() );
	}
	
	
	private void updateObjects() {
			synchronized ( objectsToAdd ) {
				if ( objectsToAdd.size() > 0 ) {
					objects.addAll( objectsToAdd );
					objectsToAdd.clear();
				}
			}
			synchronized ( objectsToDel ) {
				if ( objectsToDel.size() > 0 ) {
					objects.removeAll( objectsToDel );
					objectsToDel.clear();
				}
			}
	}
	
	
	
	/**
	 * display event
	 * draws the whole scene
	 */
	public void display(GLAutoDrawable drawable) {
		
		// do thedateObjects(); update thing
		updateObjects();
		
		
		
		
		final GL gl = drawable.getGL();
		
		startTime = System.currentTimeMillis();
		
		//XXX update cameras somewhere else
		for ( ICamera c : cameras ) {
			c.update();
		}
		
		for ( ICamera c : cameras ) {
			// INIT
			c.init(gl);
			
			// PROJECTION
			gl.glMatrixMode( GL.GL_PROJECTION );
			gl.glLoadIdentity();
			c.applyProjectionMatrix(gl);ShaderFactory.removeAllPrograms(gl);
			
			// VIEW
			gl.glMatrixMode( GL.GL_MODELVIEW );
			gl.glLoadIdentity();
			c.applyViewMatrix(gl);
			
			// MODEL
			drawModel( gl, c);
			
		}
		
		long usedTime = System.currentTimeMillis() - startTime;
		
		double fps = 0;
		
		if(usedTime != 0) {
			fps = 1000d / usedTime;
			fps_log.add(fps);
			if(fps_log_max < fps_log.size())
				fps_log.remove(0);
		}
		Dispatcher.forwardEvent( new VidisEvent<Double>( IVidisEvent.FPS, median(fps_log) ) );
	}
	
	private double median(List<Double> list) {
		if(list.size() > 0) {
			double sum = 0;
			for(Double d : list) {
				sum += d;
			}
			return sum / list.size();
		}
		return 0;
	}

	private void drawModel( GL gl, ICamera c ) {
		if ( c instanceof GuiCamera) {
			gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_FILL );
			
			ShaderFactory.removeAllPrograms(gl);
			
			// FIXME just for debug, remove later
			new Axis().render(gl);
			
			gl.glPushMatrix();
			for ( IVisObject o : objects ) {
				if ( o instanceof IGuiContainer ) {
					o.render(gl);
				}
			}
			gl.glPopMatrix();
		}
		else {
			if ( Configuration.DISPLAY_WIREFRAME ) {
				gl.glPolygonMode( GL.GL_FRONT_AND_BACK, GL.GL_LINE );
			}
//			if ( P != null && A != null ) {
//				gl.glBegin( GL.GL_LINES );
//					gl.glVertex3d( A.x, A.y, A.z );
//					gl.glVertex3d( P.x, P.y, P.z );
//				gl.glEnd();
//			}
//			for ( RenderPass p : RenderPass.values()) {
//				p.setup(gl);
				// MODEL  with draw order:
				// first draw the nodes
			    // then the back side of the links
				// then the packets
				// and finally the front sides of the links
				gl.glPushMatrix();
//					gl.glEnable( GL.GL_LIGHTING );
					// nodes
					gl.glDisable( GL.GL_LIGHT0 );
					gl.glEnable( GL.GL_LIGHT1 );
					gl.glDisable( GL.GL_LIGHT2 );
					for ( IVisObject o : objects ) {
						if ( (o instanceof Node) ) {
							o.render(gl);
						}
					}
					// packets
					gl.glDisable( GL.GL_LIGHT0 );
					gl.glDisable( GL.GL_LIGHT1 );
					gl.glEnable( GL.GL_LIGHT2 );
					for ( IVisObject o : objects ) {
						if ( (o instanceof Packet) ) {
							o.render(gl);
						}
					}
					// links
					gl.glEnable( GL.GL_LIGHT0 );
					gl.glEnable( GL.GL_LIGHT1 );
					gl.glEnable( GL.GL_BLEND );
					gl.glEnable( GL.GL_CULL_FACE );
					gl.glCullFace( GL.GL_BACK );
					
					gl.glBlendFunc( GL.GL_ONE, GL.GL_DST_ALPHA );
					gl.glColor4d( 0, 0, 1, 0.7 );
					for ( IVisObject o : objects ) {
						if ( (o instanceof Link) ) {
							o.render(gl);
						}
					}
					gl.glDisable( GL.GL_BLEND );
					gl.glDisable( GL.GL_CULL_FACE );
					
					// rest
					gl.glDisable( GL.GL_LIGHTING );
					for ( IVisObject o : objects ) {
						if ( !(o instanceof Node) && !(o instanceof Link) && !(o instanceof Packet) && !(o instanceof IGuiContainer) ) {
							o.render(gl);
						}
				}
				gl.glPopMatrix();
//			}
		}
	}
	
	
	
	/**
	 * displayChanged event
	 * called when displaymode has changed
	 */
	public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
		glLogger.debug( "displayChanged()" );
	}

	/**
	 * init method
	 * called when gl context is created
	 */
	public void init(GLAutoDrawable drawable) {
		glLogger.debug( "init()" );
		final GL gl = drawable.getGL();
		
		
		// enable / disable some global stuff
		ShaderFactory.init(gl);
		
		Light.initNodeLight(gl);
		Light.initLinkLight(gl);
		Light.initPacketLight(gl);
		
		glLogger.info("init shader prog");
		Link.setupShaderProgram(gl);
		glLogger.info("done with shader init");
		
		animator = new Animator(drawable);
		
		animator.start();
	}

	/**
	 * reshape event
	 */
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		glLogger.debug( "reshape()" );
		// update the camera targets
		for (ICamera c : cameras)
			c.reshape(x, y, width, height);
	}
	
	private void registerCamera( ICamera c ) {
		cameras.add(c);
	}
	private void unregisterCamera( ICamera c ) {
		cameras.remove(c);
	}

	private void registerObject( IVisObject o ) {
		synchronized ( objectsToAdd ) {
			objectsToAdd.add( o );
		}
	}
	
	private void unregisterObject( IVisObject o ) {
		synchronized ( objectsToAdd ) {
			objectsToDel.add( o );
		}
	}
	
	public synchronized List<IVisObject> getRegisteredObjects() {
		return new ArrayList<IVisObject>( objects );
	}
	
	private ASimObject underMouseObject = null;
	
	Point3d P;
	Point3d A;
	private void handleMouseEvent( AMouseEvent e ) {
		A = new Point3d( e.rayOrigin.x, e.rayOrigin.y, e.rayOrigin.z );
		Vector3d g = new Vector3d ( e.ray.x, e.ray.y, e.ray.z ) ;
		g.normalize();
		
		List<IVisObject> obj = getRegisteredObjects();
		Vector3d AP = new Vector3d();
		Vector3d dist = new Vector3d();
		double nearestDistance = Double.MAX_VALUE;
		ASimObject nearestObject = null;
		
		for ( IVisObject o : obj ) {
			if ( o instanceof ASimObject ) {
				try { 
					P = ((ASimObject)o).getPosition();
					
					AP.sub( P, A );
					
					dist.set( 
							g.y*AP.z - g.z*AP.y,
							g.z*AP.x - g.x*AP.z,
							g.x*AP.y - g.y*AP.x);
					double l = dist.length();
					if ( l < ((ASimObject)o).getHitRadius() ) {
						double way = AP.length();
						if ( way < nearestDistance ) {
							nearestDistance = way;
							nearestObject = (ASimObject) o;
						}
					}
				}
				catch ( Exception ex ) {
					logger.debug( "0815 error", ex );
				}
			}
			else {
				logger.debug( o + " was wrong" );
			}
		}
		if ( nearestObject != null ) {
			if ( e instanceof MouseClickedEvent ) {
				nearestObject.hit();
				if ( nearestObject instanceof Node ) {
					if ( nodeCapturingSource != null ) {
						nodeCapturingSource.setNode( (Node) nearestObject );
						nodeCapturingSource = null;
					}
				}
				else if ( nearestObject instanceof Packet ) {
					if ( packetCapturingSource != null ) {
						packetCapturingSource.setPacket( (Packet) nearestObject );
						packetCapturingSource = null;
					}
				}
			}
		}
		
		if ( nearestObject != null && underMouseObject != null ) {
			if ( ! nearestObject.equals( underMouseObject ) ) {
				// mouse out underMouseObject
				logger.info( "onMouseOut " + underMouseObject );
				underMouseObject.onMouseOut();
				// mouse in nearestObject
				underMouseObject = nearestObject;
				logger.info( "onMouseIn " + underMouseObject );
				underMouseObject.onMouseIn();
				
			}
		}
		else if ( nearestObject != null && underMouseObject == null ){
			underMouseObject = nearestObject;
			underMouseObject.onMouseIn();
			logger.info( "onMouseIn " + underMouseObject );
		}
		else if ( nearestObject == null && underMouseObject != null ) {
			logger.info( "onMouseOut " + underMouseObject );
			underMouseObject.onMouseOut();
			underMouseObject = null;
		}
	}
}
