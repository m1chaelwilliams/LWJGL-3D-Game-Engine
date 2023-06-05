package engine;

import io.Keyboard;
import renderEngine.Loader;
import renderEngine.Renderer;
import utilities.Time;

public class Engine {
    public static void init() {
        Time.init();
        Renderer.init();
    }
    public static void update() {
        Time.update();
    }
    public static void dispose() {
        Loader.dispose();
        Keyboard.cleanup();
        Renderer.dispose();
    }
}
