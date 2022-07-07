package agent.tdts;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;

/**
 * Node pohon TDTS yang merepresentasikan aksi pada state tertentu.
 *
 * @author Jiang Han
 */
class TdtsActionNode extends ActionNode {

    private double utility = 0; //insert V_init here

    TdtsActionNode(GameAction action, StateNode parent) {
        super(action, parent);
    }

    @Override
    public void updateUtility(double result) {
        double updateStepSize = 1.0 / this.getVisitCount();
        utility = utility + updateStepSize * result;
        
        localLowerBound = Math.min(localLowerBound, utility);
        localUpperBound = Math.max(localUpperBound, utility);

        ActionNode parentAction = parent.parent;
        if (parentAction != null) {
            parentAction.localLowerBound = Math.min(parentAction.localLowerBound, utility);
            parentAction.localUpperBound = Math.max(parentAction.localUpperBound, utility);
        }
    }

    @Override
    public double getUtility() {
        return utility;
    }

    @Override
    public StateNode simulateAction(GameModel model) {
        GameState nextState = model.applyAction(parent.state, this.action);

        if (children.containsKey(nextState)) {
            return children.get(nextState);
        } else {
            TdtsStateNode newNode = new TdtsStateNode(nextState, this);
            children.put(nextState, newNode);
            return newNode;
        }
    }
}
