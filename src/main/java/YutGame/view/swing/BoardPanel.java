package YutGame.view.swing;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

public class BoardPanel extends JPanel {
    private final String boardType;
    private final int CELL_SIZE = 25;
    private final int CROSS_SIZE = 40;
    private final Point center = new Point(300, 300);

    private final Map<Integer, Point> posIdToPixel = new HashMap<>();
    private final PieceView.Manager pieceManager;

    public BoardPanel(String boardType, int numPieces) {
        this.boardType = boardType;
        this.pieceManager = new PieceView.Manager(posIdToPixel, numPieces);
    }

    public PieceView.Manager getPieceManager() {
        return pieceManager;
    }

    public void updatePiece(int pieceId, int posId) {
        pieceManager.updatePiecePosition(pieceId, posId);
        repaint();
    }

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
        pieceManager.drawAll(g);
    }

    private void drawPolygonBoard(Graphics2D g, int sides) {
        double angleStep = 2 * Math.PI / sides;
        double angleOffset = Math.PI / 4;
        int radius = 200;
        Point2D[] corners = new Point2D[sides];

        // 1. 외곽 모서리 및 간격 셋업
        for (int i = 0; i < sides; i++) {
            double angle = i * angleStep + angleOffset;
            corners[i] = new Point2D.Double(
                    center.x + radius * Math.cos(angle),
                    center.y + radius * Math.sin(angle)
            );
        }
        // 외곽 선
        for (int i = 0; i < sides; i++) {
            Point2D a = corners[i];
            Point2D b = corners[(i + 1) % sides];
            g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
        }

        // 2. 시작 지점(-1)
        int startIdx = 0;
        double maxSum = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < sides; i++) {
            double sum = corners[i].getX() + corners[i].getY();
            if (sum > maxSum) { maxSum = sum; startIdx = i; }
        }
        Point startPt = new Point(
                (int)corners[startIdx].getX(), (int)corners[startIdx].getY()
        );
        drawCircle(g, startPt.x, startPt.y, CROSS_SIZE, -1);
        posIdToPixel.put(-1, startPt);
        g.setColor(Color.YELLOW);
        g.fillOval(startPt.x - CROSS_SIZE/2, startPt.y - CROSS_SIZE/2, CROSS_SIZE, CROSS_SIZE);
        g.setColor(Color.BLACK);
        g.drawOval(startPt.x - CROSS_SIZE/2, startPt.y - CROSS_SIZE/2, CROSS_SIZE, CROSS_SIZE);
        g.setFont(new Font("맑은 고딕", Font.BOLD, 15));
        g.drawString("출발", startPt.x -15, startPt.y +5);

        // 3. 외곽 셀(0..19)
        int posId = 0;
        for (int side = 0; side < sides; side++) {
            int idx = (startIdx - side + sides) % sides;
            Point2D pCorner = corners[idx];
            Point2D pNext = corners[(idx -1 + sides)%sides];

            // 코너
            drawCircle(g, (int)pCorner.getX(), (int)pCorner.getY(), CROSS_SIZE, posId);
            posIdToPixel.put(posId++, new Point((int)pCorner.getX(), (int)pCorner.getY()));

            // 중간 지점 4개
            for (int j = 1; j <= 4; j++) {
                double t = j / 5.0;
                int x = (int)(pCorner.getX() + t*(pNext.getX()-pCorner.getX()));
                int y = (int)(pCorner.getY() + t*(pNext.getY()-pCorner.getY()));
                drawCircle(g, x, y, CELL_SIZE, posId);
                posIdToPixel.put(posId++, new Point(x,y));
            }
        }
        // 0~19 매핑 완료

        // 4. 분기 1 (5->21->22->25)
        drawInterpolated(g, posIdToPixel.get(5), center, new int[]{21,22});
        // 5. 분기 2 (10->23->24->25)
        drawInterpolated(g, posIdToPixel.get(10), center, new int[]{23,24});

        // 6. 중앙(25)
        drawCircle(g, center.x, center.y, CROSS_SIZE, 25);
        posIdToPixel.put(25, new Point(center));

        // 7. 중앙 분기 후
        // branch1: 25->26->27->15
        drawInterpolated(g, center, posIdToPixel.get(15), new int[]{26,27});
        // branch2: 25->28->29->0
        drawInterpolated(g, center, posIdToPixel.get(0), new int[]{28,29});
        // id20은 0과 동일 좌표
        posIdToPixel.put(20, posIdToPixel.get(0));
    }

    private void drawInterpolated(Graphics2D g, Point from, Point to, int[] ids) {
        if (from == null || to == null) return;
        double dx = to.x - from.x;
        double dy = to.y - from.y;
        for (int i = 0; i < ids.length; i++) {
            double t = (i+1)/(double)(ids.length+1);
            int x = (int)(from.x + dx*t);
            int y = (int)(from.y + dy*t);
            drawCircle(g, x, y, CELL_SIZE, ids[i]);
            posIdToPixel.put(ids[i], new Point(x,y));
        }
        g.drawLine(from.x, from.y, to.x, to.y);
    }

    private void drawCircle(Graphics2D g, int x, int y, int size, Integer id) {
        g.setColor(Color.BLUE);
        g.drawOval(x-size/2, y-size/2, size, size);
        if (id != null) {
            g.setColor(Color.BLACK);
            g.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            g.drawString(id.toString(), x-6, y+5);
        }
    }
}