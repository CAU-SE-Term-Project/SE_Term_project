package YutGame.model;

/**
 * 윷판 추상화.
 *  • START_POS(-1) : 출발 대기
 *  • getEndPosition() : 도착 노드 id
 *  • next() : 현재 위치에서 steps 만큼 이동한 노드 id
 */
public interface Board {

    int START_POS = 0;

    int getEndPosition();

    /**
     * @param current 현재 위치(‑1 = 출발)
     * @param steps   이동 칸 수(빽도 = ‑1)
     */
    int next(int current, int steps, Piece piece);

    BoardShape shape();
}
