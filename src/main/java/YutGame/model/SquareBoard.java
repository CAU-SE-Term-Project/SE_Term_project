package YutGame.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 사각형 윷판 – 분기점(5·10·25)에서 **자동으로 안쪽(대각선) 경로** 선택.
 */
public final class SquareBoard implements Board {

    public static final int FINISH = 30;           // 도착 노드 id

    /** key: 노드, value: 다음 노드 배열(길이 1=단일, 길이 2=[0]외곽·[1]안쪽) */
    private static final Map<Integer,int[]> NEXT = new HashMap<>();
    static {
        // 출발
        NEXT.put(START_POS, new int[]{0});

        // 외곽 (반시계 방향 순서로 매핑)
        NEXT.put(0, new int[]{1});   // 출발 위
        NEXT.put(1, new int[]{2});
        NEXT.put(2, new int[]{3});
        NEXT.put(3, new int[]{4});
        NEXT.put(4, new int[]{5});
        NEXT.put(5, new int[]{6, 21});   // 분기점 1
        NEXT.put(6, new int[]{7});
        NEXT.put(7, new int[]{8});
        NEXT.put(8, new int[]{9});
        NEXT.put(9, new int[]{10});
        NEXT.put(10, new int[]{11, 23}); // 분기점 2
        NEXT.put(11, new int[]{12});
        NEXT.put(12, new int[]{13});
        NEXT.put(13, new int[]{14});
        NEXT.put(14, new int[]{15});
        NEXT.put(15, new int[]{16});
        NEXT.put(16, new int[]{17});
        NEXT.put(17, new int[]{18});
        NEXT.put(18, new int[]{19});
        NEXT.put(19, new int[]{20});
        NEXT.put(20, new int[]{FINISH});

        // 대각선 경로 (5 -> 중앙 -> 15)
        NEXT.put(21, new int[]{22});      // 5 → 21 → 22
        NEXT.put(22, new int[]{25});      // → 25

        // 대각선 경로 (10 -> 중앙 -> 15)
        NEXT.put(23, new int[]{24});      // 10 → 23 → 24
        NEXT.put(24, new int[]{25});
        // 중앙 합류 분기
        NEXT.put(25, new int[]{26, 28});  // 중앙 노드 25
        NEXT.put(26, new int[]{27});
        NEXT.put(27, new int[]{15});
        NEXT.put(28, new int[]{29});

        // 마지막 중앙 to 20
        NEXT.put(29, new int[]{20});

        // FINISH self-loop
        NEXT.put(FINISH, new int[]{FINISH});
    }

    @Override
    public int getEndPosition() {
        return FINISH;
    }

    @Override
    public int next(int current, int steps, Piece piece) {
        if (steps == 0 || current == FINISH) return current;

        // 백도 구현
        if (steps == -1){
            if (piece.peekPath() == 1){ // 현재 위치가 1이면
                return 20;
            }
            else {
                piece.popPath();
                return piece.popPath();
            }
        }

        int pos = current;
        for (int i = 0; i < steps; i++) {
            int[] nexts = NEXT.getOrDefault(pos, new int[]{FINISH});

            if (nexts.length == 2) {
                if (i == 0 && pos == current) {
                    pos = nexts[1]; // 안쪽 경로 (출발 위치가 분기점일 때만)
                } else {
                    pos = nexts[0]; // 외곽 경로
                }
            } else {
                pos = nexts[0];
            }
            piece.pushPath(pos);
        }
        return pos;
    }

    @Override
    public BoardShape shape() {
        return BoardShape.SQUARE;
    }
}
