package YutGame.app;

import YutGame.ui.GameSetUpController;
import YutGame.view.javafx.GameSetUpViewFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class YutGameFXLauncher extends Application {
    @Override
    public void start(Stage primaryStage){
        GameSetUpViewFX view = new GameSetUpViewFX();
        // GameSetUpFXController 구현 예정
        // new GameSetUpController(view);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
