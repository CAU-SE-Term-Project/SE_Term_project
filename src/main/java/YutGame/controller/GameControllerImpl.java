package YutGame.controller;

import YutGame.model.*;
import YutGame.view.GameView;

import java.util.*;

/** Controller: 게임 전체 흐름을 담당 */
public final class GameControllerImpl implements GameController {

    /* ─── 상태 ─── */
    private final Game           game   = new Game();
    private final List<GameView> views  = new ArrayList<>();

    /** 아직 사용하지 않은 윷 결과들 */
    private final List<YutResult> pendingResults = new ArrayList<>();
    /** 이번 이동에 사용할 윷 결과 */
    private YutResult curResult = null;

    /** 이번 턴에 ‘추가 턴(한 번 더 던지기)’을 얻었는지 */
    private boolean bonusTurn = false;

    /* ──────────────────────────────────────────────
     *  게임 시작 / 종료
     * ────────────────────────────────────────────── */
    @Override
    public void startNewGame(int nPlayers, int nPieces, String boardType) {
        try {
            game.init(nPlayers, nPieces, boardType);
            publish(GameEventType.GAME_STARTED, Map.of(
                    "numPlayers", nPlayers,
                    "numPieces",  nPieces,
                    "currentPlayer", game.currentPlayerId()));
        } catch (Exception e) {
            publishErr(e);
        }
    }

    @Override
    public void abortGame() {
        game.init(0, 0, "사각형");
        pendingResults.clear();
        curResult  = null;
        bonusTurn  = false;
        publish(GameEventType.GAME_ENDED, Map.of());
    }

    /* ──────────────────────────────────────────────
     *  윷 던지기
     * ────────────────────────────────────────────── */
    @Override public void rollRandomYut()            { handleRoll(game.rollRandom()); }
    @Override public void rollManualYut(YutResult r) { handleRoll(game.roll(r));      }

    private void handleRoll(YutResult r) {
        pendingResults.add(r);
        publish(GameEventType.YUT_ROLLED, Map.of("result", r));

        /* 윷·모처럼 extraTurn() == true 이면 한 번 더 던질 수 있으므로
           결과 선택 단계(RESULT_PICK_PHASE)를 건너뛴다.               */
        if (!r.extraTurn()) {
            publish(GameEventType.RESULT_PICK_PHASE, Map.of());
        }
    }

    /* ──────────────────────────────────────────────
     *  결과(윷값) 선택 → 말 선택
     * ────────────────────────────────────────────── */
    @Override
    public void chooseResult(YutResult chosen) {
        if (!pendingResults.contains(chosen)) return;  // 방어
        curResult = chosen;
        publish(GameEventType.PIECE_PICK_PHASE, Map.of("result", chosen));
    }

    @Override public List<YutResult> getPendingResults() { return List.copyOf(pendingResults); }

    /* ──────────────────────────────────────────────
     *  말 이동
     * ────────────────────────────────────────────── */
    @Override
    public void selectPiece(int pieceId) {
        try {
            /* 1) 이동에 쓸 윷 결과가 선택되었는지 확인 */
            if (curResult == null)
                throw new IllegalStateException("이동할 윷 결과가 선택되지 않았습니다");

            /* 2) 결과 소모 후 이동 수행 */
            game.roll(curResult);
            pendingResults.remove(curResult);
            curResult = null;

            MoveOutcome out = game.move(pieceId);

            /* 3) 이동 / 잡기 결과 브로드캐스트 */
            for (Integer id : out.movedPieceIds()) {
                publish(GameEventType.PIECE_MOVED, Map.of(
                        "pieceId",  id,
                        "newPos",   out.newPosition(),
                        "captured", out.capturedPieceIds()
                ));
            }

            /* 4) ‘잡기’가 발생했다면 추가 턴 플래그 ON */
            if (!out.capturedPieceIds().isEmpty()) {        // ← 핵심 변경
                bonusTurn = true;
            }

            /* 5) 다음 단계 결정 */
            if (game.finished()) {                          // 게임 종료
                publish(GameEventType.GAME_ENDED,
                        Map.of("winner", game.winnerId()));

            } else if (!pendingResults.isEmpty()) {         // 아직 안 쓴 결과 남음
                publish(GameEventType.RESULT_PICK_PHASE, Map.of());

            } else if (bonusTurn) {                         // 잡기로 얻은 추가 턴
                bonusTurn = false;                          // 소비하고 초기화
                publish(GameEventType.ROLLING_PHASE, Map.of());

            } else {                                        // 평범하게 턴 종료
                endTurn();
            }

        } catch (Exception e) {
            publishErr(e);
        }
    }

    /* ──────────────────────────────────────────────
     *  턴 종료
     * ────────────────────────────────────────────── */
    @Override
    public void endTurn() {
        pendingResults.clear();
        curResult  = null;
        bonusTurn  = false;                    // 반드시 초기화
        game.nextTurn();
        publish(GameEventType.TURN_CHANGED,
                Map.of("currentPlayer", game.currentPlayerId()));
    }

    /* ──────────────────────────────────────────────
     *  View 관리
     * ────────────────────────────────────────────── */
    @Override public void registerView(GameView v)   { views.add(v);    }
    @Override public void unregisterView(GameView v) { views.remove(v); }

    /* ──────────────────────────────────────────────
     *  유틸
     * ────────────────────────────────────────────── */
    private void publish(GameEventType t, Map<String, Object> m) {
        GameEvent evt = new GameEvent(t, m);
        views.forEach(v -> v.onGameEvent(evt));
    }
    private void publishErr(Exception ex) {
        publish(GameEventType.ERROR, Map.of("msg", ex.getMessage()));
    }

    /* ──────────────────────────────────────────────
     *  외부 조회용
     * ────────────────────────────────────────────── */
    @Override public int getCurrentPlayerId() { return game.currentPlayerId(); }

    @Override
    public int getRemainingPieceCount(int playerId) {
        Player pl = game.players().get(playerId);
        return (int) pl.pieces().stream()
                .filter(pc -> !pc.isHome(game.board()))
                .count();
    }
}
