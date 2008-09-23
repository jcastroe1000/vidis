// uniform qualified variables are changed at most once per primitive

// attribute qualified variables ar typically changed per vertex
attribute vec3 packet0;
attribute vec3 packet1;
attribute vec3 packet2;
attribute vec3 packet3;
attribute vec3 packet4;
attribute vec3 packet5;
attribute vec3 packet6;
attribute vec3 packet7;
attribute vec3 packet8;
attribute vec3 packet9;



// varying qualified variables communicate from the vertex shader to
// the framgment shader
varying vec3 normal;
varying vec3 incom;

float functioner(float distance) {
	return (distance + (1.0/distance)*0.014);
}

vec3 recalculateVertex(vec3 packet, vec3 vertex, float distance) {
	return packet + functioner(distance) * normalize (vertex - packet);
}

vec3 calculateAddVec( vec3 dir ) {
	float l = length( dir );
	if ( l <= 1.0 ) {
		return functioner( l ) * normalize( dir ) - dir;
	}else {
		return vec3( 0, 0, 0 );
	}
}



void main() {
	
	vec3 newVertex;
	vec3 oldVertex = vec3(gl_Vertex);
	float l = length(oldVertex - packet1);
	
	newVertex = oldVertex;
	newVertex += calculateAddVec( oldVertex - packet0 );
	newVertex += calculateAddVec( oldVertex - packet1 );
	newVertex += calculateAddVec( oldVertex - packet2 );
	newVertex += calculateAddVec( oldVertex - packet3 );
	newVertex += calculateAddVec( oldVertex - packet4 );
	newVertex += calculateAddVec( oldVertex - packet5 );
	newVertex += calculateAddVec( oldVertex - packet6 );
	newVertex += calculateAddVec( oldVertex - packet7 );
	newVertex += calculateAddVec( oldVertex - packet8 );
	newVertex += calculateAddVec( oldVertex - packet9 );
	
	incom = newVertex - vec3(0, 10, 0);
	normal = gl_NormalMatrix * gl_Normal;
	
	
	gl_Position = gl_ModelViewProjectionMatrix * vec4(newVertex, gl_Vertex.w);
	gl_FrontColor = gl_Color;
}