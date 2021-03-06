package agent.tdts;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import game.GameModel.GameState;
import java.util.List;

/**
 * Node pohon TDTS yang merepresentasikan state. Semua action dari state ini
 * otomatis diexpand.
 *
 * @author Jiang Han
 */
class TdtsStateNode extends StateNode {

    TdtsStateNode(GameState state, ActionNode action) {
        super(state, action);
        children = new ActionNode[GameAction.values().length];
        List<GameAction> validActions = state.getAvailableActions();
        for (GameAction validAction : validActions) {
            children[validAction.id] = new TdtsActionNode(validAction, this);
        }
    }
}
