package YutGame.view.swing;

import javax.swing.*;
import java.awt.*;

/** 게임 시작 전 옵션 화면 */
public class GameSetUpView extends JFrame {

    private final ButtonGroup playersGroup = new ButtonGroup();
    private final ButtonGroup piecesGroup  = new ButtonGroup();
    private final ButtonGroup boardGroup   = new ButtonGroup();
    private final JButton startBtn         = new JButton("시작하기");

    public GameSetUpView(){
        setTitle("윷놀이 설정");
        setSize(400,350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel center=new JPanel(new GridLayout(3,1));
        center.add(playersPanel());
        center.add(piecesPanel());
        center.add(boardPanel());
        add(center,BorderLayout.CENTER);
        add(startBtn,BorderLayout.SOUTH);
    }

    /* ── 라디오 그룹 ───────────────── */
    private JPanel playersPanel(){
        JPanel p=new JPanel(); p.setBorder(BorderFactory.createTitledBorder("플레이어 수"));
        for(int i=2;i<=4;i++){
            JRadioButton rb=new JRadioButton(i+"명");
            rb.setActionCommand(""+i);
            playersGroup.add(rb); p.add(rb);
            if(i==2)rb.setSelected(true);
        }
        return p;
    }
    private JPanel piecesPanel(){
        JPanel p=new JPanel(); p.setBorder(BorderFactory.createTitledBorder("말 개수"));
        for(int i=2;i<=5;i++){
            JRadioButton rb=new JRadioButton(i+"개");
            rb.setActionCommand(""+i);
            piecesGroup.add(rb); p.add(rb);
            if(i==2)rb.setSelected(true);
        }
        return p;
    }
    private JPanel boardPanel(){
        JPanel p=new JPanel(); p.setBorder(BorderFactory.createTitledBorder("윷판 형태"));
        JRadioButton rb=new JRadioButton("사각형",true);
        rb.setActionCommand("사각형");
        boardGroup.add(rb); p.add(rb);
        return p;
    }

    /* ── getter ───────────────────── */
    public JButton getStartButton(){ return startBtn; }
    public int getSelectedNumPlayers(){
        var sel=playersGroup.getSelection();
        return sel==null?2:Integer.parseInt(sel.getActionCommand());
    }
    public int getSelectedNumPieces(){
        var sel=piecesGroup.getSelection();
        return sel==null?2:Integer.parseInt(sel.getActionCommand());
    }
    public String getSelectedBoardType(){
        var sel=boardGroup.getSelection();
        return sel==null?"사각형":sel.getActionCommand();
    }
}
