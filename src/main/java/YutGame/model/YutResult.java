package YutGame.model;

import java.util.Random;

/** 빽도(‑1)·도(1)·개(2)·걸(3)·윷(4)·모(5) */
public enum YutResult {
    BACK_DO(-1,false), DO(1,false), GAE(2,false), GEOL(3,false), YUT(4,true), MO(5,true);

    private final int steps;
    private final boolean extra;

    YutResult(int steps, boolean extra) { this.steps = steps; this.extra = extra; }

    public int steps() { return steps; }
    public boolean extraTurn() { return extra; }

    private static final Random RND = new Random();
    public static YutResult random() {
        return values()[RND.nextInt(values().length)];
    }
}
