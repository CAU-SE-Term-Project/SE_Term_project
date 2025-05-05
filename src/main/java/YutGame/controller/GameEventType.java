package YutGame.controller;

/** 컨트롤러 → 뷰 통지 이벤트 종류 */
public enum GameEventType {
    GAME_STARTED, TURN_CHANGED, YUT_ROLLED,
    PIECE_MOVED, GAME_ENDED, ERROR
}
