package environment;

import org.joml.Vector2f;
import org.joml.Vector3f;

import io.Mouse;

import org.joml.Math;
import org.joml.Matrix4f;
import utilities.Time;

public class FirstPersonCamera {
    private float SENSITIVITY = 180.0f;

    private Vector3f position;

    private Vector3f forward = new Vector3f(0, 0, -1);
    private Vector3f up = new Vector3f(0, 1, 0);
    private Vector3f right = new Vector3f(1, 0, 0);

    private float yaw = -90.0f;
    private float pitch = 0.0f;

    private Vector2f lastPos = new Vector2f();

    public FirstPersonCamera(Vector3f position) {
        this.position = position;
    }
    public void move(Vector3f translation) {
        position.add(right.mul(-translation.x));
        position.add(forward.mul(-translation.z));
        position.y += translation.y;
    }
    public void lookAround(Vector2f mousePos) {
        float dX = mousePos.x - lastPos.x;
        float dy = mousePos.y - lastPos.y;

        pitch -= dy * SENSITIVITY * Time.getDeltaTime();
        yaw += dX * SENSITIVITY * Time.getDeltaTime();

        lastPos.set(1280.0f/2, 720.0f/2);
        Mouse.setMousePos(lastPos);

        updateVectors();
    }

    public void updateVectors() {
        if (pitch > 89.0f) {
            pitch = 89.0f;
        }
        if(pitch < -89.0f) {
            pitch = -89.0f;
        }

        forward.set(
            (float) (Math.cos(Math.toRadians(pitch)) * Math.cos(Math.toRadians(yaw))),
            (float) (Math.sin(Math.toRadians(pitch))),
            (float) (Math.cos(Math.toRadians(pitch)) * Math.sin(Math.toRadians(yaw)))
        );
        forward.set(forward.normalize());

        right.set(right)
            .cross(forward, new Vector3f(0, 1, 0));
        right.set(right.normalize());

        up.set(up)
            .cross(forward, right);
        up.set(up.normalize());
    }
    public Matrix4f getViewMatrix() {
        Vector3f target = new Vector3f();
        target.x = position.x + forward.x;
        target.y = position.y + forward.y;
        target.z = position.z + forward.z;

        Matrix4f matrix = new Matrix4f();
        matrix.lookAt(position, target, up, matrix);
        return matrix;
    }
    public Vector3f getPosition() {
        return position;
    }
    public Matrix4f getProjectionMatrix() {
        return new Matrix4f().identity().perspective(Math.toRadians(45.0f), 1280.0f/720.0f, 0.1f, 100.0f);
    }
}
