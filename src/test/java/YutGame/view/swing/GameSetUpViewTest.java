package YutGame.view.swing;

import YutGame.view.javafx.GameSetUpViewFX;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

public class GameSetUpViewTest {

    //startButton null 아닌지, "시작하기"로 잘 구성 되었는지 Test(Swing)
    @Test
    void testComponentInitialization() {
        GameSetUpView view = new GameSetUpView();
        assertNotNull(view.getStartButton());
        assertEquals("시작하기", view.getStartButton().getText());
    }

    //startbutton eventlistener test(Swing)
    @Test
    void testSwingStartButtonActionListener() {
        GameSetUpView view = new GameSetUpView();
        AtomicBoolean clicked = new AtomicBoolean(false);
        view.getStartButton().addActionListener(e -> clicked.set(true));
        for (ActionListener l : view.getStartButton().getActionListeners()) {
            l.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, ""));
        }
        assertTrue(clicked.get());
    }

    // 기본값 2, 2, 사각형으로 설정되어 있는지 확인(Swing)
    @Test
    void testSwingDefaultSelections() {
        GameSetUpView view = new GameSetUpView();

        assertEquals(2, view.getSelectedNumPlayers());
        assertEquals(2, view.getSelectedNumPieces());
        assertEquals("사각형", view.getSelectedBoardType());
    }


    @BeforeAll
    static void initJavaFX() {
        new JFXPanel();
    }

    //startButton null 아닌지, "시작하기"로 잘 구성 되었는지 Test(JavaFx)
    @Test
    void testFXStartButtonInitialization() throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            GameSetUpViewFX view = new GameSetUpViewFX(stage);
            Button startButton = view.getStartButton();
            assertNotNull(startButton);
            assertEquals("시작하기", startButton.getText());
        });

        // 짧은 대기 시간 필요 -> 비동기 방식이여서 runLater()가 실행될때까지 기다려주기
        Thread.sleep(500);
    }

    // 기본값 2, 2, 사각형으로 설정되어 있는지 확인(JavaFx)
    @Test
    void testFXDefaultSelections() throws Exception {
        Platform.runLater(() -> {
            Stage stage = new Stage();
            GameSetUpViewFX view = new GameSetUpViewFX(stage);

            assertEquals(2, view.getSelectedNumPlayers());
            assertEquals(2, view.getSelectedNumPieces());
            assertEquals("사각형", view.getSelectedBoardType());
        });

        Thread.sleep(500); // Platform.runLater 대기
    }




}
