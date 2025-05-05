// Swing 기반 UI 불러오기(GameSetUpView)
package view.swingui;

import controller.GameSetUpController;

import javax.swing.*;

public class YutGameSwingView {
    public static void startGameSwingApp(){
        SwingUtilities.invokeLater(() -> {
            GameSetUpView setupFrame = new GameSetUpView();
            new GameSetUpController(setupFrame);
            setupFrame.setVisible(true);
        });
    }
}
