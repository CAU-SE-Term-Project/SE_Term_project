package YutGame.model;

/** 말(토큰) */
public final class Piece {
    private final int id;
    private final int ownerId;
    private int pos = Board.START_POS;

    public Piece(int id,int ownerId){this.id=id;this.ownerId=ownerId;}

    public int id(){return id;}
    public int ownerId(){return ownerId;}
    public int position(){return pos;}
    public void setPosition(int p){pos=p;}

    public boolean isHome(Board board){return pos>board.getEndPosition();}
}
