/*	VIDIS is a simulation and visualisation framework for distributed systems.
	Copyright (C) 2009 Dominik Psenner, Christoph Caks
	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>. */
package vidis.ui.vis.shader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.MalformedParameterizedTypeException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.media.opengl.GL;
import javax.media.opengl.GLException;

import org.apache.log4j.Logger;

import vidis.ui.vis.shader.impl.FragmentShader;
import vidis.ui.vis.shader.impl.Program;
import vidis.ui.vis.shader.impl.ShaderException;
import vidis.ui.vis.shader.impl.VertexShader;
import vidis.ui.vis.shader.variable.DataType;
import vidis.ui.vis.shader.variable.VariableType;

public class ShaderFactory {
	private static Logger logger = Logger.getLogger(ShaderFactory.class);

	private static boolean shadersupport;

	public static class DummyShader implements IShader {
		public void compile(GL gl) throws ShaderException {
		}
		public int getShaderId() {
			return 0;
		}
		public String getShaderInfoLog(GL gl) {
			return "this is a dummy shader";
		}
		private List<IShaderVariable> vars = new ArrayList<IShaderVariable>();
		public List<IShaderVariable> getVariables() {
			return vars;
		}
		public void loadSource(String filename, GL gl) throws ShaderException {
			try {
			String source = loadShaderFromFile(filename);
			vars.addAll(parseForVariables(source));
			}
			catch ( Exception e ) {
				throw new ShaderException( e );
			}
		}
		public void create(GL gl) {
		}
		protected static String loadShaderFromFile(String filename) throws IOException{
			BufferedReader brf = new BufferedReader(new FileReader(filename));
	        String shadstr = "";
	        String line;
	        while ((line=brf.readLine()) != null) {
	          shadstr += line + "\n";
	        }
	        return shadstr;
		}
		
		protected static List<IShaderVariable> parseForVariables(String source) {
			ArrayList<IShaderVariable> tmp = new ArrayList<IShaderVariable>();
			StringTokenizer linetok = new StringTokenizer(source, "\n");
			while (linetok.hasMoreTokens()) {
				String line = linetok.nextToken();
				if (!line.trim().startsWith("//")) {
					StringTokenizer wordtok = new StringTokenizer(line, " ");
					while (wordtok.hasMoreTokens()){
						String word = wordtok.nextToken();
						if (word.equals("uniform")) {
							VariableType vtype = VariableType.UNIFORM;
							DataType dtype = DataType.byTypeValue(wordtok.nextToken());
							String name = wordtok.nextToken().replaceFirst(";", "");
							IShaderVariable tmpvar = new DummyShaderVariable(vtype, dtype, name);
							tmp.add(tmpvar);
						}
						else if (word.equals("attribute")) {
							IShaderVariable tmpvar = new DummyShaderVariable(VariableType.ATTRIBUTE, DataType.byTypeValue(wordtok.nextToken()), wordtok.nextToken().replaceFirst(";", ""));
							tmp.add(tmpvar);
						}
					}
				}
			}
			return tmp;
		}
	}
	public static class DummyShaderVariable implements IShaderVariable {
		protected VariableType variableType;
		protected DataType dataType;
		protected String name;
		protected int address;
		
		
		public DummyShaderVariable(VariableType variableType, DataType dataType, String name) {
			this.variableType = variableType;
			this.dataType = dataType;
			this.name = name;
		}
		
		public int getAddress() {
			return 0;
		}

		public DataType getDataType() {
			return dataType;
		}

		public String getName() {
			return name;
		}

		public VariableType getVariableType() {
			return variableType;
		}

		public void setAddress(int address) {
			this.address = address;
		}

		public void setValue(Object value, GL gl)
				throws MalformedParameterizedTypeException {
			
		}
		
	}
	public static class DummyProgram implements IProgram {
		List<IShader> shader = new ArrayList<IShader>();
		public void addShader(IShader s) {
			shader.add(s);
		}
		public void create(GL gl) {
		}
		public int getProgramId() {
			return 0;
		}
		public IShaderVariable getVariableByName(String string) {
			for ( IShader s : shader ) {
				for ( IShaderVariable v : s.getVariables() ) {
					if ( v.getName().equals( string ) ) {
						return v;
					}
				}
			}
			return null;
		}
		public void link(GL gl) {
		}
		public void use(GL gl) {
		}
	}
	
	public static void init(GL gl) {
		try {
			int id = gl.glCreateShader( GL.GL_VERTEX_SHADER );
			gl.glDeleteShader( id );
			shadersupport = true;
		}
		catch ( GLException e ) {
			shadersupport = false;
		}
		logger.debug("Shader support enabled: " + shadersupport);
	}
	
	public static IShader getNewFragmentShader() {
		if ( shadersupport ) {
			return new FragmentShader();
		}
		else {
			return new DummyShader();
		}
	}
	
	public static IShader getNewVertexShader() {
		if ( shadersupport ) {
			return new VertexShader();
		}
		else {
			return new DummyShader();
		}
	}
	
	public static IProgram getNewProgram() {
		if ( shadersupport ) {
			return new Program();
		}
		else {
			return new DummyProgram();
		}	
	}
	
	public static void removeAllPrograms(GL gl) {
		if ( shadersupport ) {
			gl.glUseProgram( 0 );
		}
	}
}
