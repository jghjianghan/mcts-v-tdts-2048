package agent.mcts;

import agent.ActionNode;
import agent.GamePlayingAgent;
import agent.GameResult;
import agent.StateNode;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Game-Playing Agent dengan Algoritma UCT
 *
 * @author Jiang Han
 */
public class MctsAgent extends GamePlayingAgent {

    private final double EXPLORATION_CONSTANT;
    private final Random rand = new Random();
    private double globalLowerBound = Double.POSITIVE_INFINITY;
    private double globalUpperBound = Double.NEGATIVE_INFINITY;

    public static void main(String[] args) {
        System.out.println();
    }

    public MctsAgent() {
        EXPLORATION_CONSTANT = Math.sqrt(2);
    }
    public MctsAgent(double c) {
        EXPLORATION_CONSTANT = c;
    }

    @Override
    public GameAction selectAction(GameState state, GameModel model) {
        StateNode root = new MctsStateNode(state, null);
        while(model.isUsable()){
            StateNode leaf = select(root, model);
            StateNode child = expand(leaf, model);
            GameResult result = simulate(child, model);
            backPropagate(child, result);
        }

        //select best child
        List<GameAction> bestActionList = new ArrayList<>();
        int maxVisit = -1;
        for(GameAction action : root.state.getAvailableActions()){
            ActionNode childAction = root.getChildNode(action);
            if (childAction.getVisitCount() > maxVisit){
                maxVisit = childAction.getVisitCount();
                bestActionList.clear();
                bestActionList.add(childAction.action);
            } else if (childAction.getVisitCount() == maxVisit){
                bestActionList.add(childAction.action);
            }
        }
        return bestActionList.get(rand.nextInt(bestActionList.size()));
    }

    private StateNode select(StateNode root, GameModel model) {
        if (root.state.isTerminal()) {
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
                
                //Aksi tidak valid
                if (child == null)
                    continue;

                int nRoot = root.getVisitCount();
                int nChild = child.getVisitCount();
                //menghitung nilai UCB1
                double exploitationComp = normalizeUtility(child);
                assert exploitationComp <= 1.0;
                if (exploitationComp > 1){
                    normalizeUtility(child);
                }
                double explorationComp = EXPLORATION_CONSTANT * Math.sqrt(Math.log(nRoot) / nChild);
                double value = exploitationComp + explorationComp;

                if (value > bestValue) {
                    bestValue = value;
                    bestChildList.clear();
                    bestChildList.add(child);
                } else if (value == bestValue) {
                    bestChildList.add(child);
                }
            }
            ActionNode bestChild = bestChildList.get(rand.nextInt(bestChildList.size()));
            return select(bestChild.simulateAction(model), model);
        } else {
            return root;
        }
    }

    private StateNode expand(StateNode leaf, GameModel model) {
        if (!model.isUsable() || leaf.state.isTerminal()) {
            return leaf;
        }

        List<ActionNode> unvisitedActions = new ArrayList();
        for (GameAction action : GameAction.values()) {
            ActionNode child = leaf.getChildNode(action);

            //aksi yang belum pernah dicoba
            if (child != null && child.getVisitCount() == 0) {
                unvisitedActions.add(child);
            }
        }

        /**
         * unvisitedActions tidak mungkin kosong. Kalau kosong, berarti action
         * udah visited semua, atau memang terminal state. Terminal state sudah
         * dicek di awal method, dan kalau fully-visited, harusnya belum keluar
         * dari method select().
         */
        return unvisitedActions.get(rand.nextInt(unvisitedActions.size())).simulateAction(model);
    }

    /**
     * Mensimulasikan permainan dengan Random Playout Policy mulai dari starting
     * node hingga mencapai terminal state atau kalau tick model sudah habis.
     *
     * @param startingNode Node dimulainya simulasi
     * @param model Forward model dari game ini
     * @return Skor akhir dari simulasi
     */
    private GameResult simulate(StateNode startingNode, GameModel model) {
        GameState currentState = startingNode.state;
        while (!currentState.isTerminal() && model.isUsable()) {
            List<GameAction> validActions = currentState.getAvailableActions();
            currentState = model.applyAction(currentState, validActions.get(rand.nextInt(validActions.size())));
        }

        return new GameResult(currentState.getScore());
    }

    private void backPropagate(StateNode stateNode, GameResult result) {
        while (true) {
            stateNode.incrementVisitCount();
            if (stateNode.parent == null) { //sudah sampai root
                break;
            } else {
                stateNode.parent.incrementVisitCount();
                stateNode.parent.updateUtility(result);
                updateNormalizationBound(stateNode.parent, result.score);                
                stateNode = stateNode.parent.parent;
            }
        }
    }
    
    private double normalizeUtility(ActionNode node){
//        return node.getUtility();
        double localLower = node.getLowerBound();
        double localUpper = node.getUpperBound();
        
        if (localLower < localUpper){
            return (node.getUtility() - localLower) / (localUpper - localLower);
        } else if (globalLowerBound < globalUpperBound){
            return (node.getUtility() - globalLowerBound) / (globalUpperBound - globalLowerBound);
        } else {
            return node.getUtility();
        }
    }
    
    private void updateNormalizationBound(ActionNode node, double value){
        globalLowerBound = Math.min(globalLowerBound, value);
        globalUpperBound = Math.max(globalUpperBound, value);
    }
}
