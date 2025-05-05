// 초기 설정 화면 view
package view.swingui;

import javax.swing.*;
import java.awt.*;

public class GameSetUpView extends JFrame{
    private final ButtonGroup playersGroup = new ButtonGroup();
    private final ButtonGroup piecesGroup = new ButtonGroup();
    private final ButtonGroup boardGroup = new ButtonGroup();
    private final JButton startButton = new JButton("시작하기");

    public JButton getStartButton() {return startButton;}

    public int getSelectedNumPlayers() {return Integer.parseInt(playersGroup.getSelection().getActionCommand());}
    public int getSelectedNumPieces() {return Integer.parseInt(piecesGroup.getSelection().getActionCommand());}
    public String getSelectedBoardType() {return boardGroup.getSelection().getActionCommand();}

    //생성자
    public GameSetUpView() {
        setTitle("게임 설정");
        setSize(400, 350);
        setLayout(new GridLayout(0, 1));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(createPlayerPanel());
        add(createPiecesPanel());
        add(createBoardPanel());
        add(startButton);
    }


    //player 패널 추가
    private JPanel createPlayerPanel() {
        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("사용자 수 선택"));
        for (int i = 2; i <= 4; i++) {
            JRadioButton button = new JRadioButton(i + "명");
            button.setActionCommand(Integer.toString(i));
            playersGroup.add(button);
            playerPanel.add(button);
            if (i == 2) button.setSelected(true);
        }
        return playerPanel;
    }

    //말 개수 선택 패널 추가
    private JPanel createPiecesPanel() {
        JPanel piecesPanel = new JPanel();
        piecesPanel.setBorder(BorderFactory.createTitledBorder("말 개수 선택"));
        for (int i = 2; i <= 5; i++) {
            JRadioButton button = new JRadioButton(i + "개");
            button.setActionCommand(Integer.toString(i));
            piecesGroup.add(button);
            piecesPanel.add(button);
            if (i == 2) button.setSelected(true);
        }
        return piecesPanel;
    }

    //판 선택 패널 추가
    private JPanel createBoardPanel() {
        JPanel boardPanel = new JPanel();
        boardPanel.setBorder(BorderFactory.createTitledBorder("윷판 선택"));
        String[] boardTypes = {"사각형", "오각형", "육각형"};
        for (String type : boardTypes) {
            JRadioButton button = new JRadioButton(type);
            button.setActionCommand(type);
            boardGroup.add(button);
            boardPanel.add(button);
            if (type.equals("사각형")) button.setSelected(true);
        }
        return boardPanel;
    }

}
