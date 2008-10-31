
vec3 hemisphere_lighting(vec4 curr_Vertex, vec3 curr_Normal, vec3 curr_LightPosition, vec3 SkyColor, vec3 GroundColor){
	vec3 tmpLight = curr_LightPosition; //vec3(gl_ModelViewMatrix * vec4(curr_LightPosition, 1.0));
	vec3 ecPos = vec3(gl_ModelViewMatrix * curr_Vertex);
	vec3 norm = normalize(gl_NormalMatrix * curr_Normal);
	vec3 lightVec = normalize(tmpLight - ecPos);
//	vec3 lightVec = normalize(curr_LightPosition - curr_Vertex.xyz);
	float costheta = dot(norm, lightVec);
	float a = 0.5 + 0.5 * costheta;
//	gl_FrontColor = vec4(mix(GroundColor, SkyColor, a), alpha);
	return mix(GroundColor, SkyColor, a);
}