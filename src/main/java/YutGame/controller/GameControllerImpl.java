package YutGame.controller;

import YutGame.model.*;
import YutGame.view.GameView;

import java.util.*;

public final class GameControllerImpl implements GameController {

    private final Game               game     = new Game();
    private final List<GameView>     views    = new ArrayList<>();

    /* ── 턴별 상태 ───────────────────────── */
    private final List<YutResult> pendingResults = new ArrayList<>();
    private       YutResult       curResult      = null;   // ← 말에 쓸 현재 결과

    /* ── GameController 구현 ─────────────── */

    @Override
    public void startNewGame(int nPlayers,int nPieces){
        try{
            game.init(nPlayers,nPieces);
            publish(GameEventType.GAME_STARTED, Map.of(
                    "numPlayers",nPlayers,"numPieces",nPieces,
                    "currentPlayer",game.currentPlayerId()));
        }catch(Exception e){publishErr(e);}
    }

    @Override public void abortGame(){
        game.init(0,0);
        pendingResults.clear();
        publish(GameEventType.GAME_ENDED, Map.of());
    }

    /* ---------- 던지기 ---------- */

    @Override public void rollRandomYut()  { handleRoll(game.rollRandom()); }
    @Override public void rollManualYut(YutResult manual){ handleRoll(game.roll(manual)); }

    private void handleRoll(YutResult r){
        pendingResults.add(r);
        publish(GameEventType.YUT_ROLLED, Map.of("result", r));

        if (!r.extraTurn())                           // 마지막 던지기
            publish(GameEventType.RESULT_PICK_PHASE, Map.of());
    }

    /* ---------- 결과 선택 ---------- */

    @Override public void chooseResult(YutResult chosen){
        if(!pendingResults.contains(chosen)) return;  // 방어

        curResult = chosen;                           // 이번에 사용할 결과
        publish(GameEventType.PIECE_PICK_PHASE, Map.of("result", chosen));
    }

    @Override public List<YutResult> getPendingResults(){
        return List.copyOf(pendingResults);
    }

    /* ---------- 말 선택 ---------- */

    @Override public void selectPiece(int pieceId){
        try{
            /* 1) 선택된 결과를 실제 roll 로 반영 */
            if(curResult == null)
                throw new IllegalStateException("이동할 결과가 선택되지 않았습니다");

            game.roll(curResult);       // lastRoll 업데이트 (중복 roll 아님)
            pendingResults.remove(curResult);
            curResult = null;           // 소비 완료

            /* 2) 이동 */
            MoveOutcome out = game.move(pieceId);
            publish(GameEventType.PIECE_MOVED, Map.of(
                    "pieceId", pieceId,
                    "newPos",  out.newPosition(),
                    "captured",out.capturedPieceIds()));

            /* 3) 다음 단계 결정 */
            if(game.finished()){
                publish(GameEventType.GAME_ENDED, Map.of("winner", game.winnerId()));
            } else if(!pendingResults.isEmpty()){          // 남은 결과 → 계속 선택
                publish(GameEventType.RESULT_PICK_PHASE, Map.of());
            } else if(out.extraTurn()){                    // 윷·모 효과 → 다시 던지기
                publish(GameEventType.ROLLING_PHASE, Map.of());
            } else {                                       // 턴 종료
                endTurn();
            }

        }catch(Exception e){ publishErr(e); }
    }

    @Override public void endTurn(){
        pendingResults.clear();    // 턴 넘어가면 초기화
        game.nextTurn();
        publish(GameEventType.TURN_CHANGED, Map.of("currentPlayer",
                game.currentPlayerId()));
    }

    /* ---------- View 관리 ---------- */
    @Override public void registerView(GameView v){ views.add(v); }
    @Override public void unregisterView(GameView v){ views.remove(v); }

    /* ---------- 내부 ---------- */
    private void publish(GameEventType t, Map<String,Object> m){
        GameEvent evt = new GameEvent(t, m);
        views.forEach(v -> v.onGameEvent(evt));
    }
    private void publishErr(Exception ex){
        publish(GameEventType.ERROR, Map.of("msg", ex.getMessage()));
    }
}
