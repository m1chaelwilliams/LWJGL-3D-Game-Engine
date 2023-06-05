package io;

import java.util.HashMap;
import java.util.Map;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;

public class Keyboard {
    private static Map <Keys, Integer> keymapper = new HashMap<>() {{
        put(Keys.W, GLFW.GLFW_KEY_W);
        put(Keys.A, GLFW.GLFW_KEY_A);
        put(Keys.S, GLFW.GLFW_KEY_S);
        put(Keys.D, GLFW.GLFW_KEY_D);
        put(Keys.SPACE, GLFW.GLFW_KEY_SPACE);
        put(Keys.LSHIFT, GLFW.GLFW_KEY_LEFT_SHIFT);
        put(Keys.ESCAPE, GLFW.GLFW_KEY_ESCAPE);
    }};

    private static GLFWKeyCallback keyCallback;

    private static boolean[] keys = new boolean[GLFW.GLFW_KEY_LAST];

    public static void init(long window) {
        keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (key >= 0 && key < GLFW.GLFW_KEY_LAST) {
                    keys[key] = action != GLFW.GLFW_RELEASE;
                }
            }
        };

        GLFW.glfwSetKeyCallback(window, keyCallback);
    }

    public static void cleanup() {
        keyCallback.free();
    }

    public static boolean isKeyDown(Keys key) {
        int keycode = keymapper.get(key);
        if (keycode >= 0 && keycode < GLFW.GLFW_KEY_LAST) {
            return keys[keycode];
        }
        return false;
    }


}
