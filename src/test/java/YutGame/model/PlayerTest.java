package YutGame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PlayerTest {

    @Test
    void hasWon_true_whenAllPiecesHome() {
        Board board  = new SquareBoard();
        Player pl    = new Player(1);

        Piece p1 = new Piece(1,1);
        Piece p2 = new Piece(2,1);
        p1.setPosition(board.getEndPosition()+1);
        p2.setPosition(board.getEndPosition()+2);
        pl.pieces().add(p1);
        pl.pieces().add(p2);

        assertTrue(pl.hasWon(board));
    }
}
