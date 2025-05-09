package YutGame.controller;

import YutGame.model.YutResult;
import java.util.List;

/**
 * View 계층이 호출하는 _공용 진입점_ (인터페이스).
 */
public interface GameController {

    /* ---------- 게임 수명 주기 ---------- */

    /** 새 게임 시작 */
    void startNewGame(int numPlayers, int numPieces, String boardType);

    /** 현재 게임 강제 종료 및 초기화 */
    void abortGame();

    /* ---------- 턴 처리 ---------- */

    /** 랜덤으로 윷을 던진다 */
    void rollRandomYut();

    /** 테스트‧디버깅용: 지정 결과로 윷을 던진다 */
    void rollManualYut(YutResult manualResult);

    List<YutResult> getPendingResults();

    /* ── [추가] 다이얼로그에서 결과 하나를 골랐을 때 ── */
    void chooseResult(YutResult chosen);

    /**
     * 플레이어가 말(pawn)을 선택했을 때 호출.
     * @param pieceId  ─ 선택된 말(고유 id)
     */
    void selectPiece(int pieceId);

    /** 턴 종료 (더 던질 윷이 없거나 말 이동이 끝났을 때) */
    void endTurn();

    /* ---------- View 등록 ---------- */

    int getCurrentPlayerId();

    /** Observer 패턴 – 다중 View 지원 */
    void registerView(YutGame.view.GameView view);
    void unregisterView(YutGame.view.GameView view);
}
