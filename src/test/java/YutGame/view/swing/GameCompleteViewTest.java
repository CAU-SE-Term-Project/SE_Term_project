package YutGame.view.swing;

import YutGame.view.javafx.GameCompleteViewFX;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.control.Button;
import org.junit.jupiter.api.*;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class GameCompleteViewTest {

    private GameCompleteView view;
    private GameCompleteViewFX FXview;

    //테스트 위한 더미 view 생성(Swing)
    @BeforeEach
    void setUp() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            view = new GameCompleteView(1);
            view.setVisible(true);  // 필요 시 창을 띄움
        });
    }

    @AfterEach
    void tearDown() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            if (view != null) view.dispose();
        });
    }

    // 생성된 버튼이 null이 아닌지, 각 버튼이 게임 재시작, 게임 종료를 표시하는지 테스트(Swing)
    @Test
    void testButtonsAreCreated() throws Exception {
        SwingUtilities.invokeAndWait(() -> {
            JButton btnRestart = view.getBtnRestart();
            JButton btnOver = view.getBtnOver();

            assertNotNull(btnRestart, "재시작 버튼이 null입니다.");
            assertNotNull(btnOver, "종료 버튼이 null입니다.");

            assertEquals("게임 재시작", btnRestart.getText(), "재시작 버튼의 텍스트가 올바르지 않음.");
            assertEquals("게임 종료", btnOver.getText(), "종료 버튼의 텍스트가 올바르지 않음.");
        });
    }

    //javaFX test setting
    @BeforeAll
    static void initToolkit() {
        // JavaFX 환경 초기화 (필수)
        new JFXPanel();
    }

    @BeforeEach
    void FXsetUp() throws Exception {
        Platform.runLater(() -> {
            FXview = new GameCompleteViewFX(1);  // 테스트용 인스턴스 생성
        });
        Thread.sleep(200); // Platform.runLater가 처리되도록 약간의 대기
    }

    // 생성된 버튼이 null이 아닌지, 각 버튼이 게임 재시작, 게임 종료를 표시하는지 테스트(javaFX)
    @Test
    void testButtonsAreCreatedAndLabeledCorrectly() throws Exception {
        Platform.runLater(() -> {
            Button btnRestart = FXview.getBtnRestart();
            Button btnOver = FXview.getBtnOver();

            assertNotNull(btnRestart, "재시작 버튼이 null입니다.");
            assertNotNull(btnOver, "종료 버튼이 null입니다.");

            assertEquals("게임 재시작", btnRestart.getText(), "재시작 버튼 텍스트가 올바르지 않음.");
            assertEquals("게임 종료", btnOver.getText(), "종료 버튼 텍스트가 올바르지 않음.");
        });
        Thread.sleep(200); // runLater 처리 대기
    }
}
