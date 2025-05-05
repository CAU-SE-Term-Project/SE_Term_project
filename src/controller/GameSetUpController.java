//시작하기 버튼 눌렀을때 시행되는 controller
package controller;

import view.swingui.GameSetUpView;
import view.swingui.GameBoardView;

public class GameSetUpController {
    private final GameSetUpView setupView;

    public GameSetUpController(GameSetUpView setupView) {
        this.setupView = setupView;
        StartBtnController();
    }

    private void StartBtnController(){
        setupView.getStartButton().addActionListener(e -> launchBoard());
    }

    private void launchBoard() {
        int numPlayers = setupView.getSelectedNumPlayers(); // → RadioButton에서 선택된 값
        int numPieces = setupView.getSelectedNumPieces();
        String boardType = setupView.getSelectedBoardType();

        GameBoardView boardView = new GameBoardView(boardType, numPlayers, numPieces);
        new GamePlayController(boardView); // 게임 컨트롤러 연결
        boardView.setVisible(true);

        setupView.dispose(); // 설정 창 닫기
    }



}
