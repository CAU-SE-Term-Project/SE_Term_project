package YutGame.view.javafx;

import YutGame.view.swing.PieceView;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.HashMap;
import java.util.Map;

public class BoardPanelFX extends Pane {

    private final String boardType;
    private final int CELL_SIZE = 25;
    private final int CROSS_SIZE = 40;
    private final Point2D center = new Point2D(300, 300);

    private final Map<Integer, Point2D> posIdToPixel = new HashMap<>();
    private final PieceViewFX.Manager pieceManager;

    private final Canvas canvas;
    private final GraphicsContext gc;

    public BoardPanelFX(String boardType, int numPieces) {
        this.boardType = boardType;
        this.canvas = new Canvas(600, 600); // 크기 충분히 크게 설정
        this.gc = canvas.getGraphicsContext2D();
        this.getChildren().add(canvas);

        this.pieceManager = new PieceViewFX.Manager(posIdToPixel, numPieces);

        drawPolygonBoard();
        redraw();
    }


    public PieceViewFX.Manager getPieceManager() {
        return pieceManager;
    }

    public void updatePiece(int pieceId, int posId) {
        pieceManager.updatePiecePosition(pieceId, posId);
        redraw();
    }

    public void redraw() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        drawPolygonBoard();
        pieceManager.drawAll(gc);
    }


//    public Map<Integer, Point2D> getPosIdToPixel() {
//        return posIdToPixel;
//    }

    private void drawPolygonBoard() {
        //fx 사용으로 인한 필수 코드
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFont(Font.font("맑은 고딕", 12));

        int sides = switch (boardType) {
            case "사각형" -> 4;
            case "오각형" -> 5;
            case "육각형" -> 6;
            default -> 4;
        };

        double angleStep = 2 * Math.PI / sides;
        double angleOffset = Math.PI / 4;
        double radius = 200;
        Point2D[] corners = new Point2D[sides];

        // 1. 외곽 모서리 및 간격 셋업
        for (int i = 0; i < sides; i++) {
            double angle = i * angleStep + angleOffset;
            corners[i] = new Point2D(
                    center.getX() + radius * Math.cos(angle),
                    center.getY() + radius * Math.sin(angle)
            );
        }
        // 외곽 선
        for (int i = 0; i < sides; i++) {
            Point2D a = corners[i];
            Point2D b = corners[(i + 1) % sides];
            gc.strokeLine(a.getX(), a.getY(), b.getX(), b.getY());
        }

        // 2. 시작 지점(-1)
        int startIdx = 0;
        double maxSum = Double.NEGATIVE_INFINITY;
        for (int i = 0; i < sides; i++) {
            double sum = corners[i].getX() + corners[i].getY();
            if (sum > maxSum) {
                maxSum = sum;
                startIdx = i;
            }
        }

        Point2D startPt = corners[startIdx];
        drawCircle(startPt.getX(), startPt.getY(), CROSS_SIZE, -1, Color.YELLOW);
        gc.setFill(Color.BLACK);
        gc.fillText("출발", startPt.getX() - 15, startPt.getY() + 5);
        posIdToPixel.put(-1, startPt);

        // 외곽 셀 (0 ~ 19)
        int posId = 0;
        for (int side = 0; side < sides; side++) {
            int idx = (startIdx - side + sides) % sides;
            Point2D pCorner = corners[idx];
            Point2D pNext = corners[(idx - 1 + sides) % sides];

            // 코너
            drawCircle(pCorner.getX(), pCorner.getY(), CROSS_SIZE, posId, Color.LIGHTBLUE);
            posIdToPixel.put(posId++, pCorner);

            // 외곽 루트 분기점 사이 노드 개수 4개씩
            for (int j = 1; j <= 4; j++) {
                double t = j / 5.0;
                double x = pCorner.getX() + t * (pNext.getX() - pCorner.getX());
                double y = pCorner.getY() + t * (pNext.getY() - pCorner.getY());
                drawCircle(x, y, CELL_SIZE, posId, Color.LIGHTGRAY);
                posIdToPixel.put(posId++, new Point2D(x, y));
            }
        }

        // 각 다각형별 분기 경로
        if (sides == 4) {
            drawInterpolated(posIdToPixel.get(5), center, new int[]{21, 22});
            drawInterpolated(posIdToPixel.get(10), center, new int[]{23, 24});
            drawCircle(center.getX(), center.getY(), CROSS_SIZE, 25, Color.ORANGE);
            posIdToPixel.put(25, center);
            drawInterpolated(center, posIdToPixel.get(15), new int[]{26, 27});
            drawInterpolated(center, posIdToPixel.get(0), new int[]{28, 29});
            posIdToPixel.put(20, posIdToPixel.get(0));
        } else if (sides == 5) {
            drawInterpolated(posIdToPixel.get(5), center, new int[]{26, 27});
            drawInterpolated(posIdToPixel.get(10), center, new int[]{28, 29});
            drawInterpolated(posIdToPixel.get(15), center, new int[]{30, 31});
            drawCircle(center.getX(), center.getY(), CROSS_SIZE, 32, Color.ORANGE);
            posIdToPixel.put(32, center);
            drawInterpolated(center, posIdToPixel.get(20), new int[]{33, 34});
            drawInterpolated(center, posIdToPixel.get(0), new int[]{35, 36});
            posIdToPixel.put(25, posIdToPixel.get(0));
        } else if (sides == 6) {
            drawInterpolated(posIdToPixel.get(5), center, new int[]{31, 32});
            drawInterpolated(posIdToPixel.get(10), center, new int[]{33, 34});
            drawInterpolated(posIdToPixel.get(15), center, new int[]{35, 36});
            drawInterpolated(posIdToPixel.get(20), center, new int[]{37, 38});
            drawCircle(center.getX(), center.getY(), CROSS_SIZE, 39, Color.ORANGE);
            posIdToPixel.put(39, center);
            drawInterpolated(center, posIdToPixel.get(25), new int[]{40, 41});
            drawInterpolated(center, posIdToPixel.get(0), new int[]{42, 43});
            posIdToPixel.put(30, posIdToPixel.get(0));
        }
    }

    private void drawInterpolated(Point2D from, Point2D to, int[] ids) {
        if (from == null || to == null) return;
        double dx = to.getX() - from.getX();
        double dy = to.getY() - from.getY();

        for (int i = 0; i < ids.length; i++) {
            double t = (i + 1) / (double) (ids.length + 1);
            double x = from.getX() + dx * t;
            double y = from.getY() + dy * t;

            drawCircle(x, y, CELL_SIZE, ids[i], Color.LIGHTGRAY);
            posIdToPixel.put(ids[i], new Point2D(x, y));
        }

        gc.setStroke(Color.BLACK);
        gc.strokeLine(from.getX(), from.getY(), to.getX(), to.getY());
    }

    private void drawCircle(double x, double y, double size, Integer id, Color fillColor) {
        gc.setFill(fillColor);
        gc.fillOval(x - size / 2, y - size / 2, size, size);
        gc.setStroke(Color.BLUE);
        gc.strokeOval(x - size / 2, y - size / 2, size, size);

        if (id != null) {
            gc.setFill(Color.BLACK);
            gc.fillText(id.toString(), x - 6, y + 5);
        }
    }
}
