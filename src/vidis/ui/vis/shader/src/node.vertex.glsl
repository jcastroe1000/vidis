uniform float alpha;
uniform bool mirror;

//const vec3 LightPosition = vec3(1.0, 1.0, 0.0);


// prototype
vec3 hemisphere_lighting(vec4 curr_Vertex, vec3 curr_Normal, vec3 curr_LightPosition, vec3 SkyColor, vec3 GroundColor);
void fading_color(vec4 curr_Vertex);

void main() {
//vec3 SkyColor = gl_FrontMaterial.ambient.xyz;
	vec4 curr_Vertex = gl_Vertex;
	vec3 curr_Normal = gl_Normal;
	// invert light
	//vec3 LightPosition = gl_LightSource[0].position.xyz;
	vec3 curr_LightPosition;
	if (mirror) {
		curr_LightPosition = gl_LightSource[1].position.xyz; //vec3(LightPosition.x, -LightPosition.y, LightPosition.z);
		fading_color(curr_Vertex);
	}
	else {
		curr_LightPosition = gl_LightSource[0].position.xyz;
	}
	vec3 curr_Color = hemisphere_lighting(curr_Vertex, curr_Normal, curr_LightPosition);
	gl_FrontColor = vec4(curr_Color, alpha);
	
//	gl_Position = gl_ModelViewProjectionMatrix * curr_Vertex;
	gl_Position = ftransform();
}