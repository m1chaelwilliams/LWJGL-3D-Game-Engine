#version 330 core

in vec2 uv;
in vec3 normal;
in vec3 fragPos;

out vec4 FragColor;

struct Light {
    vec3 position;
    vec3 Ia;
    vec3 Id;
    vec3 Is;
};

uniform sampler2D tex;
uniform Light light;
uniform vec3 camPos;

vec3 getLight(vec3 color) {
    vec3 Normal = normalize(normal); 

    // ambient light
    vec3 ambient = light.Ia;

    // diffuse light
    vec3 lightDir = normalize(light.position - fragPos);
    float diff = max(0, dot(lightDir, Normal));
    vec3 diffuse = diff * light.Id;

    // specular light
    vec3 viewDir = normalize(camPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, Normal);
    float spec = pow(max(dot(viewDir, reflectDir), 0), 100);
    vec3 specular = spec * light.Is;
    
    return color * (ambient + diffuse + specular);
}

void main() {
    float gamma = 2.2;
    vec3 color = texture(tex, uv).rgb; // gamma val
    color = pow(color, vec3(gamma)); // puttin the texture into linear space

    color = getLight(color);

    color = pow(color, 1/vec3(gamma)); // gamma correction

    FragColor = vec4(color, 1.0);
}