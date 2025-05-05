//프로그램 시작 파일
package launcher;

import view.swingui.YutGameSwingView;

public class YutGameLauncher {
    public static void main(String[] args) {
        boolean useJavaFX = false;

        if(useJavaFX){

        }else{
            YutGameSwingView.startGameSwingApp();
        }
    }
}
