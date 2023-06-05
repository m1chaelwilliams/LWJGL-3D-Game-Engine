package engineTester;


import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import engine.Engine;
import environment.*;
import gameObjects.GameObject;
import gameObjects.Light;
import graphics.ShaderProgram;
import io.Keyboard;
import io.Keys;
import io.Mouse;
import io.Window;
import models.*;
import renderEngine.*;
import utilities.Time;

public class Main {
    Window window;
    TexturedModel model;
    TexturedModel lightModel;
    Light light;
    ShaderProgram program;
    FirstPersonCamera camera;

    GameObject object;
    GameObject cat;

    int dir = 1;
    public static void main(String[] args) {
        Main main = new Main();
        main.init();
        main.loop();
        main.disose();
    }
    public void init() {
        window = new Window(1280, 720, "Game Engine");
        window.create();
        Engine.init();

        float[] vertices = {
            -0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,

            0.5f, -0.5f, 0.5f,
            0.5f, -0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, 0.5f,

            0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,
            0.5f, 0.5f, -0.5f,

            -0.5f, -0.5f, -0.5f,
            -0.5f, -0.5f, 0.5f,
            -0.5f, 0.5f, 0.5f,
            -0.5f, 0.5f, -0.5f,

            -0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, 0.5f,
            0.5f, 0.5f, -0.5f,
            -0.5f, 0.5f, -0.5f,

            -0.5f, -0.5f, -0.5f,
            0.5f, -0.5f, -0.5f, 
            0.5f, -0.5f, 0.5f,
            -0.5f, -0.5f, 0.5f
        };
        float[] normals = {
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,
            0.0f, 0.0f, 1.0f,

            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,
            1.0f, 0.0f, 0.0f,

            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,
            0.0f, 0.0f, -1.0f,

            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,
            -1.0f, 0.0f, 0.0f,

            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,
            0.0f, 1.0f, 0.0f,

            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f,
            0.0f, -1.0f, 0.0f
        };
        float[] uv = {
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,
            
            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f,

            0.0f, 1.0f,
            1.0f, 1.0f,
            1.0f, 0.0f,
            0.0f, 0.0f
        };
        int[] indices = {
            0,1,2,
            2,3,0,

            4,5,6,
            6,7,4,

            8,9,10,
            10,11,8,

            12,13,14,
            14,15,12,

            16,17,18,
            18,19,16,
            
            20,21,22,
            22,23,20
        };

        model = Loader.createTexturedModel(vertices, normals, uv, indices, "src/res/dirtTex.PNG");
        
        cat = new GameObject(Loader.loadObjFile("src/objs/cat.obj", "src/res/cat.jpg"));
        cat.setScale(new Vector3f(0.1f, 0.1f, 0.1f));
        cat.setRotation(new Vector3f(-90.0f, 0.0f, 0.0f));

        object = new GameObject(model);
        object.move(new Vector3f(0, 0, -3));

        light = new Light(new Vector3f(1, 4, -3), 0.1f, 0.8f, 1.0f);

        camera = new FirstPersonCamera(new Vector3f(0, 0, 3));

        program = new ShaderProgram("default.vert", "default.frag");
        program.bind();
        program.configureLighting(light, camera.getPosition());
        GL11.glEnable(GL11.GL_DEPTH_TEST);
    }
    public void loop() {
        while(!window.closed()) {
            update();
            render();
        }
    }
    public void disose() {
        window.close();
    }

    public void update() {
        window.update();
        Engine.update();

        object.rotate(new Vector3f(0, 30*Time.getDeltaTime(), 0));
        program.configureLighting(light, camera.getPosition());

        if(light.getPosition().z > 10 || light.getPosition().z < -10) {
            dir = -dir;
        }
        light.move(new Vector3f(0, 0, dir*Time.getDeltaTime()*10));

        float speed = 3f * Time.getDeltaTime();
        Vector3f movement = new Vector3f();

        if(Keyboard.isKeyDown(Keys.W)) {
            movement.z -= speed;
        }
        if(Keyboard.isKeyDown(Keys.S)) {
            movement.z += speed;
        }
        if(Keyboard.isKeyDown(Keys.A)) {
            movement.x -= speed;
        }
        if(Keyboard.isKeyDown(Keys.D)) {
            movement.x += speed;
        }
        if(Keyboard.isKeyDown(Keys.SPACE)) {
            movement.y += speed;
        }
        if(Keyboard.isKeyDown(Keys.LSHIFT)) {
            movement.y -= speed;
        }
        if(Keyboard.isKeyDown(Keys.ESCAPE)) {
            window.close();
        }
        camera.move(movement);
        camera.lookAround(Mouse.getMousePos());
    }
    public void render() {
        GL11.glClearColor(0.2f, 0.3f, 0.8f, 1.0f);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        
        Renderer.render(object, program, camera, light);
        Renderer.render(cat, program, camera, light);
    }
}
