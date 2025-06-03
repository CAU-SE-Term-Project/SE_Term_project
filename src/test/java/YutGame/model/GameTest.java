package YutGame.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;

    @BeforeEach
    void initGame() {
        game = new Game();
        game.init(2, 2, "사각형");
    }

    @Test
    void init_createsPlayersAndPieces() {
        Map<Integer,Player> players = game.players();
        assertEquals(2, players.size());
        players.values().forEach(pl -> assertEquals(2, pl.pieces().size()));
    }

    @Test
    void rollRandom_setsLastRollInternally() {
        YutResult r = game.rollRandom();
        assertNotNull(r);
        assertTrue(r.steps() >= -1 && r.steps() <= 5);
    }

    @Test
    void move_updatesPiecePosition() {
        Piece piece = game.players()
                .get(game.currentPlayerId())
                .pieces().get(0);

        game.roll(YutResult.DO);
        int before = piece.position();
        MoveOutcome out = game.move(piece.id());
        assertNotEquals(before, out.newPosition());
        assertTrue(out.movedPieceIds().contains(piece.id()));
    }

    @Test
    void move_withoutRoll_throws() {
        Piece piece = game.players()
                .get(game.currentPlayerId())
                .pieces().get(0);
        assertThrows(IllegalStateException.class,
                () -> game.move(piece.id()));
    }
}
