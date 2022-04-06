package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;

/**
 *
 * @author Jiang Han
 */
public abstract class GamePlayingAgent {
    abstract public GameAction selectAction(GameState state, GameModel model);
    abstract public String getConfigurationString();
}
