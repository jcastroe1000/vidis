/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.vis.shader;

import java.util.List;

import javax.media.opengl.GL;

import vidis.ui.vis.shader.impl.ShaderException;

public interface IShader {

	public abstract List<IShaderVariable> getVariables();

	public abstract void create(GL gl);
	
	public abstract void compile(GL gl) throws ShaderException;

	public abstract void loadSource(String filename, GL gl)
			throws ShaderException;

	public abstract int getShaderId();

	public abstract String getShaderInfoLog(GL gl);

}