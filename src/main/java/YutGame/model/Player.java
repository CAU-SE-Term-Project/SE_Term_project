package YutGame.model;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private final int id;
    private final List<Piece> pieces = new ArrayList<>();

    public Player(int id){this.id=id;}
    public int id(){return id;}
    public List<Piece> pieces(){return pieces;}

    public boolean hasWon(Board board){
        return pieces.stream().allMatch(p->p.isHome(board));
    }
}
