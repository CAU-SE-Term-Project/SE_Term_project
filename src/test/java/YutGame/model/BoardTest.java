package YutGame.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    /** START_POS 상수가 0으로 정의돼 있는지 확인 */
    @Test
    void startPos_isZero() {
        assertEquals(0, Board.START_POS);
    }

    /** getEndPosition() 은 양수여야 한다 */
    @Test
    void endPosition_isPositive() {
        Board board = new SquareBoard();
        assertTrue(board.getEndPosition() > Board.START_POS);
    }

    /** next() 가 최소 1칸 이상 전진한다(빽도 제외) */
    @Test
    void next_advancesForward() {
        Board board = new SquareBoard();
        Piece dummy = new Piece(99, 0);

        int from = Board.START_POS;   // 0
        int to   = board.next(from, 2, dummy);   // 두 칸 이동

        assertTrue(to > from, "2칸 이동 시 위치가 증가해야 함");
    }
}
