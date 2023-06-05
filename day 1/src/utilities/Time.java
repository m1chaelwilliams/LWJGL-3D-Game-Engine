package utilities;

public class Time {
    private static long lastFrameTime;
    private static float deltaTime;

    public static void init() {
        lastFrameTime = getCurrentTime();
        deltaTime = 0.0f;
    }
    public static void update() {
        long currentFrameTime = getCurrentTime();
        deltaTime = (currentFrameTime - lastFrameTime) / 1000.0f;
        lastFrameTime = currentFrameTime;
    }

    public static float getDeltaTime() {
        return deltaTime;
    }
    public static long getCurrentTime() {
        return System.nanoTime() / 1_000_000;
    }
}
