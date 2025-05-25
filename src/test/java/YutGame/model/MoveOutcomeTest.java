package YutGame.model;

import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MoveOutcomeTest {

    @Test
    void record_fields_areStored() {
        MoveOutcome out = new MoveOutcome(10,
                List.of(1,2), List.of(3), true);

        assertEquals(10, out.newPosition());
        assertEquals(List.of(1,2), out.movedPieceIds());
        assertEquals(List.of(3),   out.capturedPieceIds());
        assertTrue(out.extraTurn());
    }
}
