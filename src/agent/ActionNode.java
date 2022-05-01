package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.HashMap;
import java.util.Map;

/**
 * Class ini merepresentasikan sebuah aksi yang dapat dipilih dari state
 * tertentu pada pohon pencarian.
 *
 * @author Jiang Han
 */
public abstract class ActionNode {

    public final GameAction action;
    protected Map<GameState, StateNode> children;
    public final StateNode parent;
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

    public double getLowerBound() {
        return localLowerBound;
    }

    public double getUpperBound() {
        return localUpperBound;
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

    /**
     * Memperbarui informasi utilitas yang tercapat pada state ini, termasuk
     * local bounds-nya.
     *
     * @param result Hasil permainan
     */
    public abstract void updateUtility(double result);

    public abstract double getUtility();
}