package YutGame.model;

import java.util.List;

public record MoveOutcome(int newPosition,
                          List<Integer> capturedPieceIds,
                          boolean extraTurn) { }
