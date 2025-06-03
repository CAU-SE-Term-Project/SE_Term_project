package YutGame.view.javafx;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.*;

public class PieceViewFX {
    private final int id;
    private final int ownerId;
    private int positionId = -1;  // -1이면 윷 던지기 전 대기 상태
    private final Image image;

    public PieceViewFX(int id, int ownerId, Image image) {
        this.id = id;
        this.ownerId = ownerId;
        this.image = image;
    }

    public void setPosition(int posId) {
        this.positionId = posId;
    }

    public int getPosition() {
        return positionId;
    }

    public int getId() {
        return id;
    }

    public Image getImage() {
        return image;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public static class Manager {
        private final Map<Integer, PieceViewFX> pieceViews = new HashMap<>();
        private final Map<Integer, Point2D> positionMap;
        private final int numPieces;

        public Manager(Map<Integer, Point2D> positionMap, int numPieces) {
            this.positionMap = positionMap;
            this.numPieces = numPieces;
        }

        public PieceViewFX getPieceView(int id) {
            return pieceViews.get(id);
        }

        public void addPiece(PieceViewFX view) {
            pieceViews.put(view.getId(), view);
        }

        public void updatePiecePosition(int pieceId, int posId) {
            PieceViewFX v = pieceViews.get(pieceId);
            if (v != null) v.setPosition(posId);
        }

        //javafx에서는 GraphicsContext를 사용
        public void drawAll(GraphicsContext gc) {
            Map<Integer, List<PieceViewFX>> grouped = new HashMap<>();
            List<PieceViewFX> waiting = new ArrayList<>();

            for (PieceViewFX v : pieceViews.values()) {
                int pos = v.getPosition();
                if (pos <= 0) {
                    waiting.add(v);
                } else {
                    grouped.computeIfAbsent(pos, k -> new ArrayList<>()).add(v);
                }
            }

            // 1. 보드 상 말들 위치별로 정렬
            for (Map.Entry<Integer, List<PieceViewFX>> entry : grouped.entrySet()) {
                Point2D base = positionMap.get(entry.getKey());
                if (base == null) continue;

                List<PieceViewFX> group = entry.getValue();
                double totalWidth = group.size() * 50;
                double startX = base.getX() - totalWidth / 2;

                //javafx는 정밀한 처리를 요하기 때문에 double 기반 렌더링 처리
                for (int i = 0; i < group.size(); i++) {
                    PieceViewFX v = group.get(i);
                    double x = startX + i * 50;
                    double y = base.getY();
                    drawPiece(gc, v, x, y);
                }
            }

            // 2. 대기 중인 말들 (화면 오른쪽 상단), canvas 크기 때문에 x크기 540으로 변경
            double baseX = 540;
            double baseY = 50;
            for (int i = 0; i < waiting.size(); i++) {
                PieceViewFX v = waiting.get(i);
                double x = baseX;
                double y = baseY + i * 55;
                drawPiece(gc, v, x, y);
            }
        }

        private void drawPiece(GraphicsContext gc, PieceViewFX v, double x, double y) {
            gc.drawImage(v.getImage(), x, y - 24, 48, 48);

            String label = "[P" + v.getOwnerId() + "-말" + ((v.getId() - 1) % numPieces + 1) + "]";
            gc.setFont(Font.font("맑은 고딕", 12));
            gc.setFill(Color.BLACK);
            gc.fillText(label, x, y + 36);
        }
    }
}
