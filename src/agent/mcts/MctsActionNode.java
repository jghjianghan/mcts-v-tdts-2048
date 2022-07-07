package agent.mcts;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;

/**
 * Node pohon MCTS yang merepresentasikan aksi pada state tertentu.
 *
 * @author Jiang Han
 */
class MctsActionNode extends ActionNode {

    private double totalUtility = 0;

    MctsActionNode(GameAction action, StateNode parent) {
        super(action, parent);
    }

    @Override
    public void updateUtility(double result) {
        totalUtility += result;
        double currentUtility = getUtility();
        localLowerBound = Math.min(localLowerBound, currentUtility);
        localUpperBound = Math.max(localUpperBound, currentUtility);
        
        ActionNode parentAction = parent.parent;
        if (parentAction != null) {
            parentAction.localLowerBound = Math.min(parentAction.localLowerBound, currentUtility);
            parentAction.localUpperBound = Math.max(parentAction.localUpperBound, currentUtility);
        }
    }

    @Override
    public double getUtility() {
        return totalUtility / this.getVisitCount();
    }

    @Override
    public StateNode simulateAction(GameModel model) {
        GameState nextState = model.applyAction(parent.state, this.action);

        if (children.containsKey(nextState)) {
            return (StateNode) children.get(nextState);
        } else {
            MctsStateNode newNode = new MctsStateNode(nextState, this);
            children.put(nextState, newNode);
            return newNode;
        }
    }
}
