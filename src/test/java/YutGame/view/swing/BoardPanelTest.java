package YutGame.view.swing;

import org.junit.Test;

import static org.junit.Assert.*;

public class BoardPanelTest {

    // BoardPanel이 정상적으로 채워지는지, piecemanager가 null이 아닌지 테스트(Swing)
    @Test
    public void testConstructor_initializesCorrectly() {
        BoardPanel panel = new BoardPanel("사각형", 4);
        assertNotNull(panel.getPieceManager());
    }
}
