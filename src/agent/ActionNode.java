package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Jiang Han
 */
public abstract class ActionNode {

    public final GameAction action;
    protected Map<GameState, StateNode> children;
    public StateNode parent;
    private int visitCount = 0;
    public double localLowerBound = Double.POSITIVE_INFINITY;
    public double localUpperBound = Double.NEGATIVE_INFINITY;

    public ActionNode(GameAction action, StateNode parent) {
        this.action = action;
        this.parent = parent;
        children = new HashMap<>();
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void incrementVisitCount() {
        visitCount++;
    }

    /**
     * Menerapkan aksi ini untuk mendapatkan node State selanjutnya. Jika state
     * baru pernah dikunjungi, maka method ini akan mereturn salah satu child.
     * Kalau belum, StateNode baru akan dibuatkan
     *
     * @param model
     * @return Node yang merepresentasikan state
     */
    public abstract StateNode simulateAction(GameModel model);

    public abstract void updateUtility(GameResult result);

    public abstract double getUtility();
    
    public double getLowerBound(){
        return localLowerBound;
    }
    
    public double getUpperBound(){
        return localUpperBound;
    }
}
