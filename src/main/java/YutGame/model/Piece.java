package YutGame.model;
import java.util.Stack;


/** 말(토큰) */
public final class Piece {
    private final int id;
    private final int ownerId;
    private int pos = Board.START_POS;
    private Stack<Integer> path = new Stack<>();

    public Piece(int id,int ownerId){this.id=id;this.ownerId=ownerId;}

    public int id(){return id;}
    public int ownerId(){return ownerId;}
    public int position(){return pos;}
    public void setPosition(int p){pos=p;}

    public void pushPath(int p){path.push(p);}
    public int popPath(){return path.pop();}
    public int peekPath(){return path.peek();}
    public boolean pathIsEmpty(){return path.isEmpty();}
    public boolean pathContains(int p){return path.contains(p);}
    public boolean isHome(Board board){return pos>=board.getEndPosition();}
}

