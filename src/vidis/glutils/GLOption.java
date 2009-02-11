/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.glutils;

import javax.media.opengl.GL;

public class GLOption {
	public interface GLConfig {
		public void execute( GL gl );
	}
	public enum ShadeModel implements GLConfig{
		FLAT(GL.GL_FLAT),
		SMOOTH(GL.GL_SMOOTH);
		int mode;
		private ShadeModel(int param){
			this.mode = param;
		}
		public void execute(GL gl) {
			gl.glShadeModel(mode);
		}
	}
	
	public enum DepthFunc implements GLConfig{
		LEQUAL(GL.GL_LEQUAL),
		EQUAL(GL.GL_EQUAL);
		private int func;
		private DepthFunc(int func){
			this.func = func;
		}
		public void execute(GL gl) {
			gl.glDepthFunc(func);
		}
	}

	public enum Blending implements GLConfig{
		NONE(),
		ONE_ONE( GL.GL_ONE, GL.GL_ONE ),
		ZERO_SRC_COLOR( GL.GL_ZERO, GL.GL_SRC_COLOR);
		boolean enabled=true;
		int sfactor;
		int dfactor;
		private Blending(int s, int d){
			this.sfactor = s;
			this.dfactor = d;
		}
		private Blending(){
			enabled = false;
		}
		public void execute(GL gl) {
			if ( enabled ) {
				gl.glEnable( GL.GL_BLEND );
				gl.glBlendFunc(sfactor, dfactor);
			}
			else {
				gl.glDisable( GL.GL_BLEND );
			}
		}
	}
}
