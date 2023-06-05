package gameObjects;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import models.TexturedModel;

public class GameObject {
    private TexturedModel model;
    private Vector3f position;
    private Vector3f rotation;
    private Vector3f scale;

    public GameObject(TexturedModel model) {
        this.model = model;
        position = new Vector3f();
        rotation = new Vector3f();
        scale = new Vector3f(1,1,1);
    }
    public TexturedModel getModel() {return model;}

    public void move(Vector3f translation) {
        position = position.add(translation);
    }
    public void rotate(Vector3f rotation) {
        this.rotation = this.rotation.add(rotation);
    }
    public void scale(Vector3f scale) {
        this.scale = this.scale.add(scale);
    }
    // getters and setters
    public Matrix4f getWorldMatrix() {
        Matrix4f modelMat = new Matrix4f().identity().translate(position.x, position.y, position.z)
        .rotateX((float) Math.toRadians(rotation.x))
        .rotateY((float) Math.toRadians(rotation.y))
        .rotateZ((float) Math.toRadians(rotation.z))
        .scale(scale);
        return modelMat;
    }
    public void setPosition(Vector3f position) {
        this.position = position;
    }
    public void setRotation(Vector3f rotation) {
        this.rotation.set(rotation);
    }
    public void setScale(Vector3f scale) {
        this.scale.set(scale);
    }
    public Vector3f getPosition() {
        return position;
    } 
}
