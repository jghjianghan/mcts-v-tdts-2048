package agent.mcts;

import agent.ActionNode;
import agent.GameResult;
import agent.StateNode;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.HashMap;

/**
 *
 * @author Jiang Han
 */
public class MCTSActionNode extends ActionNode {
    MCTSActionNode(GameAction action, StateNode parent) {
        super(action, parent);
    }
    
    @Override
    public void updateUtility(GameResult result) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public double getUtility() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public StateNode simulateAction(GameModel model) {
        GameState nextState = parent.state.copy();
        model.applyAction(nextState, this.action);
        
        if (children.containsKey(nextState)) {
            return children.get(nextState);
        } else {
            MCTSStateNode newNode = new MCTSStateNode(nextState, this);
            newNode.incrementVisitCount();
            newNode.parent = this;
            children.put(nextState, newNode);
            return newNode;
        }
    }
}
