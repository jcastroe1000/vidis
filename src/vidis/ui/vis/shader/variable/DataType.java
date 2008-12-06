package vidis.ui.vis.shader.variable;

import javax.vecmath.Vector3d;
import javax.vecmath.Vector4d;

public enum DataType {
	// TODO add other datatypes
	VEC3("vec3", Vector3d.class),
	VEC4("vec4", Vector4d.class), //TODO  Vector3f ( create  Vector3f first )
	FLOAT("float", Float.class),
	BOOL("bool", Boolean.class);
	
	private String type;
	private Class javarepresentation;
	private DataType(String type, Class rep) {
		this.type = type;
		this.javarepresentation = rep;
	}
	
	public String getType() {
		return this.type;
	}
	public Class getJavaClass() {
		return javarepresentation;
	}

	public static DataType byTypeValue(String type) {
		if (type.equals("vec3")) return DataType.VEC3;
		if (type.equals("vec4")) return DataType.VEC4;
		if (type.equals("float")) return DataType.FLOAT;
		if (type.equals("bool")) return DataType.BOOL;
		return null;
	}
	
}
