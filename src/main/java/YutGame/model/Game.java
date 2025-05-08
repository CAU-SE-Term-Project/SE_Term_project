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
            throw new IllegalArgumentException("플레이어 2‐4, 말 2‐5 허용");
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

    /** 이동 */
    public MoveOutcome move(int pieceId) {
        if (lastRoll == null) throw new IllegalStateException("먼저 윷을 던지세요");
        Piece piece = pieces.get(pieceId);
        if (piece == null) throw new IllegalArgumentException("잘못된 ID");
        if (piece.ownerId() != currentPlayerId()) throw new IllegalStateException("남의 말");

        // 1) 같은 칸에 있는 말들 그룹핑 (업기)
        List<Piece> group = new ArrayList<>();
        int current = piece.position();
        if (current == Board.START_POS) {
            group.add(piece);
            current = 0;
        } else {
            for (Piece pc : players.get(piece.ownerId()).pieces()) {
                if (pc.position() == current) {
                    group.add(pc);
                }
            }
        }

        // 2) 이동 목적지 계산
        int dest = board.next(current, lastRoll.steps());

        // 3) 잡기 처리 → 잡힌 말들은 대기 위치(-1)로 리셋
        List<Integer> captured = new ArrayList<>();
        for (Piece other : pieces.values()) {
            if (other.ownerId() != piece.ownerId() && other.position() == dest) {
                other.setPosition(Board.START_POS);   // ← 대기 위치로 돌려보냄
                captured.add(other.id());
            }
        }

        // 4) 그룹 내 말들 한꺼번에 이동
        List<Integer> movedIds = new ArrayList<>();
        for (Piece pc : group) {
            pc.setPosition(dest);
            movedIds.add(pc.id());
        }

        // 5) 추가 턴 여부
        boolean extra = lastRoll.extraTurn() || !captured.isEmpty();
        lastRoll = null;

        // 결과에 movedIds, captured, extraTurn을 넘겨줌
        return new MoveOutcome(dest, movedIds, captured, extra);
    }



    /* 턴 */
    public void nextTurn(){curIdx=(curIdx+1)%turnOrder.size();}

    /* 게터 – 뷰 편의를 위해 read​‑only 제공 */
    public Map<Integer,Player> players(){return Collections.unmodifiableMap(players);}
    public Board board(){return board;}
}
