#version 330 core

layout (location = 0) in vec3 aPos;
layout (location = 1) in vec3 aNormals;
layout (location = 2) in vec2 aUV;

out vec2 uv;
out vec3 normal;
out vec3 fragPos;

uniform mat4 model;
uniform mat4 view;
uniform mat4 proj;

void main() {
    gl_Position = proj * view * model * vec4(aPos, 1.0);
    uv = aUV;

    // lighting
    fragPos = vec3(model * vec4(aPos, 1.0)); // getting the world space position of vertex
    normal = mat3(transpose(inverse(model))) * normalize(aNormals); // getting the normal vector
}