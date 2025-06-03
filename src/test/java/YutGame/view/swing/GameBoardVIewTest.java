package YutGame.view.swing;

import YutGame.view.javafx.GameBoardViewFX;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import javax.swing.*;

import java.awt.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class GameBoardVIewTest {

    /*GameBoardView 생성자 호출 후 getYutButton이 null이 아니고 버튼 6개가 제대로 활정화 되었는지 테스트
    버튼이 각 플레이어 수, 말 수로 초기화 되었는지 테스트(Swing)*/
    @Test
    public void testInitComponents() {
        GameBoardView view = new GameBoardView("사각형", 2, 2);

        assertNotNull(view.getYutButtons());
        assertEquals(6, view.getYutButtons().length);

        assertNotNull(view.getRandomButton());

        JButton[][] pieceBtns = view.getPieceButtons();
        assertEquals(2, pieceBtns.length);
        assertEquals(2, pieceBtns[0].length);
    }

    // 게임 보드 불러온 후 오류 없이 화면 출력이 되는지 테스트(Swing)
    @Test
    public void testFrameOpen() {
        SwingUtilities.invokeLater(() -> {
            GameBoardView view = new GameBoardView("사각형", 2, 2);
            view.setVisible(true);
            view.dispose();  // 리소스 낭비 방지
        });
    }
}
