package agent.mcts;

import agent.ActionNode;
import agent.GamePlayingAgent;
import agent.GameResult;
import agent.StateNode;
import agent.bestChildPolicy.BestChildPolicy;
import agent.bestChildPolicy.MostVisitPolicy;
import agent.normalizationPolicy.NormalizationPolicy;
import agent.normalizationPolicy.SpaceLocalNormalization;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Game-Playing Agent Monte Carlo Tree Search dengan Algoritma UCT
 *
 * @author Jiang Han
 */
public class MctsAgent extends GamePlayingAgent {

    private final double EXPLORATION_CONSTANT;
    private final BestChildPolicy BEST_CHILD_POLICY;
    private final NormalizationPolicy NORMALIZATION_POLICY;
    private final int MAX_SIMULATION_DEPTH;

    private final Random rand = new Random();
    private double globalLowerBound;
    private double globalUpperBound;

    private MctsAgent(
            double explorationConstant,
            BestChildPolicy bestChildPolicy,
            NormalizationPolicy normalizationPolicy,
            int maxDepth) {
        this.EXPLORATION_CONSTANT = explorationConstant;
        this.BEST_CHILD_POLICY = bestChildPolicy;
        this.NORMALIZATION_POLICY = normalizationPolicy;
        this.MAX_SIMULATION_DEPTH = maxDepth;
    }

    public static class Builder {

        private double explorationConstant;
        private BestChildPolicy bestChildPolicy;
        private NormalizationPolicy normalizationPolicy;
        private int maxDepth;

        public Builder() {
            //set default values
            explorationConstant = Math.sqrt(2);
            bestChildPolicy = new MostVisitPolicy();
            normalizationPolicy = new SpaceLocalNormalization();
            maxDepth = Integer.MAX_VALUE;
        }

        public Builder setExplorationConstant(double explorationConstant) {
            this.explorationConstant = explorationConstant;
            return this;
        }

        public Builder setBestChildPolicy(BestChildPolicy bestChildPolicy) {
            this.bestChildPolicy = bestChildPolicy;
            return this;
        }

        public Builder setNormalizationPolicy(NormalizationPolicy normalizationPolicy) {
            this.normalizationPolicy = normalizationPolicy;
            return this;
        }

        public Builder setMaxDepth(int maxDepth) {
            this.maxDepth = maxDepth;
            return this;
        }

        public MctsAgent build() {
            return new MctsAgent(explorationConstant, bestChildPolicy, normalizationPolicy, maxDepth);
        }
    }

    @Override
    public String getConfigurationString(){
        return String.format("#MCTS Agent%n"
                + "Exploration constant: %f%n"
                + "Best-child policy: %s%n"
                + "Normalization Policy: %s%n"
                + "Max Depth: %d%n"
                , EXPLORATION_CONSTANT, 
                BEST_CHILD_POLICY.getClass().getSimpleName(), 
                NORMALIZATION_POLICY.getClass().getSimpleName(), 
                MAX_SIMULATION_DEPTH);
    }
    
    @Override
    public GameAction selectAction(GameState state, GameModel model) {
        NORMALIZATION_POLICY.resetNormalizationBound();
        
        StateNode root = new MctsStateNode(state, null);
        while (model.isUsable()) {
            StateNode leaf = select(root, model);
            StateNode child = expand(leaf, model);
            GameResult result = simulate(child, model);
            backPropagate(child, result);
        }

        return BEST_CHILD_POLICY.selectBestChild(root).action;
    }

    private StateNode select(StateNode root, GameModel model) {
        if (root.state.isTerminal() || !model.isUsable()) {
            return root;
        } else {
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
                if (child == null) {
                    continue;
                }

                int nRoot = root.getVisitCount();
                int nChild = child.getVisitCount();
                //menghitung nilai UCB1
                double exploitationComp = NORMALIZATION_POLICY.getNormalizedUtility(child);
                assert exploitationComp <= 1.0;
//                if (exploitationComp > 1) {
//                    normalizeUtility(child);
//                }
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
                NORMALIZATION_POLICY.updateNormalizationBound(result.score);
                stateNode = stateNode.parent.parent;
            }
        }
    }
}
