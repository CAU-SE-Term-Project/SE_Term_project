import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameSetUp extends JFrame {
    private final ButtonGroup playersGroup = new ButtonGroup();
    private final ButtonGroup piecesGroup = new ButtonGroup();
    private final ButtonGroup boardGroup = new ButtonGroup();

    public GameSetUp() {
        setTitle("게임 설정");
        setSize(400, 350);
        setLayout(new GridLayout(0, 1));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel playerPanel = new JPanel();
        playerPanel.setBorder(BorderFactory.createTitledBorder("사용자 수 선택"));
        for (int i = 2; i <= 4; i++) {
            JRadioButton button = new JRadioButton(i + "명");
            button.setActionCommand(Integer.toString(i));
            playersGroup.add(button);
            playerPanel.add(button);
            if (i == 2) button.setSelected(true);
        }

        JPanel piecesPanel = new JPanel();
        piecesPanel.setBorder(BorderFactory.createTitledBorder("말 개수 선택"));
        for (int i = 2; i <= 5; i++) {
            JRadioButton button = new JRadioButton(i + "개");
            button.setActionCommand(Integer.toString(i));
            piecesGroup.add(button);
            piecesPanel.add(button);
            if (i == 2) button.setSelected(true);
        }

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

        JButton startButton = new JButton("시작하기");
        startButton.addActionListener(e -> launchBoard());

        add(playerPanel);
        add(piecesPanel);
        add(boardPanel);
        add(startButton);
    }

    private void launchBoard() {
        String board = boardGroup.getSelection().getActionCommand();
        SwingUtilities.invokeLater(() -> {
            YutBoardGUI boardFrame = new YutBoardGUI(board);
            boardFrame.setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameSetUp setupFrame = new GameSetUp();
            setupFrame.setVisible(true);
        });
    }
}