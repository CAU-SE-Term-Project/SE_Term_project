package YutGame.model;

import java.util.*;

/** 비즈니스 로직 전담 */
public final class Game {

    /* 상태 */
    private final Board board = new SquareBoard();
    private final Map<Integer,Player> players = new HashMap<>();
    private final Map<Integer,Piece> pieces = new HashMap<>();
    private final List<Player> turnOrder = new ArrayList<>();

    private int curIdx;
    private YutResult lastRoll;

    /* 초기화 */
    public void init(int nPlayers,int nPieces){
        if(nPlayers<2||nPlayers>4||nPieces<2||nPieces>5)
            throw new IllegalArgumentException("플레이어 2‑4, 말 2‑5 허용");
        players.clear();pieces.clear();turnOrder.clear();
        int seq=1;
        for(int p=1;p<=nPlayers;p++){
            Player pl=new Player(p);
            for(int i=0;i<nPieces;i++){
                Piece pc=new Piece(seq++,p);
                pl.pieces().add(pc);
                pieces.put(pc.id(),pc);
            }
            players.put(p,pl);turnOrder.add(pl);
        }
        curIdx=0;lastRoll=null;
    }

    public int currentPlayerId(){return turnOrder.get(curIdx).id();}

    public boolean finished(){return turnOrder.stream().anyMatch(pl->pl.hasWon(board));}

    public int winnerId(){
        return turnOrder.stream().filter(pl->pl.hasWon(board))
                .map(Player::id).findFirst().orElseThrow();
    }

    /* 윷 던지기 */
    public YutResult rollRandom(){ return lastRoll = YutResult.random(); }
    public YutResult roll(YutResult r){ return lastRoll = r; }

    public boolean hasMovable(){
        if(lastRoll==null)return false;
        Player cur=turnOrder.get(curIdx);
        return cur.pieces().stream()
                .anyMatch(pc->!pc.isHome(board)&&
                        board.next(pc.position(),lastRoll.steps())<=board.getEndPosition());
    }

    /* 이동 */
    public MoveOutcome move(int pieceId){
        if(lastRoll==null) throw new IllegalStateException("먼저 윷을 던지세요");
        Piece piece=pieces.get(pieceId);
        if(piece==null) throw new IllegalArgumentException("잘못된 ID");
        if(piece.ownerId()!=currentPlayerId()) throw new IllegalStateException("남의 말");

        // 같은 칸 묶음
        List<Piece> group=new ArrayList<>();
        for(Piece pc:players.get(piece.ownerId()).pieces())
            if(pc.position()==piece.position()) group.add(pc);

        int dest=board.next(piece.position(),lastRoll.steps());

        // 잡기
        List<Integer> captured=new ArrayList<>();
        for(Piece other:pieces.values()){
            if(other.ownerId()!=piece.ownerId()&&other.position()==dest){
                other.setPosition(Board.START_POS);captured.add(other.id());
            }
        }

        group.forEach(pc->pc.setPosition(dest));
        boolean extra=lastRoll.extraTurn()||!captured.isEmpty();
        lastRoll=null;
        return new MoveOutcome(dest,captured,extra);
    }

    /* 턴 */
    public void nextTurn(){curIdx=(curIdx+1)%turnOrder.size();}

    /* 게터 – 뷰 편의를 위해 read‑only 제공 */
    public Map<Integer,Player> players(){return Collections.unmodifiableMap(players);}
    public Board board(){return board;}
}
