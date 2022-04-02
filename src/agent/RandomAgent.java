package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jiang Han
 */
public class RandomAgent extends GamePlayingAgent {

    @Override
    public GameAction selectAction(GameState state, GameModel model) {
        List<GameAction> actions = state.getAvailableActions();
        return actions.get((new Random()).nextInt(actions.size()));
    }
    
}
