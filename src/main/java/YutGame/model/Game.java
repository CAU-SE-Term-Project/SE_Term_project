package YutGame.model;

import java.util.*;

/** 비즈니스 로직 전담 */
public final class Game {

    /* ─── 상태 ─── */
    private Board board;

    private final Map<Integer, Player> players = new HashMap<>();
    private final Map<Integer, Piece>  pieces  = new HashMap<>();
    private final List<List<Piece>>    groups  = new ArrayList<>();
    private final List<Player>         turnOrder = new ArrayList<>();

    private int       curIdx;
    private YutResult lastRoll;

    /* ─── 초기화 ─── */
    public void init(int nPlayers, int nPieces, String boardType) {
        if (nPlayers < 2 || nPlayers > 4 || nPieces < 2 || nPieces > 5)
            throw new IllegalArgumentException("플레이어 2-4, 말 2-5 허용");

        players.clear(); pieces.clear(); turnOrder.clear(); groups.clear();
        int seq = 1;
        for (int p = 1; p <= nPlayers; p++) {
            Player pl = new Player(p);
            for (int i = 0; i < nPieces; i++) {
                Piece pc = new Piece(seq++, p);
                pl.pieces().add(pc);
                pieces.put(pc.id(), pc);
            }
            players.put(p, pl);
            turnOrder.add(pl);
        }

        board = switch (boardType) {
            case "오각형" -> new PentagonBoard();
            case "육각형" -> new HexagonBoard();
            default      -> new SquareBoard();
        };
        curIdx = 0;
        lastRoll = null;
    }

    /* ─── 그룹 헬퍼 ─── */

    private List<Piece> findGroupOf(Piece piece) {
        for (List<Piece> g : groups) if (g.contains(piece)) return g;
        return null;
    }

    /** 같은 칸 아군을 그룹화 (공유 스택 설정 포함) */
    private void tryGroup(Piece moved) {
        if (findGroupOf(moved) != null) return;      // 이미 그룹 소속

        int pos = moved.position();
        List<Piece> target = null;
        for (List<Piece> g : groups)
            if (g.stream().anyMatch(p -> p.position() == pos)) { target = g; break; }

        if (target == null) { target = new ArrayList<>(); groups.add(target); }
        target.add(moved);

        /* ★ 스택 공유 ★ */
        Stack<Integer> shared = target.get(0).getPath();
        for (Piece p : target) p.setSharedPath(shared);
    }

    /** 병합 후 스택 공유 유지 */
    private void mergeGroups(int owner, int pos) {
        List<List<Piece>> toMerge = new ArrayList<>();
        for (List<Piece> g : groups)
            if (!g.isEmpty() &&
                    g.get(0).ownerId() == owner &&
                    g.stream().allMatch(p -> p.position() == pos))
                toMerge.add(g);

        if (toMerge.size() > 1) {
            List<Piece> merged = new ArrayList<>();
            toMerge.forEach(merged::addAll);
            groups.removeAll(toMerge);
            groups.add(merged);

            /* ★ 스택 공유 ★ */
            Stack<Integer> shared = merged.get(0).getPath();
            for (Piece p : merged) p.setSharedPath(shared);
        }
    }

    /* ─── 게임 상태 ─── */

    public int  currentPlayerId() { return turnOrder.get(curIdx).id(); }
    public boolean finished()    { return turnOrder.stream().anyMatch(pl -> pl.hasWon(board)); }
    public int  winnerId()       { return turnOrder.stream().filter(pl -> pl.hasWon(board))
            .map(Player::id).findFirst().orElseThrow(); }

    /* 윷 던지기 */
    public YutResult rollRandom()  { return lastRoll = YutResult.random(); }
    public YutResult roll(YutResult r) { return lastRoll = r; }

    public boolean hasMovable() {
        if (lastRoll == null) return false;
        Player cur = turnOrder.get(curIdx);
        return cur.pieces().stream().anyMatch(pc ->
                !pc.isHome(board) &&
                        board.next(pc.position(), lastRoll.steps(), pc) <= board.getEndPosition());
    }

    /* ─── 이동 ─── */

    public MoveOutcome move(int pieceId) {
        if (lastRoll == null) throw new IllegalStateException("먼저 윷을 던지세요");

        Piece piece = pieces.get(pieceId);
        if (piece == null) throw new IllegalArgumentException("존재하지 않는 말입니다");

        int dest = board.next(piece.position(), lastRoll.steps(), piece);

        /* 1) 이동 */
        List<Piece> group = findGroupOf(piece);
        List<Integer> movedIds = new ArrayList<>();
        if (group != null) {
            for (Piece p : group) { p.setPosition(dest); movedIds.add(p.id()); }
        } else {
            piece.setPosition(dest); movedIds.add(piece.id()); tryGroup(piece);
        }

        /* 2) 병합 + 스택공유 */
        mergeGroups(piece.ownerId(), dest);

        /* 3) 잡기 */
        List<Integer> captured = new ArrayList<>();
        for (Piece other : pieces.values()) {
            if (other.ownerId() != piece.ownerId() && other.position() == dest && dest != Board.START_POS) {
                List<Piece> victimGroup = findGroupOf(other);
                if (victimGroup != null) {
                    for (Piece victim : new ArrayList<>(victimGroup)) {
                        if (victim.ownerId() != piece.ownerId()) {
                            victim.setPosition(Board.START_POS);
                            captured.add(victim.id());
                            victimGroup.remove(victim);
                        }
                    }
                    if (victimGroup.isEmpty()) groups.remove(victimGroup);
                } else {
                    other.setPosition(Board.START_POS);
                    captured.add(other.id());
                }
            }
        }

        /* 4) 골인 시 그룹 제거 */
        if (dest == SquareBoard.FINISH && group != null && group.isEmpty()) groups.remove(group);

        /* 5) 턴 처리 */
        boolean extra = lastRoll.extraTurn() || !captured.isEmpty();
        lastRoll = null;
        return new MoveOutcome(dest, movedIds, captured, extra);
    }

    /* ─── 턴 ─── */
    public void nextTurn() { curIdx = (curIdx + 1) % turnOrder.size(); }

    /* ─── 읽기 전용 ─── */
    public Map<Integer, Player> players() { return Collections.unmodifiableMap(players); }
    public Board board() { return board; }
}
