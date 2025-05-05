package YutGame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 사각형 윷판 – 분기점(5·10·15)에서 **자동으로 안쪽(대각선) 경로** 선택.
 */
public final class SquareBoard implements Board {

    public static final int FINISH = 32;           // 도착 노드 id

    /** key: 노드, value: 다음 노드 배열(길이 1=단일, 길이 2=[0]외곽·[1]안쪽) */
    private static final Map<Integer,int[]> NEXT = new HashMap<>();
    static {
        // 출발
        NEXT.put(START_POS, new int[]{0});

        // 외곽
        NEXT.put(0,new int[]{1});  NEXT.put(1,new int[]{2});  NEXT.put(2,new int[]{3});
        NEXT.put(3,new int[]{4});  NEXT.put(4,new int[]{5});
        NEXT.put(5,new int[]{6,21});   // 분기①
        NEXT.put(6,new int[]{7});  NEXT.put(7,new int[]{8});  NEXT.put(8,new int[]{9});
        NEXT.put(9,new int[]{10});
        NEXT.put(10,new int[]{11,25}); // 분기②
        NEXT.put(11,new int[]{12}); NEXT.put(12,new int[]{13});
        NEXT.put(13,new int[]{14}); NEXT.put(14,new int[]{15});
        NEXT.put(15,new int[]{16,27}); // 분기③
        NEXT.put(16,new int[]{17}); NEXT.put(17,new int[]{18});
        NEXT.put(18,new int[]{19}); NEXT.put(19,new int[]{20});
        NEXT.put(20,new int[]{FINISH});

        // 대각선
        NEXT.put(21,new int[]{22}); NEXT.put(22,new int[]{23});
        NEXT.put(23,new int[]{29});
        NEXT.put(25,new int[]{26}); NEXT.put(26,new int[]{29});
        NEXT.put(27,new int[]{28}); NEXT.put(28,new int[]{29});

        // 중앙 합류
        NEXT.put(29,new int[]{30}); NEXT.put(30,new int[]{31});
        NEXT.put(31,new int[]{20});

        // FINISH self‑loop
        NEXT.put(FINISH,new int[]{FINISH});
    }

    @Override public int getEndPosition() { return FINISH; }

    @Override
    public int next(int current, int steps) {
        if (steps == 0 || current == FINISH) return current;

        // 빽도(뒤로 1칸) – 외곽 링 기준 단순 구현
        if (steps < 0) {
            int prev = current == 0 ? START_POS : current - 1;
            return prev < START_POS ? START_POS : prev;
        }

        int pos = current;
        for (int i = 0; i < steps; i++) {
            int[] nexts = NEXT.getOrDefault(pos, new int[]{FINISH});
            pos = nexts.length == 1 ? nexts[0] : nexts[1]; // 분기 시 안쪽 선택
        }
        return pos;
    }

    @Override public BoardShape shape() { return BoardShape.SQUARE; }
}
