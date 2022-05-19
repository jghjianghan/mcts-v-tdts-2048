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

    private final Random rand = new Random();

    private MctsAgent(
            double explorationConstant,
            BestChildPolicy bestChildPolicy,
            NormalizationPolicy normalizationPolicy) {
        this.EXPLORATION_CONSTANT = explorationConstant;
        this.BEST_CHILD_POLICY = bestChildPolicy;
        this.NORMALIZATION_POLICY = normalizationPolicy;
    }

    public static class Builder {

        private double explorationConstant;
        private BestChildPolicy bestChildPolicy;
        private NormalizationPolicy normalizationPolicy;

        public Builder() {
            //set default values
            explorationConstant = Math.sqrt(2);
            bestChildPolicy = new MostVisitPolicy();
            normalizationPolicy = new SpaceLocalNormalization();
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

        public MctsAgent build() {
            return new MctsAgent(explorationConstant, bestChildPolicy, normalizationPolicy);
        }
    }

    @Override
    public String getConfigurationString() {
        return String.format(
                "Exploration constant: %f%n"
                + "Best-child policy: %s%n"
                + "Normalization policy: %s",
                EXPLORATION_CONSTANT,
                BEST_CHILD_POLICY,
                NORMALIZATION_POLICY
        );
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

    /**
     * Mengembalikan node state yang memiliki aksi yang belum pernah dikunjungi
     * dari pohon pencarian. Jika tidak ditemukan node yang memenuhi hingga
     * terminal state, maka terminal state itu yang dikembalikan. Pemilihan aksi
     * saat menuruni pohon dilakukan berdasarkan teknik UCB1.
     *
     * @param root Titik awal pemilihan
     * @param model Forward model dari permainan 2048
     * @return Node state yang memiliki unvisted action atau node terminal
     */
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

                double explorationComp = EXPLORATION_CONSTANT
                        * Math.sqrt(Math.log(nRoot) / nChild);
                double ucb1 = exploitationComp + explorationComp;

                if (ucb1 > bestValue) {
                    bestValue = ucb1;
                    bestChildList.clear();
                    bestChildList.add(child);
                } else if (ucb1 == bestValue) {
                    bestChildList.add(child);
                }
            }
            ActionNode bestChild = bestChildList.get(rand.nextInt(bestChildList.size()));
            return select(bestChild.simulateAction(model), model);
        }
    }

    /**
     * "Mengembangkan" sebuah StateNode leaf dari pohon dengan memilih salah
     * satu aksi yang belum pernah dicoba, lalu mengembalikan StateNode yang
     * dihasilkan dari aksi tersebut. StateNode baru tersebut akan menjadi titik
     * awal dilakukannya simulai.
     *
     * @param leaf StateMode yang ingin di-expand
     * @param model Forward model dari permainan 2048
     * @return StateNode baru sebagai titik awal dilakukannya simulasi. Node
     * baru ini dihasilkan dari sebuah aksi di node leaf yang belum pernah, atau
     * node leaf itu sendiri jika leaf adalah state terminal atau jika model
     * sudah tidak usable. dicoba.
     */
    private StateNode expand(StateNode leaf, GameModel model) {
        if (!model.isUsable() || leaf.state.isTerminal()) {
            return leaf;
        }

        List<ActionNode> unvisitedActions = new ArrayList<>();
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

    /**
     * Memperbarui informasi utilitas yang dicatat dalam pohon permainan
     * berdasarkan hasil selection() dan simulation(). Class ini menjacat total
     * skor dan jumlah visit dari simulasi yang melewati setiap ActionState.
     *
     * @param stateNode StateNode leaf tempat simulasi dimulai.
     * @param result Hasil simulasi permainan
     */
    private void backPropagate(StateNode stateNode, GameResult result) {
		NORMALIZATION_POLICY.updateNormalizationBound(result.score);
        while (true) {
            stateNode.incrementVisitCount();
            if (stateNode.parent == null) { //sudah sampai root
                break;
            } else {
                stateNode.parent.incrementVisitCount();
                stateNode.parent.updateUtility(result.score);
                stateNode = stateNode.parent.parent;
            }
        }
    }
}
