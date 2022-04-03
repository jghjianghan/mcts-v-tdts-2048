/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.mcts;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import game.GameModel.GameState;
import java.util.List;

/**
 * Node pohon MCTS yang merepresentasikan state. Semua action dari state ini
 * otomatis diexpand.
 *
 * @author Jiang Han
 */
class MctsStateNode extends StateNode {

    MctsStateNode(GameState state, ActionNode action) {
        super(state, action);
        children = new ActionNode[GameAction.values().length];
        List<GameAction> validActions = state.getAvailableActions();
        for (GameAction validAction : validActions) {
            children[validAction.id] = new MctsActionNode(validAction, this);
        }
    }
}
