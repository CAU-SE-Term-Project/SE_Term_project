package YutGame.app;

import YutGame.ui.GameLauncher;
import YutGame.ui.GameLauncherFX;
import YutGame.view.javafx.GameSetUpViewFX;
import YutGame.view.swing.GameSetUpView;
import javafx.application.Application;
import javafx.stage.Stage;

import javax.swing.*;

public class YutGameLauncherApp extends Application {

    private static UIMode mode = UIMode.JAVAFX;

    @Override
    public void start(Stage primaryStage){
        if (mode != UIMode.JAVAFX) return;
        GameSetUpViewFX view = new GameSetUpViewFX(primaryStage);
        new GameLauncherFX(view);
        view.show();
    }

    public static void startSwingApp(){
        SwingUtilities.invokeLater(()->{
            GameSetUpView view=new GameSetUpView();
            new GameLauncher(view);
            view.setVisible(true);
        });
    }


    public static void main(String[] args) {
        // 커맨드라인 인자에서 모드 파싱
        if (args.length > 0) {
            mode = UIMode.from(args[0]);
        }

        // 실행 분기
        if (mode == UIMode.SWING) {
            startSwingApp();
        } else {
            launch(args); // JavaFX 실행
        }
    }
}


//TO-DO : 1) GameSetUpControllerFx 구현 중 -> swinggameviewadapter 재사용하기
//        2) GameBoardViewFx.createBoardPanelPlaceholder() -> BoardPanelFX로 만들고 코드 수정하기