package YutGame.controller;

import YutGame.model.*;
import YutGame.view.GameView;

import java.util.*;

public final class GameControllerImpl implements GameController {

    private final Game game = new Game();
    private final List<GameView> views = new ArrayList<>();

    /* ---------- GameController 구현 ---------- */

    @Override
    public void startNewGame(int numPlayers,int numPieces){
        try{
            game.init(numPlayers,numPieces);
            publish(GameEventType.GAME_STARTED,Map.of(
                    "numPlayers",numPlayers,"numPieces",numPieces,
                    "currentPlayer",game.currentPlayerId()));
        }catch(Exception e){publishErr(e);}
    }

    @Override public void abortGame(){game.init(0,0);publish(GameEventType.GAME_ENDED,Map.of());}

    @Override public void rollRandomYut(){
        YutResult r=game.rollRandom();
        publish(GameEventType.YUT_ROLLED,Map.of("result",r));
    }

    @Override public void rollManualYut(YutResult manual){
        YutResult r=game.roll(manual);
        publish(GameEventType.YUT_ROLLED,Map.of("result",r,"manual",true));
    }

    @Override public void selectPiece(int pieceId){
        try{
            MoveOutcome out=game.move(pieceId);
            publish(GameEventType.PIECE_MOVED,Map.of(
                    "pieceId",pieceId,"newPos",out.newPosition(),
                    "captured",out.capturedPieceIds()));

            if(game.finished()){
                publish(GameEventType.GAME_ENDED,Map.of("winner",game.winnerId()));
            }else if(!out.extraTurn()){
                endTurn();
            }
        }catch(Exception e){publishErr(e);}
    }

    @Override public void endTurn(){
        game.nextTurn();
        publish(GameEventType.TURN_CHANGED,Map.of("currentPlayer",game.currentPlayerId()));
    }

    /* ---------- View 관리 ---------- */
    @Override public void registerView(GameView v){views.add(v);}
    @Override public void unregisterView(GameView v){views.remove(v);}

    /* ---------- 내부 ---------- */
    private void publish(GameEventType t,Map<String,Object> m){
        GameEvent evt=new GameEvent(t,m);
        views.forEach(v->v.onGameEvent(evt));
    }
    private void publishErr(Exception ex){
        publish(GameEventType.ERROR,Map.of("msg",ex.getMessage()));
    }
}
