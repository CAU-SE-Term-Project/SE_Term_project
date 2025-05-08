package YutGame.view.swing;

import YutGame.controller.GameController;
import YutGame.controller.GameEvent;
import YutGame.controller.GameEventType;
import YutGame.model.MoveOutcome;
import YutGame.model.YutResult;
import YutGame.model.Board;
import YutGame.view.GameView;

import javax.swing.*;
import java.util.List;
import java.util.Map;

public final class SwingGameViewAdapter implements GameView, GameBoardView.UiCallback {

    private final GameBoardView board;
    private final GameController ctl;

    public SwingGameViewAdapter(GameBoardView board, GameController ctl) {
        this.board = board;
        this.ctl = ctl;
        board.setCallback(this);
    }

    @Override
    public void onYutClicked(YutResult r) {
        ctl.rollManualYut(r);
    }

    @Override
    public void onRandomClicked() {
        ctl.rollRandomYut();
    }

    @Override
    public void onPieceClicked(int id) {
        ctl.selectPiece(id);
    }

    @Override
    public void onResultChosen(YutResult r) {
        ctl.chooseResult(r);
    }

    @Override
    public void onGameEvent(GameEvent evt) {
        SwingUtilities.invokeLater(() -> handle(evt));
    }

    private void handle(GameEvent e) {
        Map<String, Object> p = e.getPayload();
        switch (e.getType()) {
            case GAME_STARTED:
                setupPieces(p);
                break;

            case ROLLING_PHASE:
                setYutEnabled(true);
                break;

            case RESULT_PICK_PHASE:
                board.showResultDialog(ctl.getPendingResults());
                break;

            case PIECE_PICK_PHASE:
                setYutEnabled(false);
                board.highlightYut((YutResult) p.get("result"));
                break;

            case YUT_ROLLED:
                board.highlightYut((YutResult) p.get("result"));
                break;

            case PIECE_MOVED:
                // MoveOutcome 기반 처리
                if (p.containsKey("outcome")) {
                    MoveOutcome outcome = (MoveOutcome) p.get("outcome");
                    int dest = outcome.newPosition();
                    // 그룹핑된 내 말들 이동
                    for (int pid : outcome.movedPieceIds()) {
                        board.getBoardPanel().updatePiece(pid, dest);
                    }
                    // 잡힌 말들 대기석(-1)으로 이동
                    for (int cid : outcome.capturedPieceIds()) {
                        board.getBoardPanel().updatePiece(cid, Board.START_POS);
                    }
                } else {
                    // 기존 payload 방식
                    int pieceId = (int) p.get("pieceId");
                    int newPos = (int) p.get("newPos");
                    board.getBoardPanel().updatePiece(pieceId, newPos);
                    @SuppressWarnings("unchecked")
                    List<Integer> captured = (List<Integer>) p.get("captured");
                    if (captured != null) {
                        for (int cid : captured) {
                            board.getBoardPanel().updatePiece(cid, Board.START_POS);
                        }
                    }
                }
                break;

            case TURN_CHANGED:
                board.setCurrentPlayer((int) p.get("currentPlayer"));
                setYutEnabled(true);
                break;

            case GAME_ENDED:
                showWinner((int) p.get("winner"));
                break;

            case ERROR:
                JOptionPane.showMessageDialog(board, p.get("msg"));
                break;

            default:
                break;
        }
    }

    private void setupPieces(Map<String, Object> p) {
        board.initPiecesUi();
        setYutEnabled(true);
        int numPlayers = (int) p.get("numPlayers");
        int numPieces = (int) p.get("numPieces");
        for (int pid = 0; pid < numPlayers; pid++) {
            for (int i = 0; i < numPieces; i++) {
                int pieceId = pid * numPieces + i + 1;
                String resourcePath = switch (pid) {
                    case 0 -> "/YutGame/img/horse_red.png";
                    case 1 -> "/YutGame/img/horse_blue.png";
                    case 2 -> "/YutGame/img/horse_green.png";
                    case 3 -> "/YutGame/img/horse_yellow.png";
                    default -> "/YutGame/img/horse_red.png";
                };
                ImageIcon icon = new ImageIcon(getClass().getResource(resourcePath));
                PieceView view = new PieceView(pieceId, pid + 1, icon);
                view.setPosition(Board.START_POS);
                board.getBoardPanel().getPieceManager().addPiece(view);
            }
        }
    }

    private void setYutEnabled(boolean on) {
        for (JButton b : board.getYutButtons()) {
            b.setEnabled(on);
        }
        board.getRandomButton().setEnabled(on);
    }

    private void showWinner(int id) {
        JOptionPane.showMessageDialog(board, "게임 종료! 우승: Player " + id);
    }
}
