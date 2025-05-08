package YutGame.view.swing;

import YutGame.model.YutResult;

import javax.swing.*;
import java.awt.*;

/** 실제 플레이 화면 */
public class GameBoardView extends JFrame {

    public interface UiCallback {
        void onYutClicked(YutResult r);
        void onRandomClicked();
        void onPieceClicked(int pieceId);
        void onResultChosen(YutResult r);
    }

    private UiCallback cb;
    private BoardPanel boardPanel;
    public void setCallback(UiCallback cb){ this.cb = cb; }


    /* ── 상태 & 위젯 ─────────────────────────── */
    private final int numPlayers, numPieces;
    private JButton[]   yutButtons;
    private JButton     randomButton;
    private JButton[][] pieceButtons;
    private JLabel[]    playerLabels;
    private JLabel      turnLabel;

    /* ── 생성 ───────────────────────────────── */
    public GameBoardView(String boardType,int numPlayers,int numPieces){
        this.numPlayers = numPlayers;
        this.numPieces  = numPieces;

        setTitle("윷놀이 판 ("+boardType+")");
        setSize(700, 900);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        add(createPlayerInfoPanel(), BorderLayout.NORTH);
        this.boardPanel = new BoardPanel(boardType, numPieces);
        add(boardPanel, BorderLayout.CENTER);
        add(createYutControlPanel(),    BorderLayout.SOUTH);
    }

    /* ── UI 구성 ─────────────────────────────── */
    private JPanel createPlayerInfoPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());

        turnLabel = new JLabel("현재 턴: Player 1", SwingConstants.CENTER);
        turnLabel.setBorder(BorderFactory.createEmptyBorder(6,0,6,0));
        turnLabel.setFont(turnLabel.getFont().deriveFont(Font.BOLD,16f));
        wrapper.add(turnLabel, BorderLayout.NORTH);

        JPanel list = new JPanel(new GridLayout(numPlayers,1));
        playerLabels = new JLabel[numPlayers];
        for (int i=0;i<numPlayers;i++) {
            JLabel lbl = new JLabel("Player "+(i+1)+" - 남은 말: "+numPieces);
            playerLabels[i]=lbl;
            list.add(lbl);
        }
        wrapper.add(list, BorderLayout.CENTER);
        return wrapper;
    }

    private JPanel createYutControlPanel() {
        JPanel base = new JPanel(new BorderLayout());

        JPanel yut = new JPanel();
        yut.setBorder(BorderFactory.createTitledBorder("윷 던지기"));

        String[] kor={"백도","도","개","걸","윷","모"};
        yutButtons = new JButton[kor.length];
        for(int i=0;i<kor.length;i++){
            yutButtons[i] = new JButton(kor[i]);
            int idx = i;                                   // for lambda
            yutButtons[i].addActionListener(e -> {
                if(cb!=null) cb.onYutClicked(YutResult.values()[idx]);
            });
            yut.add(yutButtons[i]);
        }
        randomButton = new JButton("랜덤");
        randomButton.setPreferredSize(new Dimension(80,30));
        randomButton.addActionListener(e -> {
            if (cb != null)
                cb.onRandomClicked();                   // → Adapter → Controller.rollRandomYut()
        });
        yut.add(randomButton);

        base.add(yut,BorderLayout.NORTH);
        base.add(createPiecePanel(),BorderLayout.SOUTH);
        return base;
    }

    /** 플레이어별 말 버튼을 만드는 패널  */
    private JPanel createPiecePanel() {

        JPanel grid = new JPanel(new GridLayout(numPlayers, 1));
        pieceButtons = new JButton[numPlayers][numPieces];

        for (int p = 0; p < numPlayers; p++) {
            JPanel row = new JPanel();
            row.setBorder(BorderFactory.createTitledBorder("플레이어 " + (p + 1)));

            for (int i = 0; i < numPieces; i++) {

                JButton btn = new JButton("말 " + (i + 1));
                pieceButtons[p][i] = btn;
                row.add(btn);

                /* ── 클릭 → UiCallback 전달 ── */
                final int pieceId = p * numPieces + i + 1;   // 고유 ID (1‑based)
                btn.addActionListener(e -> {
                    if (cb != null) cb.onPieceClicked(pieceId);
                });
            }
            grid.add(row);
        }
        return grid;
    }


    /* ── getter (컨트롤러용) ─────────────────── */
    public JButton[]   getYutButtons()   { return yutButtons; }
    public JButton     getRandomButton() { return randomButton; }
    public JButton[][] getPieceButtons() { return pieceButtons; }

    /* ── GameView Adapter 가 호출 ─────────────── */
    public void initPiecesUi(){ repaint(); }

    public void highlightYut(YutResult r){
        String kor = switch (r){
            case BACK_DO->"백도"; case DO->"도";   case GAE->"개";
            case GEOL  ->"걸";   case YUT->"윷";  case MO ->"모";
        };
        Color def = UIManager.getColor("Button.background");
        for (JButton b:yutButtons)
            b.setBackground(b.getText().equals(kor)?Color.YELLOW:def);
    }

    public void updatePiece(int pieceId,int newPos){ repaint(); }

    public void showResultDialog(java.util.List<YutResult> results){
        JDialog d = new JDialog(this,"윷 결과 선택",true);
        d.setLayout(new FlowLayout(FlowLayout.CENTER,12,12));
        for(YutResult r:results){
            JButton bt = new JButton(textKor(r));
            bt.setPreferredSize(new Dimension(64,32));
            bt.addActionListener(e -> {
                if(cb!=null) cb.onResultChosen(r);
                d.dispose();
            });
            d.add(bt);
        }
        d.pack();
        d.setLocationRelativeTo(this);
        d.setVisible(true);
    }
    private static String textKor(YutResult r) {
        return switch (r) {
            case BACK_DO -> "백도"; case DO -> "도";
            case GAE     -> "개";   case GEOL -> "걸";
            case YUT     -> "윷";   case MO   -> "모";
        };
    }

    public void setCurrentPlayer(int id){
        turnLabel.setText("현재 턴: Player "+id);
        int idx=id-1;
        for(int i=0;i<playerLabels.length;i++){
            boolean turn=i==idx;
            playerLabels[i].setForeground(turn?Color.RED:Color.BLACK);
            playerLabels[i]
                    .setFont(playerLabels[i].getFont().deriveFont(
                            turn?Font.BOLD:Font.PLAIN));
        }
    }


    public BoardPanel getBoardPanel() {
        return boardPanel;
    }
}
