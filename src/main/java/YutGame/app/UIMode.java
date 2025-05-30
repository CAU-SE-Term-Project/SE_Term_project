package YutGame.app;

public enum UIMode {
    SWING,
    JAVAFX;

    public static UIMode from(String modeStr) {
        if (modeStr == null) return JAVAFX; // 기본값
        return switch (modeStr.toLowerCase()) {
            case "swing" -> SWING;
            case "javafx" -> JAVAFX;
            default -> throw new IllegalArgumentException("Unknown UI mode: " + modeStr);
        };
    }
}