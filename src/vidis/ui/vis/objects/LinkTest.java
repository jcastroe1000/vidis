package vidis.ui.vis.objects;

import java.nio.DoubleBuffer;

import javax.media.opengl.GL;

import vidis.ui.model.structure.IVisObject;

import com.sun.opengl.util.BufferUtil;

public class LinkTest implements IVisObject {

	double[] p1 = new double[] {1,0,0};
	double[] p2 = new double[] {0,1,0};
	double[] p3 = new double[] {-1,0,0};
	double[] p4 = new double[] {0,-1,0};
	
	double[] q1 = new double[] {1,0,5};
	double[] q2 = new double[] {0,1,5};
	double[] q3 = new double[] {-1,0,5};
	double[] q4 = new double[] {0,-1,5};
	
//	double[][][] ctrlpoints = new double[][][] {
//			{
//				p1,
//				p2,
//				p3
////				p4,
////				p1
//			},
//			{
//				q1,
//				q2,
//				q3
////				q4,
////				q1
//			}
//	};
	double x = Math.sqrt(1d/2d);
//	double[][][] ctrlpoints = new double[][][] {
//		{
//			{ 	0,	-1.5 + x,	x	},
//			{	1,	-1.5 + x,	x	},
//			{	1,	-1.5 -x,	-x	},
//			{	-1,	-1.5 -x,	-x	},
//			{	-1,	-1.5 +x,	x	},
//			{	0,	-1.5 +x,	x	}
//		},
//		{
//			{	0,	0,	1 - 2	},
//			{	1,	0,	1 - 2	},
//			{	1,	0,	-1 - 2	},
//			{	-1,	0,	-1 - 2	},
//			{	-1,	0,	1 - 2	},
//			{	0,	0,	1 - 2	}
//		},
//		{
//			{	0,	1.5 -x,	x	},
//			{	1,	1.5 -x,	x	},
//			{	1,	1.5 +x,	-x	},
//			{	-1,	1.5 +x,	-x	},
//			{	-1,	1.5 -x,	x	},
//			{	0,	1.5 -x,	x	}
//		}
//	};
	double step = 0.01;
	void moveIt(){
		r+=step;
		if (r >= 1 || r <= -1) step*=-1;
	}
	double r = 0;
	double[][][] ctrlpoints;
	void init() {
	ctrlpoints = new double[][][] {
			{
				{ 	0,	-1.5,	0.3	},
				{	0.3,	-1.5 ,	0.3	},
				{	0.3,	-1.5,	-0.3	},
				{	-0.3,	-1.5,	-0.3	},
				{	-0.3,	-1.5,	0.3	},
				{	0,	-1.5,	0.3	}
			},
			{
				{	0,	r-0.5,	0.3	},
				{	0.3,	r-0.5,	0.3	},
				{	0.3,	r-0.5,	-0.3	},
				{	-0.3,	r-0.5,	-0.3	},
				{	-0.3,	r-0.5,	0.3	},
				{	0,	r-0.5,	0.3	}
			},
			{
				{	0,	r,	2	},
				{	2,	r,	2	},
				{	2,	r,	-2	},
				{	-2,	r,	-2	},
				{	-2,	r,	2	},
				{	0,	r,	2	}
			},
			{
				{	0,	r+0.5,	0.3	},
				{	0.3,	r-0.5,	0.3	},
				{	0.3,	r-0.5,	-0.3	},
				{	-0.3,	r-0.5,	-0.3	},
				{	-0.3,	r-0.5,	0.3	},
				{	0,	r-0.5,	0.3	}
			},
			{
				{	0,	1.5,	0.3	},
				{	0.3,	1.5,	0.3	},
				{	0.3,	1.5,	-0.3	},
				{	-0.3,	1.5,	-0.3	},
				{	-0.3,	1.5,	0.3	},
				{	0,	1.5,	0.3	}
			}
		};
	ctrlpointsBuf = BufferUtil
    .newDoubleBuffer(ctrlpoints.length * ctrlpoints[0].length
                    * ctrlpoints[0][0].length);
// SO copy 4x4x3 array above to float buffer
  for (int i = 0; i < ctrlpoints.length; i++)
  {
    // System.out.print(ctrlpoints.length+ " ");
    for (int j = 0; j < ctrlpoints[0].length; j++)
    {
      // System.out.println(ctrlpoints[0][0].length+" ");
      for (int k = 0; k < ctrlpoints[0][0].length; k++)
      {
        ctrlpointsBuf.put(ctrlpoints[i][j][k]);
//        System.out.print(ctrlpoints[i][j][k] + " ");
      }
//      System.out.println();
    }
  }
  // THEN rewind it before use
  ctrlpointsBuf.rewind();
	}
	 // need float buffer instead of n-dimensional array above
	 private DoubleBuffer ctrlpointsBuf;
	public void render(GL gl) {
		if (System.currentTimeMillis()%1==0) moveIt();
		init();
		gl.glEnable(GL.GL_MAP2_VERTEX_3);
		ctrlpointsBuf.rewind();
		gl.glMap2d(GL.GL_MAP2_VERTEX_3,
				0, 1, 3, 6, 
				0, 1, 3 * 6, 5,
				ctrlpointsBuf);
//		gl. glMap2d(GL.GL_MAP2_VERTEX_3,
//			    0.0, 1.0,  /* U ranges 0..1 */
//			    2,         /* U stride, 2 floats per coord */
//			    2,         /* U is 2nd order, ie. linear */
//			    0.0, 1.0,  /* V ranges 0..1 */
//			    2 * 3,     /* V stride, row is 2 coords, 3 floats per coord */
//			    2,         /* V is 2nd order, ie linear */
//			    ctrlpointsBuf);  /* control points */

		gl.glMapGrid2f(20, 0.0f, 1.0f, 20, 0.0f, 1.0f);
		gl.glRotated(Math.PI/2, 0, 0, 1);
		
		gl.glEvalMesh2(GL.GL_LINE, 0, 20, 0, 20);
		
		gl.glPushMatrix();
			ctrlpointsBuf.rewind();
			gl.glColor3d(1, 0, 0);
			gl.glPointSize(3);
			gl.glBegin(GL.GL_POINTS);
				while (ctrlpointsBuf.hasRemaining())
					gl.glVertex3d(ctrlpointsBuf.get(), ctrlpointsBuf.get(), ctrlpointsBuf.get());
			gl.glEnd();
		gl.glPopMatrix();
		
	}
	public void kill() {
		// TODO Auto-generated method stub
		
	}

}
