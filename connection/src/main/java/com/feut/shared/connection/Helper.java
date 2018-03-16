package com.feut.shared.connection;

public class Helper {
    private static boolean isDebugMode = false;

    public static void toggleDebugMode(boolean _isDebugMode) {
        isDebugMode = _isDebugMode;
    }

    public static boolean isDebugMode() {
        return isDebugMode;
    }

    public static void Log(String log) {
        if (isDebugMode()) {
            System.out.println(log);
        }
    }
}
