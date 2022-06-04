package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.List;
import java.util.Random;

/**
 * GPA Random yang selalu memilih aksi secara acak.
 *
 * @author Jiang Han
 */
public class RandomAgent extends GamePlayingAgent {

    @Override
    public GameAction selectAction(GameState state, GameModel model) {
        List<GameAction> actions = state.getAvailableActions();
        return actions.get((new Random()).nextInt(actions.size()));
    }

    @Override
    public String getConfigurationString() {
        return "";
    }
}
