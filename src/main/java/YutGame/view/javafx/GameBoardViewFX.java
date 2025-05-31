package YutGame.view.javafx;

import YutGame.controller.GameController;
import YutGame.model.YutResult;
import YutGame.view.swing.BoardPanel;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class GameBoardViewFX {

    public interface UiCallback {
        void onYutClicked(YutResult r);
        void onRandomClicked();
        void onPieceClicked(int pieceId);
        void onResultChosen(YutResult r);
    }

    private UiCallback cb;
    private BoardPanelFX boardPanelFX;
    private final int numPlayers, numPieces;
    private Button[] yutButtons;
    private Button randomButton;
    private Button[][] pieceButtons;
    private Label[] playerLabels;
    private Label turnLabel;
    private Stage primaryStage;

    public GameBoardViewFX(Stage stage, String boardType, int numPlayers, int numPieces) {
        this.primaryStage = stage;
        this.numPlayers = numPlayers;
        this.numPieces = numPieces;

        BorderPane root = new BorderPane();
        root.setTop(createPlayerInfoPanel());

        this.boardPanelFX = new BoardPanelFX(boardType, numPieces);
        root.setCenter(boardPanelFX);

        root.setRight(createYutControlPanel());

        Scene scene = new Scene(root, 1000, 700); // 크기 조정 가능
        stage.setScene(scene);
        stage.setTitle("윷놀이 판 (" + boardType + ")");
        stage.setResizable(false);
    }

    public void setCallback(UiCallback cb) {
        this.cb = cb;
    }

    private VBox createPlayerInfoPanel() {
        VBox panel = new VBox(10);
        panel.setPadding(new Insets(10));

        turnLabel = new Label("현재 턴: Player 1");
        turnLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16;");
        panel.getChildren().add(turnLabel);

        playerLabels = new Label[numPlayers];
        for (int i = 0; i < numPlayers; i++) {
            Label lbl = new Label("Player " + (i + 1) + " - 남은 말: " + numPieces);
            playerLabels[i] = lbl;
            panel.getChildren().add(lbl);
        }
        return panel;
    }

    private VBox createYutControlPanel() {
        VBox panel = new VBox(15);
        panel.setPadding(new Insets(10));
        panel.setPrefWidth(200);

        TitledPane yutPane = new TitledPane();
        yutPane.setText("윷 던지기");
        FlowPane yutButtonBox = new FlowPane(10, 10);
        String[] kor = {"백도", "도", "개", "걸", "윷", "모"};
        yutButtons = new Button[kor.length];

        for (int i = 0; i < kor.length; i++) {
            int idx = i;
            Button b = new Button(kor[i]);
            b.setPrefSize(60, 30);
            b.setOnAction(e -> {
                if (cb != null) cb.onYutClicked(YutResult.values()[idx]);
            });
            yutButtons[i] = b;
            yutButtonBox.getChildren().add(b);
        }

        randomButton = new Button("랜덤");
        randomButton.setOnAction(e -> {
            if (cb != null) cb.onRandomClicked();
        });
        yutButtonBox.getChildren().add(randomButton);
        yutPane.setContent(yutButtonBox);

        VBox piecePanel = createPiecePanel();

        panel.getChildren().addAll(yutPane, piecePanel);
        return panel;
    }

    private VBox createPiecePanel() {
        VBox grid = new VBox(10);
        pieceButtons = new Button[numPlayers][numPieces];

        for (int p = 0; p < numPlayers; p++) {
            TitledPane row = new TitledPane();
            row.setText("플레이어 " + (p + 1));
            FlowPane pieceRow = new FlowPane(10, 10);

            for (int i = 0; i < numPieces; i++) {
                int pieceId = p * numPieces + i + 1;
                Button btn = new Button("말 " + (i + 1));
                btn.setOnAction(e -> {
                    if (cb != null) cb.onPieceClicked(pieceId);
                });
                pieceButtons[p][i] = btn;
                pieceRow.getChildren().add(btn);
            }
            row.setContent(pieceRow);
            grid.getChildren().add(row);
        }
        return grid;
    }

    public void showResultDialog(List<YutResult> results) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(primaryStage);
        dialog.setTitle("윷 결과 선택");

        FlowPane root = new FlowPane(10, 10);
        root.setPadding(new Insets(10));

        for (YutResult r : results) {
            Button btn = new Button(textKor(r));
            btn.setPrefSize(64, 32);
            btn.setOnAction(e -> {
                if (cb != null) cb.onResultChosen(r);
                dialog.close();
            });
            root.getChildren().add(btn);
        }

        Scene scene = new Scene(root);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    public void highlightYut(YutResult r) {
        for (Button b : yutButtons) {
            boolean isSelected = b.getText().equals(textKor(r));
            b.setStyle(isSelected ? "-fx-background-color: yellow;" : "");
        }
    }

    public void setCurrentPlayer(int id) {
        turnLabel.setText("현재 턴: Player " + id);
        for (int i = 0; i < playerLabels.length; i++) {
            boolean isTurn = i == (id - 1);
            playerLabels[i].setStyle(isTurn ?
                    "-fx-text-fill: red; -fx-font-weight: bold;" :
                    "-fx-text-fill: black;");
        }
    }

    private String textKor(YutResult r) {
        return switch (r) {
            case BACK_DO -> "백도";
            case DO -> "도";
            case GAE -> "개";
            case GEOL -> "걸";
            case YUT -> "윷";
            case MO -> "모";
        };
    }

    public void initPiecesUi(){
        boardPanelFX.redraw();
    }


    public Button[] getYutButtons() {
        return yutButtons;
    }

    public Button getRandomButton() {
        return randomButton;
    }

    public Button[][] getPieceButtons() {
        return pieceButtons;
    }

    public void show(){primaryStage.show();}

    public BoardPanelFX getBoardPanel() {
        return boardPanelFX;
    }

    public void refreshAllPlayerInfo(GameController controller) {
    for (int i = 0; i < numPlayers; i++) {
        int playerId = i + 1;
        int remaining = controller.getRemainingPieceCount(playerId);  // ← 여기서 호출!
        playerLabels[i].setText("Player " + playerId + " - 남은 말: " + remaining);
    }
}

    
}

