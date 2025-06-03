package YutGame.controller;

/** 컨트롤러 → 뷰 통지 & UI‑흐름 상태 식별용 이벤트 종류 */
public enum GameEventType {

    /* ===== 기존 이벤트 ===== */
    GAME_STARTED,
    TURN_CHANGED,
    YUT_ROLLED,
    PIECE_MOVED,
    GAME_ENDED,
    ERROR,

    /* ===== UI 흐름(Phase) ===== */
    /** 플레이어가 윷을 던질 수 있는 단계 */
    ROLLING_PHASE,

    /** 던진 결과 목록 중 다음에 사용할 결과를 고르는 단계 */
    RESULT_PICK_PHASE,

    /** 말을 선택해 실제 이동을 수행하는 단계 */
    PIECE_PICK_PHASE
}
