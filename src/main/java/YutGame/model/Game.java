package YutGame.model;

import java.util.*;

/** 비즈니스 로직 전담 */
public final class Game {

    /* 상태 */
    private final Board board = new SquareBoard();
    private final Map<Integer,Player> players = new HashMap<>();
    private final Map<Integer,Piece> pieces = new HashMap<>();
    private final List<List<Piece>> groups = new ArrayList<>();
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

    private List<Piece> findGroupOf(Piece piece) {
        for (List<Piece> group : groups) {
            if (group.contains(piece)) return group;
        }
        return null; // 해당 말이 속한 그룹이 없음
    }
    private void tryGroup(Piece movedPiece) {
        // 이미 그룹에 속해있으면 아무것도 안 함
        if (findGroupOf(movedPiece) != null) return;

        int pos = movedPiece.position();
        List<Piece> group = new ArrayList<>();

        for (Piece p : pieces.values()) {
            if (p != movedPiece && p.position() == pos && findGroupOf(p) == null) {
                group.add(p);
            }
        }

        // 본인도 추가
        if (!group.isEmpty()) {
            group.add(movedPiece);
            groups.add(group);
        }
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
        if (piece == null) throw new IllegalArgumentException("존재하지 않는 말입니다");

        int steps = lastRoll.steps();
        int dest = board.next(piece.position(), steps);

        // 그룹 판단
        List<Piece> group = findGroupOf(piece);
        System.out.println("group:"+group);
        // 1) 이동
        List<Integer> movedIds = new ArrayList<>();
        if (group != null) {
            for (Piece p : group) {
                p.setPosition(dest);
                System.out.println("position of p:"+p.id()+"   "+p.position());
                movedIds.add(p.id());
                System.out.println(groups);
            }
        } else {
            piece.setPosition(dest);
            movedIds.add(piece.id());
            tryGroup(piece); // 새 그룹 구성 시도
            System.out.println(groups);
        }

        // 2) 잡기 처리 (상대편 말이 같은 위치에 있을 경우)
        List<Integer> captured = new ArrayList<>();
        for (Piece other : pieces.values()) {
            if (other.ownerId() != piece.ownerId() && other.position() == dest) {
                List<Piece> victimGroup = findGroupOf(other);
                if (victimGroup != null) {
                    // 그룹 전체 잡힘
                    for (Piece victim : victimGroup) {
                        victim.setPosition(board.START_POS);
                        captured.add(victim.id());
                    }
                    groups.remove(victimGroup);
                } else {
                    // 단독 말 잡힘
                    other.setPosition(board.START_POS);
                    captured.add(other.id());
                }
            }
        }

        // 3) 골인 시 그룹 제거
        if (dest == (SquareBoard.FINISH) && group != null) {
            groups.remove(group);
        }

        // 4) 턴 처리
        boolean extra = lastRoll.extraTurn() || !captured.isEmpty();
        lastRoll = null;

        return new MoveOutcome(dest, movedIds, captured, extra);
    }




    /* 턴 */
    public void nextTurn(){curIdx=(curIdx+1)%turnOrder.size();}

    /* 게터 – 뷰 편의를 위해 read​‑only 제공 */
    public Map<Integer,Player> players(){return Collections.unmodifiableMap(players);}
    public Board board(){return board;}
}
