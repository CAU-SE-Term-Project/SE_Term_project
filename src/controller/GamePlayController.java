//gameboardview의 controller 정의
package controller;

import view.swingui.GameBoardView;
import javax.swing.*;

public class GamePlayController {
    private final GameBoardView boardView;

    public GamePlayController(GameBoardView boardView) {
        this.boardView = boardView;
        HandleYutControlPanel();
        HandlePieceSelectionPanel();
    }

    private void HandleYutControlPanel() {
        // 윷 버튼 처리
        for(JButton btn : boardView.getYutButtons()){
            btn.addActionListener(e -> {
                String result = btn.getText();
                handleYutResult(result);
            });
        }

        // 랜덤 버튼 처리
        boardView.getRandomButton().addActionListener(e -> {
            String[] results = {"도", "개", "걸", "윷", "모"};
            int idx = (int) (Math.random() * results.length);
            handleYutResult(results[idx]);
        });
    }

    private void HandlePieceSelectionPanel(){
        JButton[][] pieces = boardView.getPieceButtons();
        for(int i=0; i<pieces.length; i++){
            for(int j=0; j<pieces[i].length; j++){
                final int player = i + 1;
                final int piece = j + 1;
                pieces[i][j].addActionListener(e -> handlePieceSelection(player, piece));
            }
        }
    }


    private void handleYutResult(String result) {
        JOptionPane.showMessageDialog(boardView, "선택된 윷 결과: " + result);
        // TODO: 게임 진행 로직과 연결 필요
    }

    private void handlePieceSelection(int playerNumber, int pieceNumber) {
        JOptionPane.showMessageDialog(boardView, "선택된 플레이어: " + playerNumber + ", 말: " + pieceNumber);
        // TODO: 실제 말 이동 로직과 연결
    }
}
