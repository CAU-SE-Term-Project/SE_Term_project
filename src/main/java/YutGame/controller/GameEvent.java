package YutGame.controller;

import java.util.Map;

/** 단순 이벤트 DTO */
public final class GameEvent {
    private final GameEventType type;
    private final Map<String,Object> data;

    public GameEvent(GameEventType type, Map<String,Object> data){
        this.type=type;this.data=data;
    }
    public GameEventType getType(){return type;}
    public Map<String,Object> getPayload(){return data;}
}
