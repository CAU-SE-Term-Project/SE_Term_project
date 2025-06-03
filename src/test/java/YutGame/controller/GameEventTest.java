package YutGame.controller;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameEventTest {

    @Test
    void createEvent_withPayload_storesTypeAndData() {
        GameEventType type   = GameEventType.PIECE_MOVED;        // 실제 enum 상수
        Map<String,Object> data = Map.of(
                "pieceId",  7,
                "from",     3,
                "to",       5
        );

        GameEvent event = new GameEvent(type, data);

        assertEquals(type, event.getType());
        assertEquals(data, event.getPayload());
        assertEquals(7,   event.getPayload().get("pieceId"));
    }

    @Test
    void createEvent_withoutPayload_allowsNull() {
        GameEvent event = new GameEvent(GameEventType.YUT_ROLLED, null);

        assertEquals(GameEventType.YUT_ROLLED, event.getType());
        assertNull(event.getPayload());
    }
}
