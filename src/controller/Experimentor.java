package controller;

import agent.GamePlayingAgent;
import agent.RandomAgent;
import agent.bestChildPolicy.MaxUtilPolicy;
import agent.bestChildPolicy.MostVisitPolicy;
import agent.mcts.MctsAgent;
import agent.normalizationPolicy.NoNormalization;
import agent.normalizationPolicy.SpaceLocalNormalization;
import agent.tdts.TdtsAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import io.ExperimentLogger;
import java.awt.Toolkit;
import java.util.Arrays;
import java.util.function.Supplier;
import static util.StatHelper.*;

/**
 * Class untuk menjalankan eksperimen pengujian performa GPA dalam memainkan
 * 2048.
 *
 * @author Jiang Han
 */
public class Experimentor {

    /**
     * Melakukan beberapa kali pengujian untuk agen MCTS dengan algoritma UCT,
     * lalu mencatat hasil pengujian dan rata-rata skornya.
     *
     * @param iteration Jumlah pengujian yang ingin dilakukan.
     * @param MAX_TICK Jumlah langkah waktu yang diberikan ke agen pada setiap
     * pemilihan aksi.
     * @param EXP_CONST Konstanta eksplorasi UCT
     * @param isRobustChild Jika true, agen akan memakai Robust Child sebagai
     * Best-Child Policynya. Sebaliknya, agen akan memakai Max Child.
     * @param isSpaceLocalNorm Jika true, agen akan memakai Space-Local Value
     * Normalization Policy sebagai teknik normalisasinya. Sebaliknya, agen
     * tidak akan memakai metode normalisasi apapun.
     */
    public static void MCTSAverage(
            int iteration,
            int MAX_TICK,
            double EXP_CONST,
            boolean isRobustChild,
            boolean isSpaceLocalNorm) {

        System.out.println("UCT Agent is being tested...");
        Supplier<GamePlayingAgent> agentBuilder = () -> new MctsAgent.Builder()
                .setExplorationConstant(EXP_CONST)
                .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                .build();

        ExperimentLogger logger = new ExperimentLogger("mcts",
                "Average MCTS score over n experiments",
                agentBuilder.get().getConfigurationString(),
                "Max Tick: " + MAX_TICK,
                "Number of Games: " + iteration
        );

        runExperiment(iteration, MAX_TICK, agentBuilder, logger);
    }

    /**
     * Melakukan beberapa kali pengujian untuk agen TDTS dengan algoritma
     * Sarsa-UCT(lambda), lalu mencatat hasil pengujian dan rata-rata skornya.
     *
     * @param iteration Jumlah pengujian yang ingin dilakukan.
     * @param MAX_TICK Jumlah langkah waktu yang diberikan ke agen pada setiap
     * pemilihan aksi.
     * @param EXP_CONST Konstanta eksplorasi UCT
     * @param gamma Reward discount yang mengurangi nilai dari state yang jauh
     * dari state saat ini.
     * @param lambda Eligibility trace decay rate.
     * @param isRobustChild Jika true, agen akan memakai Robust Child sebagai
     * Best-Child Policynya. Sebaliknya, agen akan memakai Max Child.
     * @param isSpaceLocalNorm Jika true, agen akan memakai Space-Local Value
     * Normalization Policy sebagai teknik normalisasinya. Sebaliknya, agen
     * tidak akan memakai metode normalisasi apapun.
     */
    public static void TDTSAverage(
            int iteration,
            int MAX_TICK,
            double EXP_CONST,
            double gamma,
            double lambda,
            boolean isRobustChild,
            boolean isSpaceLocalNorm) {
        System.out.println("Sarsa UCT(lambda) Agent is being tested...");
        
        Supplier<GamePlayingAgent> agentBuilder = () -> new TdtsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setRewardDiscount(gamma)
                    .setEligibilityTraceDecay(lambda)
                    .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                    .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                    .build();
        
        ExperimentLogger logger = new ExperimentLogger("tdts",
                "Average TDTS score over n experiments",
                agentBuilder.get().getConfigurationString(),
                "Max Tick: " + MAX_TICK,
                "Number of Games: " + iteration
        );

        runExperiment(iteration, MAX_TICK, agentBuilder, logger);
    }

    public static void RandomAverage(int iteration) {
        System.out.println("Random Agent is being tested...");
        ExperimentLogger logger = new ExperimentLogger(
                "random", 
                "Average score of Random GPA",
                "Number of Games: " + iteration
        );

        runExperiment(iteration, Integer.MAX_VALUE, () -> new RandomAgent(), logger);
    }

    private static void runExperiment(
            int iteration,
            int maxTick,
            Supplier<GamePlayingAgent> agentBuilder,
            ExperimentLogger logger) {

        int scores[] = new int[iteration];
        int steps[] = new int[iteration];
        int maxTiles[] = new int[iteration];
        long durations[] = new long[iteration];

        for (int i = 0; i < iteration; i++) {
            logger.nextFile();
            GamePlayingAgent agent = agentBuilder.get();
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            GameModel.GameState state = infModel.generateInitialState();

            logger.log(state);
            int step = 0;
            long startTime, endTime;
            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(maxTick));

                state = infModel.applyAction(state, chosenAction);
                logger.log(chosenAction, state);
                step++;
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            
            steps[i] = step;
            durations[i] = endTime - startTime;

            scores[i] = state.getScore();
            logger.log("Score: " + state.getScore());
            logger.log("Step: " + step);
            logger.log("Duration: " + durations[i] + " ms");

            System.out.println("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + durations[i] + " ms)");
            logger.logSummary("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + durations[i] + " ms)");

            maxTiles[i] = state.getLargestTile();
            logger.log("Max Tile: " + maxTiles[i]);
        }
        double avgScore = average(scores);
        System.out.println("Average Score: " + avgScore);
        logger.logSummary("Average Score: " + avgScore);

        double avgStep = average(steps);
        System.out.println("Average Step: " + avgStep);
        logger.logSummary("Average Step: " + avgStep);
        
        long totalTime = Arrays.stream(durations).sum();
        System.out.println("Total time: " + (totalTime/1000.0) + " s");
        logger.logSummary("Total time: " + (totalTime/1000.0) + " s");
        System.out.println("Average time per game: " + (totalTime/1000.0/iteration) + " s");
        logger.logSummary("Average time per game: " + (totalTime/1000.0/iteration) + " s");

        int maxScoreId = idOfMaximum(scores);
        System.out.println("Max Score: " + scores[maxScoreId] + " (Iteration[" + (maxScoreId + 1) + "])");
        logger.logSummary("Max Score: " + scores[maxScoreId] + " (Iteration[" + (maxScoreId + 1) + "])");

        int minScoreId = idOfMinimum(scores);
        System.out.println("Min Score: " + scores[minScoreId] + " (Iteration[" + (minScoreId + 1) + "])");
        logger.logSummary("Min Score: " + scores[minScoreId] + " (Iteration[" + (minScoreId + 1) + "])");

        int maxTileId = idOfMaximum(maxTiles);
        System.out.println("Max Tile: " + maxTiles[maxTileId] + " (Iteration[" + (maxTileId + 1) + "])");
        logger.logSummary("Max Tile: " + maxTiles[maxTileId] + " (Iteration[" + (maxTileId + 1) + "])");

        Toolkit.getDefaultToolkit().beep();
    }
}
