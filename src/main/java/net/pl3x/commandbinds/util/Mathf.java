package net.pl3x.commandbinds.util;

public class Mathf {
    public static double inverseLerp(double a, double b, double t) {
        return (t - a) / (b - a);
    }

    public static float inverseLerp(float a, float b, float t) {
        return (t - a) / (b - a);
    }

    public static double lerp(double start, double end, double step) {
        return start + step * (end - start);
    }

    public static float lerp(float start, float end, float step) {
        return start + step * (end - start);
    }

    public static float tween(float step) {
        return (step -= 1) * step * ((1.70158F + 1) * step + 1.70158F) + 1;
    }

    public static double clamp(double min, double max, double value) {
        return Math.min(Math.max(value, min), max);
    }

    public static float clamp(float min, float max, float value) {
        return Math.min(Math.max(value, min), max);
    }

    public static int clamp(int min, int max, int value) {
        return Math.min(Math.max(value, min), max);
    }
}
