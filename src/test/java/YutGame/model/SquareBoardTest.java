package YutGame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SquareBoardTest {

    private final SquareBoard board = new SquareBoard();
    private final Piece dummy       = new Piece(1, 0);

    /** 0 ➔ 1 한 칸 전진이 정상인지 */
    @Test
    void next_oneStep_fromStart() {
        int to = board.next(Board.START_POS, 1, dummy);
        assertEquals(1, to);
    }

    /** 도착 직전(END)에서 1칸 이동하면 FINISH 로 간다 */
    @Test
    void next_movesToFinish() {
        int finish = board.getEndPosition();  // 30
        int from   = 20;                      // 경로 정의상 FINISH 직전 노드
        int to     = board.next(from, 1, dummy);

        assertEquals(finish, to);
    }

    /** 여러 번 연속 이동하더라도 범위를 넘지 않는다 */
    @Test
    void next_neverExceedsFinishByMoreThanSteps() {
        int pos = Board.START_POS;

        for (int i = 0; i < 20; i++) {
            pos = board.next(pos, 1, dummy);  // 한 칸씩 20번
        }
        assertTrue(pos <= board.getEndPosition());
    }
}
