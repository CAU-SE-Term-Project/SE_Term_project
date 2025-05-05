import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class YutBoardGUI extends JFrame {
    private final String boardType;
    private final int numPlayers;
    private final int numPieces;

    public YutBoardGUI(String boardType, int numPlayers, int numPieces) {
        this.boardType = boardType;
        this.numPlayers = numPlayers;
        this.numPieces = numPieces;
        setTitle("윷놀이 판 (" + boardType + ")");
        setSize(600, 850);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        add(createPlayerInfoPanel(), BorderLayout.NORTH);
        add(new BoardPanel(), BorderLayout.CENTER);
        add(createControlPanel(), BorderLayout.SOUTH);
    }

    private JPanel createPlayerInfoPanel() {
        JPanel panel = new JPanel(new GridLayout(numPlayers, 1));
        for (int i = 1; i <= numPlayers; i++) {
            JLabel label = new JLabel("Player " + i + " - 남은 말: " + numPieces);
            panel.add(label);
        }
        return panel;
    }

    private JPanel createControlPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel yutPanel = new JPanel();
        yutPanel.setBorder(BorderFactory.createTitledBorder("윷 던지기"));
        String[] results = {"도", "개", "걸", "윷", "모"};
        for (String result : results) {
            JButton btn = new JButton(result);
            btn.addActionListener(e -> handleYutResult(result));
            yutPanel.add(btn);
        }

        JButton randomBtn = new JButton("Random");
        randomBtn.addActionListener(e -> {
            String[] randResults = {"도", "개", "걸", "윷", "모"};
            int idx = (int) (Math.random() * randResults.length);
            handleYutResult(randResults[idx]);
        });
        yutPanel.add(randomBtn);

        panel.add(yutPanel, BorderLayout.NORTH);
        panel.add(createPieceSelectionPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createPieceSelectionPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(numPlayers, 1));  // 플레이어 수만큼 행

        for (int player = 1; player <= numPlayers; player++) {
            JPanel playerPanel = new JPanel();
            playerPanel.setBorder(BorderFactory.createTitledBorder("플레이어 " + player));

            for (int piece = 1; piece <= numPieces; piece++) {
                final int currentPlayer = player;
                final int currentPiece = piece;
                JButton btn = new JButton("말 " + currentPiece);
                btn.addActionListener(e -> handlePieceSelection(currentPlayer, currentPiece));
                playerPanel.add(btn);
            }

            panel.add(playerPanel);
        }

        return panel;
    }

    private void handleYutResult(String result) {
        JOptionPane.showMessageDialog(this, "선택된 윷 결과: " + result);
        // TODO: 게임 진행 로직과 연결 필요
    }

    private void handlePieceSelection(int playerNumber, int pieceNumber) {
        JOptionPane.showMessageDialog(this, "선택된 플레이어: " + playerNumber + ", 말: " + pieceNumber);
        // TODO: 실제 말 이동 로직과 연결
    }

    class BoardPanel extends JPanel {
        private final int CELL_SIZE = 25;
        private final int CROSS_SIZE = 40;
        private final Point center = new Point(300, 300);

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setStroke(new BasicStroke(2));
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int sides = switch (boardType) {
                case "사각형" -> 4;
                case "오각형" -> 5;
                case "육각형" -> 6;
                default -> 4;
            };

            drawPolygonBoard(g2d, sides);
        }



        private void drawPolygonBoard(Graphics2D g, int sides) {
            double angleStep = 2 * Math.PI / sides;
            double angleOffset;
            if (sides == 4) angleOffset = -Math.PI / 4;      // 45도 회전 보정
            else if (sides == 5) angleOffset = -Math.PI / 10; // 36도 회전 보정
            else if (sides == 6) angleOffset = 0;             // 0도 (꼭짓점이 위로)
            else angleOffset = -Math.PI / 2;
            int radius = 200;
            Point2D[] corners = new Point2D[sides];

            for (int i = 0; i < sides; i++) {
                double angle = i * angleStep + angleOffset;
                double x = center.x + radius * Math.cos(angle);
                double y = center.y + radius * Math.sin(angle);
                corners[i] = new Point2D.Double(x, y);
            }

            for (int i = 0; i < sides; i++) {
                Point2D start = corners[i];
                Point2D end = corners[(i + 1) % sides];
                g.drawLine((int) start.getX(), (int) start.getY(), (int) end.getX(), (int) end.getY());
            }

            for (Point2D corner : corners) {
                g.drawLine(center.x, center.y, (int) corner.getX(), (int) corner.getY());

                // 꼭짓점 원
                drawCircle(g, (int) corner.getX(), (int) corner.getY(), CROSS_SIZE);

                // 중간 원 2개 (1/3, 2/3 위치)
                double dx = corner.getX() - center.x;
                double dy = corner.getY() - center.y;

                for (double r : new double[]{1.0 / 3, 2.0 / 3}) {
                    int cx = (int) (center.x + dx * r);
                    int cy = (int) (center.y + dy * r);
                    drawCircle(g, cx, cy, CELL_SIZE);
                }
            }

            for (int i = 0; i < sides; i++) {
                Point2D start = corners[i];
                Point2D end = corners[(i + 1) % sides];

                for (int j = 1; j <= 3; j++) {
                    double ratio = j / 4.0;
                    double x = start.getX() + ratio * (end.getX() - start.getX());
                    double y = start.getY() + ratio * (end.getY() - start.getY());
                    drawCircle(g, (int) x, (int) y, CELL_SIZE);
                }
            }

            drawCircle(g, center.x, center.y, CROSS_SIZE);

            for (Point2D corner : corners) {
                drawCircle(g, (int) corner.getX(), (int) corner.getY(), CROSS_SIZE);
            }

            g.setFont(new Font("맑은 고딕", Font.BOLD, 15));
            // 출발 위치를 항상 오른쪽 아래(5시 방향)로 잡기: x+y가 가장 큰 꼭짓점 찾기
            int startIdx = 0;
            double maxXY = corners[0].getX() + corners[0].getY();
            for (int i = 1; i < corners.length; i++) {
                double sum = corners[i].getX() + corners[i].getY();
                if (sum > maxXY) {
                    maxXY = sum;
                    startIdx = i;
                }
            }
            // 출발 원의 좌표
            int sx = (int) corners[startIdx].getX();
            int sy = (int) corners[startIdx].getY();

            // 노란색으로 채우기
            g.setColor(Color.YELLOW);
            g.fillOval(sx - CROSS_SIZE / 2, sy - CROSS_SIZE / 2, CROSS_SIZE, CROSS_SIZE);

            // 테두리 검은색 원 다시 그리기
            g.setColor(Color.BLACK);
            g.drawOval(sx - CROSS_SIZE / 2, sy - CROSS_SIZE / 2, CROSS_SIZE, CROSS_SIZE);

            // 출발 텍스트 표시
            g.setFont(new Font("맑은 고딕", Font.BOLD, 15));
            g.drawString("출발", sx - 15, sy + 5);
        }

        private void drawCircle(Graphics2D g, int x, int y, int size) {
            g.drawOval(x - size / 2, y - size / 2, size, size);
        }
    }



}