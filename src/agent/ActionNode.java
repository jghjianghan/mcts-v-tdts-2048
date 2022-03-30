package agent;

import game.GameAction;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Jiang Han
 */
abstract class ActionNode {

    GameAction action;
    Set<StateNode> children;
    StateNode parent;

    ActionNode(GameAction action, StateNode parent) {
        this.action = action;
        this.parent = parent;
        children = new HashSet<>();
    }

    void addChild(StateNode state){
        state.parent = this;
//        children.a
    }
    
   
    
    abstract void updateUtility(GameResult result);

    abstract double getUtility();

}
