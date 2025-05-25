package YutGame.controller;

import YutGame.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class GameControllerImplTest {

    private GameControllerImpl ctrl;
    private Game game;                     // 내부 Game 인스턴스를 리플렉션으로 획득

    @BeforeEach
    void setUp() throws Exception {
        ctrl = new GameControllerImpl();
        ctrl.startNewGame(2, 2, "사각형");

        // ── private final Game game 필드 꺼내기 ──
        Field g = GameControllerImpl.class.getDeclaredField("game");
        g.setAccessible(true);
        game = (Game) g.get(ctrl);
    }

    @Test
    void rollYut_addsPendingResult() {
        int before = ctrl.getPendingResults().size();
        ctrl.rollManualYut(YutResult.DO);
        int after  = ctrl.getPendingResults().size();

        assertEquals(before + 1, after);
        assertTrue(ctrl.getPendingResults().contains(YutResult.DO));
    }

    @Test
    void chooseResult_thenSelectPiece_consumesResultAndMoves() {
        // 1) 결과 등록
        ctrl.rollManualYut(YutResult.GAE);
        ctrl.chooseResult(YutResult.GAE);

        // 2) 현재 플레이어의 첫 말 선택
        int curId  = ctrl.getCurrentPlayerId();
        Player p   = game.players().get(curId);
        Piece  pc  = p.pieces().get(0);
        int    before = pc.position();

        ctrl.selectPiece(pc.id());                 // 실제 이동

        // 3) 결과가 소모되었는지 & 위치가 변했는지
        assertFalse(ctrl.getPendingResults().contains(YutResult.GAE));
        assertNotEquals(before, pc.position());
    }

    @Test
    void endTurn_switchesCurrentPlayer() {
        int before = ctrl.getCurrentPlayerId();
        ctrl.endTurn();
        int after  = ctrl.getCurrentPlayerId();

        assertNotEquals(before, after);
    }

    @Test
    void selectPiece_withoutChoose_doesNothing() {
        // 현재 플레이어와 첫 번째 말
        Player player = game.players().get(ctrl.getCurrentPlayerId());
        Piece  piece  = player.pieces().get(0);

        int beforePos = piece.position();
        int beforePending = ctrl.getPendingResults().size();

        // chooseResult() 호출 없이 바로 selectPiece()
        ctrl.selectPiece(piece.id());   // 예외를 던지지 않고 내부에서 publishErr(e) 처리

        // 위치·상태가 그대로인지 확인
        int afterPos = piece.position();
        int afterPending = ctrl.getPendingResults().size();

        assertEquals(beforePos, afterPos,      "결과를 선택하지 않았으므로 말이 이동하면 안 됨");
        assertEquals(beforePending, afterPending, "pendingResults 크기도 변하면 안 됨");
    }

}
