package YutGame.view.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameCompleteView extends JFrame {
    private final JButton btnRestart = new JButton("게임 재시작");
    private final JButton btnOver = new JButton("게임 종료");
    public GameCompleteView(int winnerId) {
        super("Game Complete");
        setSize(300, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));

        JPanel topPanel = new JPanel();
        JLabel winLabel = new JLabel("플레이어 " + winnerId + " 승리!");
        winLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        topPanel.add(winLabel);

// 색상 번쩍이는 타이머 효과
        Timer timer = new Timer(300, new ActionListener() {
            private boolean toggle = false;
            public void actionPerformed(ActionEvent e) {
                winLabel.setForeground(toggle ? Color.RED : Color.BLUE);
                toggle = !toggle;
            }
        });
        timer.start();

        JPanel bottomPanel = new JPanel(new GridLayout(1,2));
        bottomPanel.add(btnRestart);
        bottomPanel.add(btnOver);

        mainPanel.add(topPanel);
        mainPanel.add(bottomPanel);

        add(mainPanel);

        btnRestart.addActionListener(e -> {
            dispose();
            YutGameSwingView.startGameSwingApp();
        });
        btnOver.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        new GameCompleteView(1).setVisible(true);
    }
}
