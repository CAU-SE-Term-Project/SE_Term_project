package YutGame.model;

import java.util.Stack;

/** 말(토큰) */
public final class Piece {
    private final int id;
    private final int ownerId;

    private int pos = Board.START_POS;
    private Stack<Integer> path = new Stack<>();   // 경로 스택

    public Piece(int id, int ownerId) {
        this.id      = id;
        this.ownerId = ownerId;
    }

    /* ───── 기본 프로퍼티 ───── */
    public int id()          { return id; }
    public int ownerId()     { return ownerId; }
    public int position()    { return pos; }
    public void setPosition(int p) { pos = p; }

    /* ───── path 조작 ───── */
    public void pushPath(int p)    { path.push(p); }
    public int  popPath()          { return path.pop(); }
    public int  peekPath()         { return path.peek(); }
    public boolean pathIsEmpty()   { return path.isEmpty(); }
    public boolean pathContains(int p) { return path.contains(p); }

    /* ───── path 공유용 ───── */
    void   setSharedPath(Stack<Integer> shared) { this.path = shared; }
    Stack<Integer> getPath()                    { return path; }

    /* ───── 상태 ───── */
    public boolean isHome(Board board) {
        return pos >= board.getEndPosition();
    }
}
