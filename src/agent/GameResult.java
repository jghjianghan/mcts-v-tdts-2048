package agent;

import game.GameAction;
import game.GameModel.GameState;

/**
 *
 * @author Jiang Han
 */
public class GameResult {

    public final long score;

    public GameResult(long score) {
        this.score = score;
        state = null;
        precedingAction = null;
    }
    
    public final GameState state;
    public final GameAction precedingAction;
    
    public GameResult(GameAction action, GameState state){
        this.state = state;
        this.score = state.getScore();
        this.precedingAction = action;
    }
}
