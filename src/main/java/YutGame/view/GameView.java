package YutGame.view;

import YutGame.controller.GameEvent;

/** Observer 패턴 – GUI/콘솔 등 View가 구현 */
public interface GameView {
    void onGameEvent(GameEvent evt);
}


