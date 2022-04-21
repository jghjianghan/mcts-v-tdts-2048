package controller;

import agent.GamePlayingAgent;
import agent.RandomAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import io.ExperimentLogger;

/**
 * Tester for RandomAgent
 * 
 * @author Jiang Han
 */
public class RandomExperiment {    
    public static void averageAgentScore(int iteration){
        long totalScore = 0;
        long step = 0;
        System.out.println("Random Agent");
        
        ExperimentLogger logger = new ExperimentLogger("random", "Average score of Random GPA");
        
        for (int i = 0; i < iteration; i++) {
            GamePlayingAgent agent = new RandomAgent();
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            GameModel.GameState state = infModel.generateInitialState();
            
            logger.log(state);
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(100000));
                
                state = infModel.applyAction(state, chosenAction);
                logger.log(chosenAction, state);
                step++;
            } while (!state.isTerminal());
            totalScore += state.getScore();
            System.out.println("Score: " + state.getScore());
            logger.logSummary("Score: " + state.getScore());
            logger.log("Score: " + state.getScore());
            
            logger.nextFile();
        }
        System.out.println("Average Score: " + ((double) totalScore / iteration));
        logger.logSummary("Average Score: " + ((double) totalScore / iteration));
        
        System.out.println("Average Step: " + ((double) step / iteration));
        logger.logSummary("Average Step: " + ((double) step / iteration));
    }
}
