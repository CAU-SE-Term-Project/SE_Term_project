package YutGame.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class YutResultTest {

    @Test
    void steps_and_extraTurn_flags() {
        assertEquals(1,  YutResult.DO.steps());
        assertFalse(YutResult.GAE.extraTurn());
        assertTrue (YutResult.YUT.extraTurn());
    }
}
