package YutGame.ui;

import YutGame.controller.GameController;
import YutGame.controller.GameControllerImpl;
import YutGame.view.javafx.GameBoardViewFX;
import YutGame.view.javafx.GameSetUpViewFX;
import YutGame.view.swing.GameBoardView;
import YutGame.view.swing.SwingGameViewAdapter;
import javafx.stage.Stage;

public class GameSetUpControllerFX {
    private final GameSetUpViewFX setUpViewFX;

    public GameSetUpControllerFX(GameSetUpViewFX view) {
        this.setUpViewFX = view;
        view.getStartButton().setOnAction(actionEvent -> launchGame());
    }

        /* ── 게임 시작 로직 ── */
        private void launchGame() {
            /* 1) 사용자가 선택한 옵션 수집 */
            int players   = setUpViewFX.getSelectedNumPlayers();
            int pieces    = setUpViewFX.getSelectedNumPieces();
            String boardT = setUpViewFX.getSelectedBoardType();

            /* 2) 백엔드(Controller) & 프런트(View) 생성 */
            GameController controller = new GameControllerImpl();

            /* javafx 수정 부분*/
            Stage gamestage = new Stage();
            GameBoardViewFX boardViewFX  = new GameBoardViewFX(gamestage, boardT, players, pieces);
            //SwingGameViewAdapter adapter   = new SwingGameViewAdapter(boardView, controller);

            /* 3) Observer 등록 */
            //controller.registerView(adapter);

            /* 4) 새 게임 초기화 */
            controller.startNewGame(players, pieces, boardT);

            /* 5) 화면 전환 */
            boardViewFX.show();
            setUpViewFX.closeWindow();

        }
}
