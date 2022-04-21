package controller;

import agent.GamePlayingAgent;
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
    public static void getMCTSAverageScore(
            int iteration,
            int MAX_TICK,
            double EXP_CONST,
            boolean isRobustChild,
            boolean isSpaceLocalNorm) {

        GamePlayingAgent agent = new MctsAgent.Builder()
                .setExplorationConstant(EXP_CONST)
                .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                .build();

        //Todo: ubah metode logging
        ExperimentLogger logger = new ExperimentLogger("mcts",
                "Average MCTS score over n experiments",
                agent.getConfigurationString(),
                "Max Tick: " + MAX_TICK,
                "Num of Experiment: " + iteration
        );

        long totalScore = 0;
        long totalTime = 0;
        for (int i = 0; i < iteration; i++) {
            logger.nextFile();

            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            agent = new MctsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                    .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                    .build();
            GameState state = infModel.generateInitialState();
            logger.log(state);

            long startTime, endTime;

            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
                state = infModel.applyAction(state, chosenAction);
                logger.log(chosenAction, state);
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + elapsedTime + " ms)");

            logger.log("Score: " + state.getScore() + " (" + elapsedTime + " ms)");
            logger.logSummary("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + elapsedTime + " ms)");

            totalScore += state.getScore();
            totalTime += elapsedTime;
        }

        System.out.println("Average: " + ((double) totalScore / iteration));
        System.out.println("Total time: " + (totalTime / 1000) + " s");
        System.out.println("Average time per run: " + ((double) totalTime / iteration / 1000) + " s");

        logger.logSummary(
                "Average: " + ((double) totalScore / iteration)
                + "\nTotal time: " + (totalTime / 1000) + " s"
                + "\nAverage time per run: " + ((double) totalTime / iteration / 1000) + " s"
        );

        Toolkit.getDefaultToolkit().beep();
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
    public static void getTDTSAverageScore(
            int iteration,
            int MAX_TICK,
            double EXP_CONST,
            double gamma,
            double lambda,
            boolean isRobustChild,
            boolean isSpaceLocalNorm) {
        GamePlayingAgent agent = new TdtsAgent.Builder()
                .setExplorationConstant(EXP_CONST)
                .setRewardDiscount(gamma)
                .setEligibilityTraceDecay(lambda)
                .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                .build();

        ExperimentLogger logger = new ExperimentLogger("tdts",
                "Average TDTS score over n experiments",
                agent.getConfigurationString(),
                "Max Tick: " + MAX_TICK,
                "Num of Experiment: " + iteration
        );

        long totalScore = 0;
        long totalTime = 0;
        for (int i = 0; i < iteration; i++) {
            logger.nextFile();

            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            agent = new TdtsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setRewardDiscount(gamma)
                    .setEligibilityTraceDecay(lambda)
                    .setBestChildPolicy(isRobustChild ? new MostVisitPolicy() : new MaxUtilPolicy())
                    .setNormalizationPolicy(isSpaceLocalNorm ? new SpaceLocalNormalization() : new NoNormalization())
                    .build();

            GameState state = infModel.generateInitialState();
            logger.log(state);

            long startTime, endTime;

            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
                state = infModel.applyAction(state, chosenAction);
                logger.log(chosenAction, state);
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            long elapsedTime = endTime - startTime;
            System.out.println("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + elapsedTime + " ms)");

            logger.log("Score: " + state.getScore() + " (" + elapsedTime + " ms)");
            logger.logSummary("Iteration[" + (i + 1) + "] Final Score: " + state.getScore() + " (" + elapsedTime + " ms)");

            totalScore += state.getScore();
            totalTime += elapsedTime;
        }

        System.out.println("Average: " + ((double) totalScore / iteration));
        System.out.println("Total time: " + (totalTime / 1000) + " s");
        System.out.println("Average time per run: " + ((double) totalTime / iteration / 1000) + " s");

        logger.logSummary(
                "Average: " + ((double) totalScore / iteration)
                + "\nTotal time: " + (totalTime / 1000) + " s"
                + "\nAverage time per run: " + ((double) totalTime / iteration / 1000) + " s"
        );

        Toolkit.getDefaultToolkit().beep();
    }
}
