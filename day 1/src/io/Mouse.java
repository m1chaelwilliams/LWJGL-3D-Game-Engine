package io;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFW;

public class Mouse {

    private static Vector2f mousePos;
    private static long window;

    public static void init(long window) {
        mousePos = new Vector2f();
        Mouse.window = window;
        GLFW.glfwSetCursorPosCallback(window, (w, xpos, ypos) -> {
            mousePos.x = (float)xpos;
            mousePos.y = (float)ypos;
        });
    }

    public static void setMousePos(Vector2f position) {
        GLFW.glfwSetCursorPos(Mouse.window, position.x, position.y);
    }
    public static Vector2f getMousePos() {return mousePos;}
}
