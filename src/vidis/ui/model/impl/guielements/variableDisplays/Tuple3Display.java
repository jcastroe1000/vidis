/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.model.impl.guielements.variableDisplays;

import javax.media.opengl.GL;
import javax.vecmath.Tuple3b;
import javax.vecmath.Tuple3d;
import javax.vecmath.Tuple3f;
import javax.vecmath.Tuple3i;

import org.apache.log4j.Logger;

import vidis.data.var.vars.AVariable;
import vidis.util.Rounding;

public class Tuple3Display extends Display {
	private static Logger logger = Logger.getLogger(Tuple3Display.class);

	public Tuple3Display ( AVariable v ) {
		super(v);
		this.setText( "Label" );
	}
	
//	@Override
//	public Display newInstance( AVariable var ) {
//		return new Tuple3Display( var );
//	}
	
	private String convertUnknownTupleToString() {
		Object tuple = var.getData();
		if ( tuple instanceof Tuple3d ) {
			return Rounding.round(((Tuple3d) tuple).getX(), 3) + "; " + 
			Rounding.round(((Tuple3d) tuple).getY(), 3) + "; " + 
			Rounding.round(((Tuple3d) tuple).getZ(), 3);
		} else if ( tuple instanceof Tuple3b ) {
			return ((Tuple3b)tuple).toString();
		} else if ( tuple instanceof Tuple3f ) {
			return Rounding.round(((Tuple3f) tuple).getX(), 3) + ", " + 
			Rounding.round(((Tuple3f) tuple).getY(), 3) + "; " + 
			Rounding.round(((Tuple3f) tuple).getZ(), 3);
		} else if ( tuple instanceof Tuple3i ) {
			return ((Tuple3i)tuple).toString();
		} else {
			return "ERROR";
		}
	}

	
	
	@Override
	public void renderContainer(GL gl) {
		String txt = var.getIdentifier().replaceAll(var.getNameSpace()+".", "   ") + " -> (" + convertUnknownTupleToString() + ")";
		this.setText(txt);
		super.renderContainer(gl);
	}
//	@Override
//	public void renderContainer(GL gl) {
//		String txt = var.getIdentifier() + " -> " + var.getData().toString();
//		double scale = 0.01;
//		height = textH * scale;
//		double hoffset = height / 2d;
//		gl.glPushMatrix();
//			gl.glTranslated(0, 0, 0);
//			gl.glScaled( scale, scale, scale );
//			textRenderer.begin3DRendering();
//			textRenderer.setUseVertexArrays(false);
//			textRenderer.draw3D( txt, 0f, 0f, 0f, 1f );
//			textRenderer.end3DRendering();
//		gl.glPopMatrix();
//		//super.renderContainer(gl);
//	}
}
