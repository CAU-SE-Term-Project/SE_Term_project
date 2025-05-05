package view.swingui;

import javax.swing.*;
import java.awt.*;

public class GameBoardView extends JFrame {
    private final int numPlayers;
    private final int numPieces;
    private JButton[] yutButtons;
    private JButton randomButtons;
    private JButton[][] pieceButtons;

    public JButton[] getYutButtons() {return yutButtons;}
    public JButton getRandomButton() {return randomButtons;}
    public JButton[][] getPieceButtons() {return pieceButtons;}

    //게임 보드 생성
    public GameBoardView(String boardType, int numPlayers, int numPieces) {
        this.numPlayers = numPlayers;
        this.numPieces = numPieces;

        setTitle("윷놀이 판 (" + boardType + ")");
        setSize(600, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        add(PlayerInfoPanel(), BorderLayout.NORTH);
        add(new BoardPanel(boardType), BorderLayout.CENTER);
        add(YutControlPanel(), BorderLayout.SOUTH);
    }

    // 상단 플레이어와 남은 말 표시
    private JPanel PlayerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(numPlayers, 1));
        for (int i = 1; i <= numPlayers; i++) {
            JLabel label = new JLabel("Player " + i + " - 남은 말: " + numPieces);
            panel.add(label);
        }
        return panel;
    }

    // 말 선택부분 view
    private JPanel YutControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel yutPanel = new JPanel();
        yutPanel.setBorder(BorderFactory.createTitledBorder("윷 던지기"));

        String[] results = {"도", "개", "걸", "윷", "모"};
        yutButtons = new JButton[results.length];

        for(int i=0; i< results.length; i++){
            yutButtons[i] = new JButton(results[i]);
            yutPanel.add(yutButtons[i]);
        }

        randomButtons = new JButton("Random");
        yutPanel.add(randomButtons);

        panel.add(yutPanel, BorderLayout.NORTH);
        panel.add(PieceSelectionPanel(), BorderLayout.SOUTH);

        return panel;
    }

    // 플레이어 선택 부분
    private JPanel PieceSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(numPlayers, 1));  // 플레이어 수만큼 행

        pieceButtons = new JButton[numPlayers][numPieces];


        for (int player = 0; player < numPlayers; player++) {
            JPanel playerPanel = new JPanel();
            playerPanel.setBorder(BorderFactory.createTitledBorder("플레이어 " + player));

            for (int piece = 0; piece < numPieces; piece++) {
                JButton btn = new JButton("말 " + (piece + 1));
                pieceButtons[player][piece] = btn;
                playerPanel.add(btn);
            }

            panel.add(playerPanel);
        }

        return panel;
    }
}
