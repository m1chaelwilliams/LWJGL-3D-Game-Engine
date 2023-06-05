package renderEngine;

import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import environment.FirstPersonCamera;
import gameObjects.GameObject;
import gameObjects.Light;
import graphics.ShaderProgram;
import models.TexturedModel;

public class Renderer {
    private static ShaderProgram defaultShader;
    private static Light defaultLight;
    public static void init() {
        defaultShader = new ShaderProgram("default.vert", "default.frag");
        defaultShader.bind();
        defaultLight = new Light(new Vector3f(0, 5, 0), 0.1f, 0.8f, 1.0f);
    }
    public static void dispose() {
        defaultShader.delete();
    }

    public static void configureLighting(Vector3f cameraPosition) {
        defaultShader.configureLighting(defaultLight, cameraPosition);
    }
    public static void render(GameObject obj, ShaderProgram shader, FirstPersonCamera cam, Light light) {
        shader.bind();
        _render(obj, shader, cam);
    }
    public static void render(GameObject obj, FirstPersonCamera camera) {
        defaultShader.bind();
        configureLighting(camera.getPosition());
        _render(obj, defaultShader, camera);
    }

    private static void _render(GameObject obj, ShaderProgram shader, FirstPersonCamera cam) {
        TexturedModel model = obj.getModel();
        model.getVAO().bind();
        model.getEBO().bind();
        model.getTexture().bind();
        shader.putDataIntoUniformMat4(obj.getWorldMatrix(), "model");
        shader.putDataIntoUniformMat4(cam.getViewMatrix(), "view");
        shader.putDataIntoUniformMat4(cam.getProjectionMatrix(), "proj");
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getEBO().getLength(), GL11.GL_UNSIGNED_INT, 0);
    }
}
