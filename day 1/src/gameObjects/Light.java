package gameObjects;

import org.joml.Vector3f;

public class Light {
    private Vector3f position;
    private Vector3f ambience;
    private Vector3f diffuse;
    private Vector3f spec;

    public Light(Vector3f position, float ambience, float diffuse, float spec) {
        this.position = new Vector3f(position);

        this.ambience = new Vector3f(ambience, ambience, ambience);
        this.diffuse = new Vector3f(diffuse, diffuse, diffuse);
        this.spec = new Vector3f(spec, spec, spec);
    }

    public void move(Vector3f offset) {
        position.x += offset.x;
        position.y += offset.y;
        position.z += offset.z;
    }
    public void setPosition(Vector3f position) {
        this.position.set(position);
    }

    public Vector3f getPosition() {return position;}
    public Vector3f getAmbience() {return ambience;}
    public Vector3f getDiffuse() {return diffuse;}
    public Vector3f getSpec() {return spec;}
}
