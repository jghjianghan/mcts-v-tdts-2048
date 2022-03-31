package game;

/**
 * An Enumerations of game actions
 * @author Jiang Han
 */
public enum GameAction {
    LEFT(0),
    RIGHT(1),
    UP(2),
    DOWN(3);
    
    public final int id;
    GameAction(int id){
        this.id = id;
    }
}
