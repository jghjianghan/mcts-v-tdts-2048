package controller;

import agent.GamePlayingAgent;
import agent.mcts.MctsAgent;
import agent.normalizationPolicy.SpaceLocalNormalization;
import agent.tdts.TdtsAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import io.ExperimentLogger;
import java.awt.Toolkit;

/**
 *
 * @author Jiang Han
 */
public class Experimentor {

    public static void main(String[] args) {
//        runMCTSDetailed(5000, Math.sqrt(2));
        getMCTSAverageScore(30, 1000000, Math.sqrt(2));
    }

    public static void runMCTSDetailed(int MAX_TICK, double EXP_CONST) {
        GameModel infModel = new GameModel(Integer.MAX_VALUE);
        GamePlayingAgent agent = new MctsAgent.Builder()
                .setExplorationConstant(EXP_CONST)
                .setNormalizationPolicy(new SpaceLocalNormalization())
                .build();
        GameState state = infModel.generateInitialState();

        ExperimentLogger logger = new ExperimentLogger("mcts",
                agent.getConfigurationString() + 
                String.format("Max Tick: %d%n", MAX_TICK)
        );

        logger.log(state);
        long startTime, endTime, totalWriteTime = 0, total;

        do {
            GameState copyState = state.copy();
            startTime = System.currentTimeMillis();
            GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
            endTime = System.currentTimeMillis();

            state = infModel.applyAction(state, chosenAction);
            System.out.println("Duration: " + (endTime - startTime) + " ms");
            logger.log(chosenAction, state);

            System.out.println("Score: " + state.getScore());
        } while (!state.isTerminal());
        Toolkit.getDefaultToolkit().beep();
    }

    public static void getMCTSAverageScore(int iteration, int MAX_TICK, double EXP_CONST) {
        GamePlayingAgent agent = new MctsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setNormalizationPolicy(new SpaceLocalNormalization())
                    .build();
        ExperimentLogger logger = new ExperimentLogger("mcts",
                String.format("Average MCTS score over n experiments%n") +
                agent.getConfigurationString() + 
                String.format("Max Tick: %d%n", MAX_TICK) +
                String.format("Num of Experiment: %d%n", iteration)
        );
        
        long totalScore = 0;
        long totalTime = 0;
        for (int i = 0; i < iteration; i++) {
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            agent = new MctsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setNormalizationPolicy(new SpaceLocalNormalization())
                    .build();
            GameState state = infModel.generateInitialState();

            long startTime, endTime;

            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
                state = infModel.applyAction(state, chosenAction);
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            System.out.println("Iteration[" + (i + 1) + "] Final Score: " + state.getScore());
            System.out.println("Time: " + (endTime - startTime) + " ms");
            
            
            logger.log("Iteration[" + (i + 1) + "] Final Score: " + state.getScore()
                    + "\nTime: " + (endTime - startTime) + " ms\n");
            
            totalScore += state.getScore();
            totalTime += (endTime - startTime);
        }
        logger.logClosingPattern();
        
        System.out.println("Average: " + ((double) totalScore / iteration));
        System.out.println("Total time: " + (totalTime / 1000) + " s");
        System.out.println("Average time per run: " + ((double)totalTime / iteration / 1000) + " s");
        
        logger.log(
                "Average: " + ((double) totalScore / iteration)
                + "\nTotal time: " + (totalTime / 1000) + " s"
                + "\nAverage time per run: " + ((double)totalTime / iteration / 1000) + " s"
        );
        
        Toolkit.getDefaultToolkit().beep();
    }

    public static void runTDTSDetailed(int MAX_TICK, double EXP_CONST, double gamma, double lambda) {
        GameModel infModel = new GameModel(Integer.MAX_VALUE);
        GamePlayingAgent agent = new TdtsAgent.Builder()
                .setExplorationConstant(EXP_CONST)
                .setNormalizationPolicy(new SpaceLocalNormalization())
                .setRewardDiscount(gamma)
                .setEligibilityTraceDecay(lambda)
                .build();
        GameState state = infModel.generateInitialState();

        ExperimentLogger logger = new ExperimentLogger("tdts",
                agent.getConfigurationString() + 
                String.format("Max Tick: %d%n", MAX_TICK)
        );

        logger.log(state);
        long startTime, endTime, totalWriteTime = 0, total;

        do {
            GameState copyState = state.copy();
            startTime = System.currentTimeMillis();
            GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
            endTime = System.currentTimeMillis();

            state = infModel.applyAction(state, chosenAction);
            System.out.println("Duration: " + (endTime - startTime) + " ms");
            logger.log(chosenAction, state);

            System.out.println("Score: " + state.getScore());
        } while (!state.isTerminal());
        Toolkit.getDefaultToolkit().beep();
    }

    public static void getTDTSAverageScore(int iteration, int MAX_TICK, double EXP_CONST, double gamma, double lambda) {
        GamePlayingAgent agent = new TdtsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setNormalizationPolicy(new SpaceLocalNormalization())
                    .setRewardDiscount(gamma)
                    .setEligibilityTraceDecay(lambda)
                    .build();
        ExperimentLogger logger = new ExperimentLogger("tdts_avg",
                String.format("Average TDTS score over n experiments%n") +
                agent.getConfigurationString() + 
                String.format("Max Tick: %d%n", MAX_TICK) +
                String.format("Num of Experiment: %d%n", iteration)
        );
        
        long totalScore = 0;
        long totalTime = 0;
        for (int i = 0; i < iteration; i++) {
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            agent = new TdtsAgent.Builder()
                    .setExplorationConstant(EXP_CONST)
                    .setNormalizationPolicy(new SpaceLocalNormalization())
                    .setRewardDiscount(gamma)
                    .setEligibilityTraceDecay(lambda)
                    .build();
            GameState state = infModel.generateInitialState();

            long startTime, endTime;

            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
                state = infModel.applyAction(state, chosenAction);
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            System.out.println("Iteration[" + (i + 1) + "] Final Score: " + state.getScore());
            System.out.println("Time: " + (endTime - startTime) + " ms");
            
            
            logger.log("Iteration[" + (i + 1) + "] Final Score: " + state.getScore()
                    + "\nTime: " + (endTime - startTime) + " ms\n");
            
            totalScore += state.getScore();
            totalTime += (endTime - startTime);
        }
        logger.logClosingPattern();
        
        System.out.println("Average: " + ((double) totalScore / iteration));
        System.out.println("Total time: " + (totalTime / 1000) + " s");
        System.out.println("Average time per run: " + ((double)totalTime / iteration / 1000) + " s");
        
        logger.log(
                "Average: " + ((double) totalScore / iteration)
                + "\nTotal time: " + (totalTime / 1000) + " s"
                + "\nAverage time per run: " + ((double)totalTime / iteration / 1000) + " s"
        );
        
        Toolkit.getDefaultToolkit().beep();
    }
}