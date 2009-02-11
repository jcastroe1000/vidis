//	VIDIS is a simulation and visualisation framework for distributed systems.
//	Copyright (C) 2009 Dominik Psenner, Christoph Caks
//	This program is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
//	This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
//	You should have received a copy of the GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
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