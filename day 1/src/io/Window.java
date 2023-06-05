package io;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.system.MemoryUtil.*;

import java.nio.IntBuffer;

public class Window {
    private int width, height;
    private String title;
    private long window;

    public Window(int width, int height, String title) {
        this.width = width;
        this.height = height;
        this.title = title;
    }
    public void create() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!GLFW.glfwInit()) {
            throw new IllegalStateException("Failed to intialize GLFW");
        }

        // GLFW parameters
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MAJOR, 3);
        GLFW.glfwWindowHint(GLFW.GLFW_CONTEXT_VERSION_MINOR, 2);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_FORWARD_COMPAT, GL11.GL_TRUE);
        GLFW.glfwWindowHint(GLFW.GLFW_OPENGL_PROFILE, GLFW.GLFW_OPENGL_CORE_PROFILE);
        GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, GLFW.GLFW_FALSE);
        GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, GLFW.GLFW_TRUE);

        // create window
        window = GLFW.glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            throw new RuntimeException("Failed to create window");
        }

        Keyboard.init(window);
        Mouse.init(window);
        Mouse.setMousePos(new Vector2f(getWidth()/2, getHeight()/2));

        // resize callback
        GLFW.glfwSetFramebufferSizeCallback(window, (window, w, h) -> {
            this.width = w;
            this.height = h;
            GL11.glViewport(0,0,w, h);
        });


        // get the thread stack and push a new frame
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer pWidth = stack.mallocInt(1); // allocating memory for an int
            IntBuffer pHeight = stack.mallocInt(1); // allocating memory for another int

            // get the window size
            GLFW.glfwGetWindowSize(window, pWidth, pHeight); // sending the two ints as references and having them be returned as the values for width and height

            // get the resolution of the primary monitor
            GLFWVidMode vidmode = GLFW.glfwGetVideoMode(GLFW.glfwGetPrimaryMonitor());

            // center the window
            GLFW.glfwSetWindowPos(window, (vidmode.width() - pWidth.get(0))/2, (vidmode.height() - pHeight.get(0))/2);
        }

        GLFW.glfwMakeContextCurrent(window);
        GLFW.glfwShowWindow(window);
        GLFW.glfwSetInputMode(window, GLFW.GLFW_CURSOR, GLFW.GLFW_CURSOR_HIDDEN);
        GL.createCapabilities();
        GL11.glViewport(0, 0, width, height);
    }
    public void update() {
        GLFW.glfwSwapBuffers(window);
        GLFW.glfwPollEvents();
    }
    public int getWidth() {
        IntBuffer w = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(window, w, null);
        return w.get(0);
    }
    public int getHeight() {
        IntBuffer h = BufferUtils.createIntBuffer(1);
        GLFW.glfwGetWindowSize(window, null, h);
        return h.get(0);
    }
    public boolean closed() {
        return GLFW.glfwWindowShouldClose(window);
    }
    public void close() {
        GLFW.glfwDestroyWindow(window);
        GLFW.glfwTerminate();
        GLFW.glfwSetErrorCallback(null).free();
    }
}
