package agent;

import game.GameAction;
import game.GameModel.GameState;

/**
 * Node pada pohon pencarian yang merepresentasikan state. Semua action node
 * yang valid otomatis ditambahkan sebagai children.
 *
 * @author Jiang Han
 */
public abstract class StateNode {

    public final GameState state;
    
    public ActionNode parent;
    protected ActionNode[] children;

    private int visitCount = 0;

    public StateNode(GameState state, ActionNode action) {
        this.state = state;
        parent = action;       
    }

    public StateNode(GameState state) {
        this(state, null);
    }

    public GameState getState() {
        return state;
    }
    
    public ActionNode getChildNode(GameAction action) {
        return children[action.id];
    }
    
    public int getVisitCount() {
        return visitCount;
    }

    public void incrementVisitCount() {
        visitCount++;
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
