package YutGame.view.javafx;

import YutGame.ui.GameSetUpControllerFX;
import YutGame.view.javafx.GameSetUpViewFX;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameCompleteViewFX {
    private final Button btnRestart = new Button("게임 재시작");
    private final Button btnOver = new Button("게임 종료");

    public GameCompleteViewFX(int winnerId) {
        Stage stage = new Stage();
        stage.setTitle("Game Complete");

        Label winLabel = new Label("플레이어 " + winnerId + " 승리!");
        winLabel.setFont(new Font("맑은 고딕", 20));
        winLabel.setTextFill(Color.RED);

        // 색상 번쩍이는 효과
        Timeline colorFlasher = new Timeline(new KeyFrame(Duration.millis(300), e -> {
            Color current = (Color) winLabel.getTextFill();
            winLabel.setTextFill(current.equals(Color.RED) ? Color.BLUE : Color.RED);
        }));
        colorFlasher.setCycleCount(Timeline.INDEFINITE);
        colorFlasher.play();

        HBox buttonBox = new HBox(10, btnRestart, btnOver);
        buttonBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(30, winLabel, buttonBox);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 300, 200);
        stage.setScene(scene);
        stage.show();

        // ✅ JavaFX 앱 내에서 재시작
        btnRestart.setOnAction(e -> {
            stage.close();
            Stage newStage = new Stage();
            GameSetUpViewFX newView = new GameSetUpViewFX(newStage);
            new GameSetUpControllerFX(newView);
            newView.show();  // 새 게임 설정 화면 띄우기
        });

        btnOver.setOnAction(e -> Platform.exit());
    }
}
