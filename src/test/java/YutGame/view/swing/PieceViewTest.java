package YutGame.view.swing;

import YutGame.view.javafx.PieceViewFX;
import javafx.scene.image.WritableImage;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class PieceViewTest {

    //piece 기본 설정(id, ownerId, img, 초기 위치가 -1인지) 테스트(Swing)
    @Test
    void testSwingPieceViewInitialization() {
        ImageIcon icon = new ImageIcon(); // mock ImageIcon
        PieceView piece = new PieceView(1, 2, icon);

        assertEquals(1, piece.getId());
        assertEquals(2, piece.getOwnerId());
        assertEquals(icon.getImage(), piece.getImage());
        assertEquals(-1, piece.getPosition()); // 초기 상태 확인
    }

    //piece 객체가 제대로 동작하는지 setposition과 getposition 테스트(Swing)
    @Test
    void testSwingSetAndGetPosition() {
        PieceView piece = new PieceView(1, 2, new ImageIcon());
        piece.setPosition(5);
        assertEquals(5, piece.getPosition());
    }

    //piece 기본 설정(id, ownerId, dummy img, 초기 위치가 -1인지) 테스트(JavaFX)
    @Test
    void testFXPieceViewInitialization() {
        WritableImage dummyImage = new WritableImage(10, 10); // 흰 배경 빈 이미지
        PieceViewFX piece = new PieceViewFX(1, 2, dummyImage);

        assertEquals(1, piece.getId());
        assertEquals(2, piece.getOwnerId());
        assertEquals(dummyImage, piece.getImage());
        assertEquals(-1, piece.getPosition()); // 초기 상태 확인
    }

    //piece 객체가 제대로 동작하는지 setposition과 getposition 테스트(JavaFX)
    @Test
    void testFXSetAndGetPosition() {
        PieceViewFX piece = new PieceViewFX(1, 2, new WritableImage(10, 10));
        piece.setPosition(5);
        assertEquals(5, piece.getPosition());
    }
}
