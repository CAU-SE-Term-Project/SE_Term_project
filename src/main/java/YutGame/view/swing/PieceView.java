// PieceView.java
package YutGame.view.swing;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

public class PieceView {
    private final int id;
    private final int ownerId;
    private int positionId = -1;  // -1이면 윷 던지기 전 대기 상태
    private final Image image;

    public PieceView(int id, int ownerId, ImageIcon icon) {
        this.id = id;
        this.ownerId = ownerId;
        this.image = icon.getImage();
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
        private final Map<Integer, PieceView> pieceViews = new HashMap<>();
        private final Map<Integer, Point> positionMap;
        private final int numPieces;

        public Manager(Map<Integer, Point> positionMap, int numPieces) {
            this.positionMap = positionMap;
            this.numPieces = numPieces;
        }

        public void addPiece(PieceView view) {
            pieceViews.put(view.getId(), view);
        }

        public void updatePiecePosition(int pieceId, int posId) {
            PieceView v = pieceViews.get(pieceId);
            if (v != null) v.setPosition(posId);
        }

        public void drawAll(Graphics g) {
            Map<Integer, java.util.List<PieceView>> grouped = new HashMap<>();
            java.util.List<PieceView> waiting = new ArrayList<>();

            for (PieceView v : pieceViews.values()) {
                int pos = v.getPosition();
                if (pos <= 0) {
                    waiting.add(v);  // 아직 윷 던지기 전 말
                } else {
                    grouped.computeIfAbsent(pos, k -> new ArrayList<>()).add(v);
                }
            }

            // 1. 말들을 위치별로 그림 (posId 기준)
            for (Map.Entry<Integer, java.util.List<PieceView>> entry : grouped.entrySet()) {
                Point base = positionMap.get(entry.getKey());
                if (base == null) continue;

                java.util.List<PieceView> group = entry.getValue();
                int totalWidth = group.size() * 50;
                int startX = base.x - totalWidth / 2;

                for (int i = 0; i < group.size(); i++) {
                    PieceView v = group.get(i);
                    int x = startX + i * 50;
                    int y = base.y;
                    drawPiece(g, v, x, y);
                }
            }

            // 2. 출발 전 대기 말은 화면 우측 상단 대기공간에 나열
            int baseX = 600;
            int baseY = 50;
            for (int i = 0; i < waiting.size(); i++) {
                PieceView v = waiting.get(i);
                int x = baseX;
                int y = baseY + i * 55;
                drawPiece(g, v, x, y);
            }
        }

        private void drawPiece(Graphics g, PieceView v, int x, int y) {
            g.drawImage(v.getImage(), x, y - 24, 48, 48, null);
            String label = "[P" + v.getOwnerId() + "-말" + ((v.getId() - 1) % numPieces + 1) + "]";
            g.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
            g.setColor(Color.BLACK);
            g.drawString(label, x , y + 36);
        }
    }
}
