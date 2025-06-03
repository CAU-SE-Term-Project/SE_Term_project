package YutGame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Stack;
import static org.junit.jupiter.api.Assertions.*;

public class PieceTest {

    private Piece piece;
    private Board board;

    @BeforeEach
    void setUp() {
        piece = new Piece(1, 1);
        board = new SquareBoard();
    }

    @Test
    void initial_state() {
        assertEquals(Board.START_POS, piece.position());
        assertTrue(piece.pathIsEmpty());
    }

    @Test
    void path_push_pop_peek() {
        piece.pushPath(3);
        piece.pushPath(5);
        assertEquals(5, piece.peekPath());
        assertEquals(5, piece.popPath());
        assertEquals(3, piece.peekPath());
    }

    @Test
    void isHome_true_afterEnd() {
        piece.setPosition(board.getEndPosition() + 1);
        assertTrue(piece.isHome(board));
    }
}
