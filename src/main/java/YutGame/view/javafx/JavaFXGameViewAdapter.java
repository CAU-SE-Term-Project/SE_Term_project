package YutGame.view.javafx;

import YutGame.controller.GameController;
import YutGame.controller.GameEvent;
import YutGame.model.MoveOutcome;
import YutGame.model.YutResult;
import YutGame.model.Board;
import YutGame.view.GameView;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;

import java.util.List;
import java.util.Map;

public class JavaFXGameViewAdapter implements GameView, GameBoardViewFX.UiCallback {

    private final GameBoardViewFX board;
    private final GameController ctl;

    public JavaFXGameViewAdapter(GameBoardViewFX board, GameController ctl) {
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
        int ownerId = board.getBoardPanel()
                .getPieceManager()
                .getPieceView(id)
                .getOwnerId();

        int currentPlayerId = ctl.getCurrentPlayerId();

        if (ownerId != currentPlayerId) {
            System.out.println("❌ 상대 플레이어의 말 클릭 - 무시됨");
            return;
        }

        ctl.selectPiece(id);
    }

    @Override
    public void onResultChosen(YutResult r) {
        ctl.chooseResult(r);
    }

    @Override
    public void onGameEvent(GameEvent evt) {
        Platform.runLater(() -> handle(evt));
    }

    private void handle(GameEvent e) {
        Map<String, Object> p = e.getPayload();

        switch (e.getType()) {
            case GAME_STARTED -> setupPieces(p);

            case ROLLING_PHASE -> setYutEnabled(true);

            case RESULT_PICK_PHASE -> board.showResultDialog(ctl.getPendingResults());

            case PIECE_PICK_PHASE -> {
                setYutEnabled(false);
                board.highlightYut((YutResult) p.get("result"));
            }

            case YUT_ROLLED -> board.highlightYut((YutResult) p.get("result"));

            case PIECE_MOVED -> {
                if (p.containsKey("outcome")) {
                    MoveOutcome outcome = (MoveOutcome) p.get("outcome");
                    int dest = outcome.newPosition();
                    for (int pid : outcome.movedPieceIds()) {
                        board.getBoardPanel().updatePiece(pid, dest);
                    }
                    for (int cid : outcome.capturedPieceIds()) {
                        board.getBoardPanel().updatePiece(cid, Board.START_POS);
                    }
                } else {
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

                // 말 이동 후 플레이어 정보 새로고침
                board.refreshAllPlayerInfo(ctl);    
            }

            case TURN_CHANGED -> {
                board.setCurrentPlayer((int) p.get("currentPlayer"));
                setYutEnabled(true);
            }

            case GAME_ENDED -> {
                int winner = (int) p.get("winner");
                new GameCompleteViewFX(winner);
            }

            case ERROR -> {
                Alert err = new Alert(Alert.AlertType.ERROR,
                        (String) p.get("msg"), ButtonType.OK);
                err.setTitle("오류");
                err.showAndWait();
            }

            default -> {}
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
                String path = switch (pid) {
                    case 0 -> "/YutGame/img/horse_red.png";
                    case 1 -> "/YutGame/img/horse_blue.png";
                    case 2 -> "/YutGame/img/horse_green.png";
                    case 3 -> "/YutGame/img/horse_yellow.png";
                    default -> "/YutGame/img/horse_red.png";
                };

                Image img = new Image(getClass().getResourceAsStream(path));
                PieceViewFX piece = new PieceViewFX(pieceId, pid + 1, img);
                piece.setPosition(Board.START_POS);
                board.getBoardPanel().getPieceManager().addPiece(piece);
            }
        }

        board.getBoardPanel().redraw(); // drawAll 호출 위함
    }

    private void setYutEnabled(boolean on) {
        for (var b : board.getYutButtons()) b.setDisable(!on);
        board.getRandomButton().setDisable(!on);
    }
}
