package YutGame.view.swing;

import YutGame.ui.GameSetUpController;

import javax.swing.SwingUtilities;

public final class YutGameSwingView {
    private YutGameSwingView() {}

    public static void startGameSwingApp() {
        SwingUtilities.invokeLater(() -> {
            GameSetUpView setupFrame = new GameSetUpView();
            new GameSetUpController(setupFrame);
            setupFrame.setVisible(true);
        });
    }
}