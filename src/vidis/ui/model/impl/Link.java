package vidis.ui.model.impl;

import java.awt.Font;
import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.media.opengl.GL;
import javax.vecmath.Point3d;
import javax.vecmath.Tuple3d;
import javax.vecmath.Vector3d;

import org.apache.log4j.Logger;

import com.sun.opengl.util.j2d.TextRenderer;

import vidis.data.sim.SimLink;
import vidis.data.var.AVariable;
import vidis.data.var.IVariableContainer;
import vidis.ui.events.IVidisEvent;
import vidis.ui.model.structure.ASimObject;
import vidis.ui.model.structure.IVisObject;
import vidis.ui.vis.VecUtil;
import vidis.ui.vis.shader.IProgram;
import vidis.ui.vis.shader.IShader;
import vidis.ui.vis.shader.ShaderFactory;
import vidis.ui.vis.shader.impl.ShaderException;

public class Link extends ASimObject {
	
	private static Logger logger = Logger.getLogger( Link.class );
	
	private List<Packet> packets = new ArrayList<Packet>();
	
	DoubleBuffer surfaceControlPointsRightDown = DoubleBuffer.allocate( 3 * 4 * 3 ); // 3 doubles per point, 4 * 3 points per surface, 4 surfaces
	DoubleBuffer surfaceControlPointsLeftDown = DoubleBuffer.allocate( 3 * 4 * 3 );
	DoubleBuffer surfaceControlPointsRightUp = DoubleBuffer.allocate( 3 * 4 * 3 );
	DoubleBuffer surfaceControlPointsLeftUp = DoubleBuffer.allocate( 3 * 4 * 3 );
	
//	DoubleBuffer linesControlPoints = DoubleBuffer.allocate( 3 * 3 * 5 ); // 3 doubles per point, 3 points per line, 5 lines
//	DoubleBuffer lineControlPoints = DoubleBuffer.allocate( 3 * 3 ); // 3 doubles per point, 3 points
//	
//	DoubleBuffer lineDownControlPoints = DoubleBuffer.allocate( 3 * 3 ); // 3 doubles per point, 3 points
//	DoubleBuffer lineUpControlPoints = DoubleBuffer.allocate( 3 * 3 ); // 3 doubles per point, 3 points
//	DoubleBuffer lineLeftControlPoints = DoubleBuffer.allocate( 3 * 3 ); // 3 doubles per point, 3 points
//	DoubleBuffer lineRightControlPoints = DoubleBuffer.allocate( 3 * 3 ); // 3 doubles per point, 3 points
	
	private static Vector3d up = new Vector3d( 0, 1, 0 );
	private static Vector3d down = new Vector3d( 0, -1, 0 );

	private Vector3d left = null;
	private Vector3d right = null;
	
	private int displayListId = -1;

	// kappa
	private static double kappa = 4d * ( Math.sqrt( 2d ) - 1d ) / 3d;
	
	private double radius = 0.1;
	
	private double l = radius * kappa;
	
	public Link(IVariableContainer c) {
		super(c);
		logger.debug( "new link with the following vars: \n"+ getVariableIds());
	}

	// need to override render to get rid of the automatic positioning
	@Override
	public void render(GL gl) {
		try {
			
			Tuple3d posA = (Tuple3d) getVariableById( SimLink.POINT_A ).getData();
			Tuple3d posB = (Tuple3d) getVariableById( SimLink.POINT_B ).getData();
			
			if ( ! knownPointA.equals( posA ) && ! knownPointB.equals( posB ) ) {
				knownPointA = new Point3d( posA );
				knownPointB = new Point3d( posB );
				calculateControlPoints( knownPointA, knownPointB );
				preRenderObject( gl );
			}
			String text = "";
			try {
				// add text
				text = getVariableById(AVariable.COMMON_IDENTIFIERS.NAME).getData().toString();
			} catch (NullPointerException e) {
				// may happen, but if, don't care
				text = getVariableById(AVariable.COMMON_IDENTIFIERS.ID).getData().toString();
			} finally {
				if(text != "")
					drawText(gl, text, 180, 0, 1, 0);
			}
			Link.useShaderProgram(gl);
			renderObject(gl);
			ShaderFactory.removeAllPrograms(gl);
		}
		catch ( Exception e ) {
			logger.warn( e.getMessage(), e );
		}
	}

	private Point3d knownPointA = new Point3d();
	private Point3d knownPointB = new Point3d();
	
	
	private static IProgram linkProgram;
	public static void setupShaderProgram(GL gl) {
		try {
			IShader vs = ShaderFactory.getNewVertexShader();
			vs.create(gl);
			vs.loadSource("bin/vidis/ui/vis/shader/src/link.vertex.glsl", gl);
			vs.compile(gl);
			
			IShader fs = ShaderFactory.getNewFragmentShader();
			fs.create(gl);
			fs.loadSource("bin/vidis/ui/vis/shader/src/link.fragment.glsl", gl);
			fs.compile(gl);
			
			
			linkProgram = ShaderFactory.getNewProgram();
			linkProgram.create(gl);
			linkProgram.addShader( vs );
			linkProgram.addShader( fs );
			linkProgram.link(gl);
			linkProgram.use(gl);
		}
		catch ( ShaderException se ) {
			logger.error( "setupShaderProgram", se );
		}
	}
	
	public static void useShaderProgram(GL gl) {
		linkProgram.use(gl);
	}
	
	private void drawText(GL gl, String text, double angle, double x, double y, double z) {
		gl.glPushMatrix();
			//gl.glCullFace(GL.GL_FRONT);
			gl.glEnable(GL.GL_AUTO_NORMAL);
			gl.glDisable(GL.GL_CULL_FACE);
			// put into middle
			Point3d pointM = calculateMiddle(knownPointA, knownPointB, 0.7);
			gl.glTranslated(pointM.x, pointM.y, pointM.z);
			gl.glRotated(angle, x, y, z);
			// scale it down
			gl.glScaled(0.001, 0.001, 0.001);
			textRenderer.begin3DRendering();
			textRenderer.setUseVertexArrays(false);
			textRenderer.draw3D( text, 0f, 0f, 0f, 1f );
			textRenderer.end3DRendering();
			//gl.glFrontFace(GL.GL_CCW);
		gl.glPopMatrix();
	}
	
	@Override
	public void renderObject(GL gl) {
		for( int i=0; i<9; i++) {
			if(packets.size() > i) {
				Packet p = packets.get(i);
				Point3d pos = p.getPosition();
				if(pos != null) {
					linkProgram.getVariableByName("packet" + i).setValue(pos, gl);
				}
				//linkProgram.getVariableByName( "packet" + j++ ).setValue( ((Packet)packets.toArray()[i]).getPosition(), gl );
			} else {
				linkProgram.getVariableByName("packet"+i).setValue(new Vector3d(0,0,0), gl);
			}
		}

		gl.glCallList( displayListId );
		
//		gl.glMap1d( GL.GL_MAP1_VERTEX_3, 0, 1, 3, 3, linesControlPoints.array(), 0 );
//		gl.glEnable( GL.GL_MAP1_VERTEX_3 );
//		
//		gl.glPushMatrix();
//		gl.glColor3d( 1, 1, 0);
//		gl.glBegin( GL.GL_LINE_STRIP );
//			for ( int i=0; i<30; i++ ) {
//				gl.glEvalCoord1d(i/30.0);
//			}
//		gl.glEnd();
//		
//		gl.glMap1d( GL.GL_MAP1_VERTEX_3, 0, 1, 3, 3, linesControlPoints.array(), 9 );
//		gl.glBegin( GL.GL_LINE_STRIP );
//		for ( int i=0; i<30; i++ ) {
//			gl.glEvalCoord1d(i/30.0);
//		}
//		gl.glEnd();
//		
//		gl.glMap1d( GL.GL_MAP1_VERTEX_3, 0, 1, 3, 3, linesControlPoints.array(), 18 );
//		gl.glBegin( GL.GL_LINE_STRIP );
//		for ( int i=0; i<30; i++ ) {
//			gl.glEvalCoord1d(i/30.0);
//		}
//		gl.glEnd();
//		
//		gl.glPopMatrix();
//		
//		if ( right != null ) {
//			gl.glColor3d( 0, 0, 1 );
//		gl.glPushMatrix();
//			// right 
//			gl.glBegin( GL.GL_LINES );
//				gl.glVertex3d( knownPointA.x, knownPointA.y, knownPointA.z );
//				Point3d pointR = new Point3d( knownPointA ); pointR.add( right );
//				gl.glVertex3d( pointR.x, pointR.y, pointR.z );
//			gl.glEnd();
//		gl.glPopMatrix();
		
		
//		}
		
	
		
	}
	
	private void preRenderObject( GL gl ) {
		requireTextRenderer();
		if ( displayListId == -1 ) {
			displayListId = gl.glGenLists(1);
		}
		gl.glNewList( displayListId, GL.GL_COMPILE );
			gl.glEnable( GL.GL_MAP2_VERTEX_3 );
			drawBuffer( gl, surfaceControlPointsLeftDown );
			drawBuffer( gl, surfaceControlPointsLeftUp );
			drawBuffer( gl, surfaceControlPointsRightDown );
			drawBuffer( gl, surfaceControlPointsRightUp );
		gl.glEndList();
	}
	
	private int segments = 20;
	
	private void drawBuffer( GL gl, DoubleBuffer buf ) {
		gl.glEnable( GL.GL_AUTO_NORMAL );
		
		gl.glMap2d(GL.GL_MAP2_VERTEX_3,
				0,	1,
				3,	4, 
				0,	1,	
				12,	3, 
				buf);
		gl.glMapGrid2f( 3, 0.0f, 1.0f, segments, 0.0f, 1.0f);
		gl.glPushMatrix();
			gl.glEvalMesh2( GL.GL_FILL, 0, 3, 0, segments);
		gl.glPopMatrix();
	}
	
	private void calculateControlPoints( Point3d pointA, Point3d pointB) {
		// calc axis
			Vector3d AB = new Vector3d( pointB );
			AB.sub( pointA );
			segments = (int) Math.round( 5 * AB.length() );
			AB.normalize();
			
			right = VecUtil.cross( AB, up );
			right.normalize();
			left = new Vector3d( right );
			left.negate();
			left.normalize();
			
		// calc middle point
			Point3d pointM = calculateMiddle(pointA, pointB, 1.0);
		// line
			
//			fillLineBuffer(pointA, pointM, pointB, linesControlPoints, 0 );
			
		// surface
			// calculate control points
			
			double radius_ = Math.sqrt( Math.pow( radius, 2 )/ 2d );
			
			
			Vector3d tmp = new Vector3d();
			// base points
			Point3d pointAdown = new Point3d( pointA );
			tmp.scale( radius_, down );
			pointAdown.add( tmp );
			tmp.scale( radius_, AB );
			pointAdown.add( tmp );
			
			Vector3d vecAdown = new Vector3d( pointAdown );
			vecAdown.sub( pointA );
			Vector3d vecAup = new Vector3d( vecAdown );
			vecAup.negate();			
			
			Point3d pointAup = new Point3d( pointA );
			pointAup.add( vecAup );
			
			vecAdown.normalize();
			vecAup.normalize();
			
			Point3d pointAright = new Point3d( pointA );
			tmp.scale( radius, right );
			pointAright.add( tmp );
			
			Point3d pointAleft = new Point3d( pointA );
			tmp.scale( radius, left );
			pointAleft.add( tmp );
			
			// downpoints
			Point3d pointAdownRight = new Point3d( pointAdown );
			tmp.scale( l, right );
			pointAdownRight.add( tmp );
			
			Point3d pointAdownLeft = new Point3d( pointAdown );
			tmp.scale( l, left );
			pointAdownLeft.add( tmp );
			
			// rightpoints
			Point3d pointArightUp = new Point3d( pointAright );
			tmp.scale( l, vecAup );
			pointArightUp.add( tmp );
			
			Point3d pointArightDown = new Point3d( pointAright );
			tmp.scale( l, vecAdown );
			pointArightDown.add( tmp );
			
			// leftpoints
			Point3d pointAleftDown = new Point3d( pointAleft );
			tmp.scale( l, vecAdown );
			pointAleftDown.add( tmp );
			
			Point3d pointAleftUp = new Point3d( pointAleft );
			tmp.scale( l, vecAup );
			pointAleftUp.add( tmp );
			
			// top points
			Point3d pointAupLeft = new Point3d( pointAup );
			tmp.scale( l, left );
			pointAupLeft.add( tmp );
			
			Point3d pointAupRight = new Point3d( pointAup );
			tmp.scale( l, right );
			pointAupRight.add( tmp );
			
			fillSegment( 0, surfaceControlPointsRightDown, pointAright, pointArightDown, pointAdownRight, pointAdown );
			fillSegment( 0, surfaceControlPointsRightUp, pointAup, pointAupRight, pointArightUp, pointAright );
			fillSegment( 0, surfaceControlPointsLeftUp, pointAleft, pointAleftUp, pointAupLeft, pointAup );
			fillSegment( 0, surfaceControlPointsLeftDown, pointAdown, pointAdownLeft, pointAleftDown, pointAleft );
			
			
			Point3d pointMdown = new Point3d( pointM );
			tmp.scale( radius, down );
			pointMdown.add( tmp );
			
			Point3d pointMup = new Point3d( pointM );
			tmp.scale( radius, up );
			pointMup.add( tmp );
			
			Point3d pointMright = new Point3d( pointM );
			tmp.scale( radius, right );
			pointMright.add( tmp );
			
			Point3d pointMleft = new Point3d( pointM );
			tmp.scale( radius, left );
			pointMleft.add( tmp );
			
			Point3d pointMdownRight = new Point3d( pointMdown );
			tmp.scale( l, right );
			pointMdownRight.add( tmp );
			
			Point3d pointMdownLeft = new Point3d( pointMdown );
			tmp.scale( l, left );
			pointMdownLeft.add( tmp );
			
			Point3d pointMrightDown = new Point3d( pointMright );
			tmp.scale( l, down );
			pointMrightDown.add( tmp );
			
			Point3d pointMrightUp = new Point3d( pointMright );
			tmp.scale( l, up );
			pointMrightUp.add( tmp );
			
			Point3d pointMupRight = new Point3d( pointMup );
			tmp.scale( l, right );
			pointMupRight.add( tmp );
			
			Point3d pointMupLeft = new Point3d( pointMup );
			tmp.scale( l, left );
			pointMupLeft.add( tmp );
			
			Point3d pointMleftDown = new Point3d( pointMleft );
			tmp.scale( l, down );
			pointMleftDown.add( tmp );
			
			Point3d pointMleftUp = new Point3d( pointMleft );
			tmp.scale( l, up );
			pointMleftUp.add( tmp );
			
			fillSegment( 1, surfaceControlPointsRightDown, pointMright, pointMrightDown, pointMdownRight, pointMdown );
			fillSegment( 1, surfaceControlPointsRightUp, pointMup, pointMupRight, pointMrightUp, pointMright );
			fillSegment( 1, surfaceControlPointsLeftUp, pointMleft, pointMleftUp, pointMupLeft, pointMup );
			fillSegment( 1, surfaceControlPointsLeftDown, pointMdown, pointMdownLeft, pointMleftDown, pointMleft );
			
			Point3d pointBdown = new Point3d( pointB );
			tmp.scale( radius_, down );
			pointBdown.add( tmp );
			tmp.scale( radius_, AB );
			pointBdown.sub( tmp );
			
			Vector3d vecBdown = new Vector3d( pointBdown );
			vecBdown.sub( pointB );
			Vector3d vecBup = new Vector3d( vecBdown );
			vecBup.negate();
		
			Point3d pointBup = new Point3d( pointB );
			pointBup.add( vecBup );
			
			vecBdown.normalize();
			vecBup.normalize();
			
			Point3d pointBright = new Point3d( pointB );
			tmp.scale( radius, right );
			pointBright.add( tmp );
			
			Point3d pointBleft = new Point3d( pointB );
			tmp.scale( radius, left );
			pointBleft.add( tmp );
			
			Point3d pointBdownRight = new Point3d( pointBdown );
			tmp.scale( l, right );
			pointBdownRight.add( tmp );
			
			Point3d pointBdownLeft = new Point3d( pointBdown );
			tmp.scale( l, left );
			pointBdownLeft.add( tmp );
			
			Point3d pointBrightDown = new Point3d( pointBright );
			tmp.scale( l, vecBdown );
			pointBrightDown.add( tmp );
			
			Point3d pointBrightUp = new Point3d( pointBright );
			tmp.scale( l, vecBup );
			pointBrightUp.add( tmp );
			
			Point3d pointBleftDown = new Point3d( pointBleft );
			tmp.scale( l, vecBdown );
			pointBleftDown.add( tmp );
			
			Point3d pointBleftUp = new Point3d( pointBleft );
			tmp.scale( l, vecBup );
			pointBleftUp.add( tmp );
			
			Point3d pointBupRight = new Point3d( pointBup );
			tmp.scale( l, right );
			pointBupRight.add( tmp );
			
			Point3d pointBupLeft = new Point3d( pointBup );
			tmp.scale( l, left );
			pointBupLeft.add( tmp );
			
			fillSegment( 2, surfaceControlPointsRightDown, pointBright, pointBrightDown, pointBdownRight, pointBdown );
			fillSegment( 2, surfaceControlPointsRightUp, pointBup, pointBupRight, pointBrightUp, pointBright );
			fillSegment( 2, surfaceControlPointsLeftUp, pointBleft, pointBleftUp, pointBupLeft, pointBup );
			fillSegment( 2, surfaceControlPointsLeftDown, pointBdown, pointBdownLeft, pointBleftDown, pointBleft );
			
			
//			fillLineBuffer( pointAdown, pointMdown, pointBdown, linesControlPoints, 9);
//			
//			fillLineBuffer( pointAup, pointMup, pointBup, linesControlPoints, 18);
			
			
	}
	
/*	private void fillLineBuffer( Point3d a, Point3d b, Point3d c, DoubleBuffer buf, int offset ) {
		buf.put( offset + 0, a.x );
		buf.put( offset + 1, a.y );
		buf.put( offset + 2, a.z );
		
		buf.put( offset + 3, b.x );
		buf.put( offset + 4, b.y );
		buf.put( offset + 5, b.z );
		
		buf.put( offset + 6, c.x );
		buf.put( offset + 7, c.y );
		buf.put( offset + 8, c.z );
	}*/
	
	private void fillSegment( int segment, DoubleBuffer buf, Point3d d, Point3d c, Point3d b, Point3d a ) {
		buf.put( 12 * segment + 0, a.x );
		buf.put( 12 * segment + 1, a.y );
		buf.put( 12 * segment + 2, a.z );
		buf.put( 12 * segment + 3, b.x );
		buf.put( 12 * segment + 4, b.y );
		buf.put( 12 * segment + 5, b.z );
		buf.put( 12 * segment + 6, c.x );
		buf.put( 12 * segment + 7, c.y );
		buf.put( 12 * segment + 8, c.z );
		buf.put( 12 * segment + 9, d.x );
		buf.put( 12 * segment + 10, d.y );
		buf.put( 12 * segment + 11, d.z );
		
		
	}

	@Override
	protected void handleEvent(IVidisEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void addPacket( Packet p ) {
		packets.add( p );
	}

	public void delPacket(IVisObject p) {
		packets.remove( p );
		
	}
	
}
