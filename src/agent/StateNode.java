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

    public final ActionNode parent;
    //Jika action ke-i tidak valid, elemen ke-i nilainya null
    protected ActionNode[] children;

    private int visitCount = 0;

    public StateNode(GameState state) {
        this(state, null);
    }

    public StateNode(GameState state, ActionNode action) {
        this.state = state;
        parent = action;
    }

    public GameState getState() {
        return state;
    }
    
    public int getVisitCount() {
        return visitCount;
    }

    public void incrementVisitCount() {
        visitCount++;
    }

    /**
     * Mengembalikan node yang merepresentasikan aksi tertentu yang dapat
     * diambil dari state ini.
     *
     * @param action
     * @return
     */
    public ActionNode getChildNode(GameAction action) {
        return children[action.id];
    }
}
