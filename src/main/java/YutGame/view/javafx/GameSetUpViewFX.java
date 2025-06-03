package YutGame.view.javafx;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameSetUpViewFX {

    private final ToggleGroup playersGroup = new ToggleGroup();
    private final ToggleGroup piecesGroup = new ToggleGroup();
    private final ToggleGroup boardGroup = new ToggleGroup();
    private final Button startBtn = new Button("시작하기");

    private final Stage stage;

    //javafx는 jframe 대신 stage 사용
    public GameSetUpViewFX(Stage stage) {
        this.stage = stage;
        stage.setTitle("윷놀이 설정");
        stage.setScene(createScene());
        stage.setWidth(400);
        stage.setHeight(350);
        stage.centerOnScreen();
    }

    //화면의 전체 구조를 담당하는 scene
    private Scene createScene() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));
        root.getChildren().addAll(playersPanel(), piecesPanel(), boardPanel(), startBtn);
        return new Scene(root);
    }

    //각 box는 jpanel에 대응
    private VBox playersPanel() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        Label label = new Label("플레이어 수");

        HBox radioBox = new HBox(10);
        for (int i = 2; i <= 4; i++) {
            RadioButton rb = new RadioButton(i + "명");
            rb.setToggleGroup(playersGroup);
            rb.setUserData(i);
            if (i == 2) rb.setSelected(true);
            radioBox.getChildren().add(rb);
        }
        box.getChildren().addAll(label, radioBox);
        return box;
    }

    private VBox piecesPanel() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        Label label = new Label("말 개수");

        HBox radioBox = new HBox(10);
        for (int i = 2; i <= 5; i++) {
            RadioButton rb = new RadioButton(i + "개");
            rb.setToggleGroup(piecesGroup);
            rb.setUserData(i);
            if (i == 2) rb.setSelected(true);
            radioBox.getChildren().add(rb);
        }
        box.getChildren().addAll(label, radioBox);
        return box;
    }

    private VBox boardPanel() {
        VBox box = new VBox(5);
        box.setPadding(new Insets(10));
        box.setStyle("-fx-border-color: gray; -fx-border-width: 1;");
        Label label = new Label("윷판 형태");

        HBox radioBox = new HBox(10);
        String[] types = {"사각형", "오각형", "육각형"};
        for (String type : types) {
            RadioButton rb = new RadioButton(type);
            rb.setToggleGroup(boardGroup);
            rb.setUserData(type);
            if (type.equals("사각형")) rb.setSelected(true);
            radioBox.getChildren().add(rb);
        }
        box.getChildren().addAll(label, radioBox);
        return box;
    }

    /* ── getter ───────────────────── */
    public Button getStartButton() {
        return startBtn;
    }

    public int getSelectedNumPlayers() {
        RadioButton selected = (RadioButton) playersGroup.getSelectedToggle();
        return selected == null ? 2 : (int) selected.getUserData();
    }

    public int getSelectedNumPieces() {
        RadioButton selected = (RadioButton) piecesGroup.getSelectedToggle();
        return selected == null ? 2 : (int) selected.getUserData();
    }

    public String getSelectedBoardType() {
        RadioButton selected = (RadioButton) boardGroup.getSelectedToggle();
        return selected == null ? "사각형" : (String) selected.getUserData();
    }

    public void show() {
        stage.show();
    }

    /** javafx에서 추가한 부분 */
    public void closeWindow() {
        stage.close();
    }
}
