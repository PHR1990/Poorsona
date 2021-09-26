#type vertex
#version 330 core
layout (location = 0) in vec3 aPos;
layout (location = 1) in vec4 aColor;

uniform mat4 uProjectionMatrix;
uniform mat4 uViewMatrix;

out vec4 fColor;

void main() {
    fColor = aColor;
    gl_Position = uProjectionMatrix * uViewMatrix * vec4(aPos, 1.0);
}

#type fragment
#version 330 core

uniform float uTime;

in vec4 fColor;

out vec4 color;

void main() {
    //float avg = (fColor.r + fColor.g + fColor.g) / 3;
    float noise = fract(sin(dot(fColor.xy, vec2(12.9898, 78233))) * 43758.5453);
    //color = vec4(avg, avg, avg, 1);
    color = fColor * noise;
}