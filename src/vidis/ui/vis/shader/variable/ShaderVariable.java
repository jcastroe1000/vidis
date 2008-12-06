package vidis.ui.vis.shader.variable;

import javax.media.opengl.GL;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import vidis.ui.vis.shader.IShaderVariable;

public class ShaderVariable implements IShaderVariable {
	protected VariableType variableType;
	protected DataType dataType;
	protected String name;
	protected int address;
	
	
	public ShaderVariable(VariableType variableType, DataType dataType, String name) {
		this.variableType = variableType;
		this.dataType = dataType;
		this.name = name;
	}

	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#getVariableType()
	 */
	public VariableType getVariableType() {
		return variableType;
	}

	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#getDataType()
	 */
	public DataType getDataType() {
		return dataType;
	}

	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#getName()
	 */
	public String getName() {
		return name;
	}
	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#setAddress(int)
	 */
	public void setAddress(int address) {
		this.address = address;
	}
	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#getAddress()
	 */
	public int getAddress() {
		return address;
	}
	
	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#toString()
	 */
	public String toString() {
		return getVariableType().getType() + " " + getDataType().getType() + " " + getName() + " @ "+ getAddress();
	}
	// TODO replace with own exception
	// TODO add a case for every possible combination
	/* (non-Javadoc)
	 * @see ui.vis.shader.variable.IShaderVariable#setValue(java.lang.Object, javax.media.opengl.GL)
	 */
	public void setValue(Object value, GL gl) throws java.lang.reflect.MalformedParameterizedTypeException {
		if (dataType.getJavaClass().equals(value.getClass())) {
			switch (variableType) {
			case UNIFORM:
				switch (dataType){
				case VEC3: 
					Vector3f v = (Vector3f) value;
					gl.glUniform3f(this.address, v.x, v.y, v.z);
					break;
				case VEC4:
					break;
				case FLOAT:
					break;
				case BOOL:
					Boolean b = (Boolean) value;
					gl.glUniform1i(this.address, b?1:0);
					break;
				}
			break;
			case ATTRIBUTE:
				switch (dataType){
				case VEC3: 
					Vector3d v = (Vector3d) value;
					gl.glVertexAttrib3d(this.address, v.x, v.y, v.z);
				break;
				case VEC4:
				break;
				case FLOAT:
					gl.glVertexAttrib1f(this.address, (Float) value);
				break;
				}				
			break;
			}
		}
		else throw new java.lang.reflect.MalformedParameterizedTypeException();
	}
	
	
}
