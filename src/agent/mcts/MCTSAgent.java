package agent.mcts;

import agent.ActionNode;
import agent.GamePlayingAgent;
import agent.StateNode;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Game-Playing Agent dengan Algoritma UCT
 * @author Jiang Han
 */
public class MCTSAgent extends GamePlayingAgent {
    private final double EXPLORATION_CONSTANT;
    private final Random rand = new Random();
    
    public static void main(String[] args) {
        System.out.println();
    }
    
    public MCTSAgent(double c){
        EXPLORATION_CONSTANT = c;
    }
    
    @Override
    public GameAction selectAction(GameState state, GameModel model) {
        StateNode root = new MCTSStateNode(state, null);
        StateNode leaf = select(root, model);
        StateNode child = expand(leaf, model);
        
        return null;
    }
    
    private StateNode select(StateNode root, GameModel model){
        if (root.state.isTerminal()){
            return root;
        } else if (model.isUsable()) {
            for (GameAction action : GameAction.values()) {
                ActionNode child = root.getChildNode(action);
                
                //Ada aksi yang belum pernah dicoba
                if (child != null && child.getVisitCount() == 0) {
                    return root;
                }
            }
            
            //State ini sudah fully expanded
            List<ActionNode> bestChildList = new ArrayList<>();
            double bestValue = Double.NEGATIVE_INFINITY;
            for (GameAction action : GameAction.values()) {
                ActionNode child = root.getChildNode(action);
                
                int nRoot = root.getVisitCount();
                int nChild = child.getVisitCount();
                //menghitung nilai UCB1
                double value = child.getUtility() + 
                        EXPLORATION_CONSTANT * Math.sqrt(Math.log(nRoot) / nChild);
                
                if (value > bestValue){
                    bestValue = value;
                    bestChildList.clear();
                    bestChildList.add(child);
                } else if (value == bestValue){
                    bestChildList.add(child);
                }
            }
            ActionNode bestChild = bestChildList.get(rand.nextInt(bestChildList.size()));
            return select(bestChild.simulateAction(model), model);
        } else {
            return root;
        }
    }

    private StateNode expand(StateNode leaf, GameModel model){
        List<ActionNode> unvisitedActions = new ArrayList();
        for (GameAction action : GameAction.values()) {
            ActionNode child = leaf.getChildNode(action);

            //aksi yang belum pernah dicoba
            if (child != null && child.getVisitCount() == 0) {
                unvisitedActions.add(child);
            }
        }
        if (unvisitedActions.isEmpty()){
            return leaf;
        } else {
            return unvisitedActions.get(rand.nextInt(unvisitedActions.size())).simulateAction(model);
        }
    }
}
