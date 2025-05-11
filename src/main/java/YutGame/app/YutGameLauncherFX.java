package YutGame.app;

import YutGame.ui.GameSetUpControllerFX;
import YutGame.view.javafx.GameSetUpViewFX;
import javafx.application.Application;
import javafx.stage.Stage;

public class YutGameLauncherFX extends Application {
    @Override
    public void start(Stage primaryStage){
        GameSetUpViewFX view = new GameSetUpViewFX(primaryStage);
        new GameSetUpControllerFX(view);
        view.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


//TO-DO : 1) GameSetUpControllerFx 구현 중 -> swinggameviewadapter 재사용하기
//        2) GameBoardViewFx.createBoardPanelPlaceholder() -> BoardPanelFX로 만들고 코드 수정하기