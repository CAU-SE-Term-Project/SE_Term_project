package YutGame.ui;

import YutGame.controller.GameController;
import YutGame.controller.GameControllerImpl;
import YutGame.view.swing.GameBoardView;
import YutGame.view.swing.GameSetUpView;
import YutGame.view.swing.SwingGameViewAdapter;

import javax.swing.*;

public final class GameSetUpController {

    private final GameSetUpView setupView;

    /* ── 생성 & “시작하기” 버튼 훅 ── */
    public GameSetUpController(GameSetUpView view) {
        this.setupView = view;
        view.getStartButton().addActionListener(e -> launchGame());
    }

    /* ── 게임 시작 로직 ── */
    private void launchGame() {
        /* 1) 사용자가 선택한 옵션 수집 */
        int players   = setupView.getSelectedNumPlayers();
        int pieces    = setupView.getSelectedNumPieces();
        String boardT = setupView.getSelectedBoardType();

        /* 2) 백엔드(Controller) & 프런트(View) 생성 */
        GameController controller = new GameControllerImpl();
        GameBoardView boardView  = new GameBoardView(boardT, players, pieces);
        SwingGameViewAdapter adapter   = new SwingGameViewAdapter(boardView, controller);

        /* 3) Observer 등록 */
        controller.registerView(adapter);

        /* 4) 새 게임 초기화 */
        controller.startNewGame(players, pieces, boardT);

        /* 5) 화면 전환 */
        boardView.setVisible(true);
        setupView.dispose();
    }
}
