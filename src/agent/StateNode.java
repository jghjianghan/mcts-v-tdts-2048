package agent;

import game.GameModel.GameState;
import game.GameAction;

/**
 * Node pada pohon pencarian yang merepresentasikan state
 *
 * @author Jiang Han
 */
class StateNode {

    GameState state;
    ActionNode parent;
    ActionNode[] children;

    StateNode(GameState state, ActionNode action) {
        this.state = state;
        parent = action;
        children = new ActionNode[GameAction.values().length];
    }

    StateNode(GameState state) {
        this(state, null);
    }

    @Override
    public int hashCode() {
        return state.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StateNode other = (StateNode) obj;
        return this.state.equals(other.state);
    }
    
}
