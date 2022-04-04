package controller;

import agent.GamePlayingAgent;
import agent.mcts.MctsAgent;
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
    
    public static void runMCTSDetailed(int MAX_TICK, double EXP_CONST){
        GameModel infModel = new GameModel(Integer.MAX_VALUE);
        GamePlayingAgent agent = new MctsAgent(EXP_CONST);
        GameState state = infModel.generateInitialState();
        
        ExperimentLogger logger = new ExperimentLogger(String.format("MCTS UCT Agent%nMax Tick: %d%nCp: %f%n", MAX_TICK, EXP_CONST));
        
        logger.log(state);
        long startTime, endTime, totalWriteTime = 0, total;

        do {
            GameState copyState = state.copy();
            startTime = System.currentTimeMillis();
            GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
            endTime = System.currentTimeMillis();

            state = infModel.applyAction(state, chosenAction);
            System.out.println("Duration: " + (endTime - startTime) + " ms");
            startTime = System.currentTimeMillis();
            logger.log(chosenAction, state);
            endTime = System.currentTimeMillis();
            System.out.println("Write time: " + (endTime - startTime) + " ms");

            System.out.println("Score: " + state.getScore());
        } while (!state.isTerminal());
        Toolkit.getDefaultToolkit().beep();
    }
    
    public static void getMCTSAverageScore(int iteration, int MAX_TICK, double EXP_CONST){
        ExperimentLogger logger = new ExperimentLogger(String.format("MCTS UCT Agent (Average)%n"
                + "Iteration: %d%n"
                + "Max Tick: %d%n"
                + "Cp: %f%n", iteration, MAX_TICK, EXP_CONST));
        long totalScore = 0;
        for(int i = 0; i<iteration; i++){
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            GamePlayingAgent agent = new MctsAgent(EXP_CONST);
            GameState state = infModel.generateInitialState();

            long startTime, endTime;

            startTime = System.currentTimeMillis();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(MAX_TICK));
                state = infModel.applyAction(state, chosenAction);
            } while (!state.isTerminal());
            endTime = System.currentTimeMillis();
            System.out.println("Iteration[" + (i+1) + "] Final Score: " + state.getScore());
            System.out.println("Time: " + (endTime-startTime) + " ms");
            logger.log("Iteration[" + (i+1) + "] Final Score: " + state.getScore());
            totalScore += state.getScore();
        }
        System.out.println("Average: " + ((double) totalScore / iteration));
        logger.log("Average: " + ((double) totalScore / iteration));
        Toolkit.getDefaultToolkit().beep();
    }
}
