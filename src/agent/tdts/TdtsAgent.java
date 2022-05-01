package agent.tdts;

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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Agen Temporal Difference Tree Search dengan algoritma Sarsa-UCT(lambda)
 *
 * @author Jiang Han
 */
public class TdtsAgent extends GamePlayingAgent {

    private final double EXPLORATION_CONSTANT;
    private final BestChildPolicy BEST_CHILD_POLICY;
    private final NormalizationPolicy NORMALIZATION_POLICY;
    //gamma
    private final double REWARD_DISCOUNT;
    //lambda
    private final double ELIGIBILITY_TRACE_DECAY;

    public TdtsAgent(
            double explorationConstant,
            BestChildPolicy bestChildPolicy,
            NormalizationPolicy NORMALIZATION_POLICY,
            double rewardDiscount,
            double eligibilityTraceDecay) {
        this.EXPLORATION_CONSTANT = explorationConstant;
        this.BEST_CHILD_POLICY = bestChildPolicy;
        this.NORMALIZATION_POLICY = NORMALIZATION_POLICY;
        this.REWARD_DISCOUNT = rewardDiscount;
        this.ELIGIBILITY_TRACE_DECAY = eligibilityTraceDecay;
    }

    public static class Builder {

        private double explorationConstant;
        private BestChildPolicy bestChildPolicy;
        private NormalizationPolicy normalizationPolicy;
        private double gamma, lambda;

        public Builder() {
            //set default values
            explorationConstant = Math.sqrt(2);
            bestChildPolicy = new MostVisitPolicy();
            normalizationPolicy = new SpaceLocalNormalization();
            lambda = gamma = 1;
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

        public Builder setRewardDiscount(double rewardDiscount) {
            this.gamma = rewardDiscount;
            return this;
        }

        public Builder setEligibilityTraceDecay(double eligibilityTraceDecay) {
            this.lambda = eligibilityTraceDecay;
            return this;
        }

        public TdtsAgent build() {
            return new TdtsAgent(
                    explorationConstant,
                    bestChildPolicy,
                    normalizationPolicy,
                    gamma,
                    lambda
            );
        }
    }

    private final Random rand = new Random();

    @Override
    public GameAction selectAction(GameModel.GameState state, GameModel model) {
        NORMALIZATION_POLICY.resetNormalizationBound();

        StateNode root = new TdtsStateNode(state, null);
        while (model.isUsable()) {
            StateNode leaf = select(root, model);
            StateNode child = expand(leaf, model);
            Stack<GameResult> simulatedTrajectory = simulate(child, model);
            backPropagate(child, simulatedTrajectory);
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

                int nCurr = root.getVisitCount();
                int nChild = child.getVisitCount();
                //menghitung nilai UCB1
                double exploitationComp = NORMALIZATION_POLICY.getNormalizedUtility(child);
                assert exploitationComp <= 1.0;
                double explorationComp = EXPLORATION_CONSTANT
                        * Math.sqrt(Math.log(nCurr) / nChild);
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
     * @param startingNode Node dimulainya simulasi (didapatkan dari method
     * expand().
     * @param model Forward model dari game ini
     * @return Stack berisi GameResult yang mencatat setiap aksi, perubahan
     * state, dan riwayat skor dalam simulasi. State paling dalam di stack ini
     * adalah state yang direpresentasikan oleh startingNode.
     */
    private Stack<GameResult> simulate(StateNode startingNode, GameModel model) {
        Stack<GameResult> trajectory = new Stack<>();
        GameModel.GameState currentState = startingNode.state;
        trajectory.push(new GameResult(startingNode.parent.action, currentState));

        while (!currentState.isTerminal() && model.isUsable()) {
            List<GameAction> validActions = currentState.getAvailableActions();
            int actionIdx = rand.nextInt(validActions.size());
            currentState = model.applyAction(currentState, validActions.get(actionIdx));
            trajectory.push(new GameResult(validActions.get(actionIdx), currentState));
        }

        return trajectory;
    }

    /**
     * Memperbarui informasi utilitas yang dicatat dalam pohon permainan
     * berdasarkan hasil selection() dan simulation().
     *
     * @param Leaf StateNode tempat simulasi dimulai.
     * @param simulatedTrajectory Catatan riwayat pemilihan aksi, perpindahan
     * state, dan riwayat skor selama simulasi. Hasil terdalam adalah state yang
     * direpresentasikan oleh node leaf.
     */
    private void backPropagate(StateNode leaf, Stack<GameResult> simulatedTrajectory) {
        double cumulativeDelta = 0;
        double nextValue = 0; //Q_next(s_t, a)
        double nextScore = simulatedTrajectory.pop().score;

        //Unmemorized space update
        while (!simulatedTrajectory.isEmpty()) {
            GameResult currentResult = simulatedTrajectory.pop();
            double reward = nextScore - currentResult.score; //harusnya dikurang skor 1 state sebelumnya

            double currentValue = 0; //Use V_playout here is needed
            double delta = reward + REWARD_DISCOUNT * nextValue - currentValue;
            cumulativeDelta = ELIGIBILITY_TRACE_DECAY * REWARD_DISCOUNT * cumulativeDelta + delta;

            nextValue = currentValue;
            nextScore = currentResult.score;

            NORMALIZATION_POLICY.updateNormalizationBound(cumulativeDelta);
        }

        //Memorized space update
        while (true) {
            leaf.incrementVisitCount();
            if (leaf.parent == null) { //sudah sampai ke root
                break;
            } else {
                leaf.parent.incrementVisitCount();
                
                double currentScore = leaf.parent.parent.state.getScore();
                double reward = nextScore - currentScore;
                double currentValue = leaf.parent.getUtility();
                double delta = reward + REWARD_DISCOUNT * nextValue - currentValue;
                cumulativeDelta = ELIGIBILITY_TRACE_DECAY * REWARD_DISCOUNT * cumulativeDelta + delta;
                
                leaf.parent.updateUtility(cumulativeDelta);
                NORMALIZATION_POLICY.updateNormalizationBound(cumulativeDelta);
                
                nextScore = currentScore;
                nextValue = currentValue;
                leaf = leaf.parent.parent;
            }
        }
    }

    @Override
    public String getConfigurationString() {
        return String.format(
                "Exploration constant: %f%n"
                + "Reward discount rate (gamma): %f%n"
                + "Eligibility trace decay rate (lambda): %f%n"
                + "Best-child policy: %s%n"
                + "Normalization policy: %s",
                EXPLORATION_CONSTANT,
                REWARD_DISCOUNT,
                ELIGIBILITY_TRACE_DECAY,
                BEST_CHILD_POLICY,
                NORMALIZATION_POLICY
        );
    }
    
}
