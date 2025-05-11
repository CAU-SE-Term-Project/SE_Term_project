package YutGame.app;

import YutGame.ui.GameSetUpController;
import YutGame.view.swing.GameSetUpView;

import javax.swing.*;

public class YutGameLauncher {
    public static void main(String[] args){
        SwingUtilities.invokeLater(()->{
            GameSetUpView view=new GameSetUpView();
            new GameSetUpController(view);
            view.setVisible(true);
        });
    }
}
// BugCase 1 : 여러 윷이 나왔을 때, 윷/모 를 마지막 결과 윷으로 선택 시 같은 플레이어에게 추가 턴 부여됨
//  ex) 도 도 윷의 순서로 윷을 선택하면 추가 턴 부여, 윷 도 도 순서로 선택 시 다음 플레이어에게 턴 부여
// BugCase 2 : 윷 결과를 남의 말에 적용시 남의 말 출력, 이후 결과를 본인 말에 적용하려 하면 이동할 결과가 선택되지 않았습니다.
//      출력 후 버튼이 사라짐
// BugCase 3 : ...
// BugCase 4 : 윷판과 말판 싱크 안맞음


// Final BugCase
// 두 말을 모두 보드에 윷 굴리기를 통해 투입하고 나서  그룹핑을 시도하면 그룹핑이 되지 않는다.