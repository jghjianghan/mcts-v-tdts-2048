package controller;

import agent.GamePlayingAgent;
import agent.RandomAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;

/**
 * Tester for RandomAgent
 * 
 * @author Jiang Han
 */
public class RandomExperiment {
    public static void main(String[] args) {
//        Result
//        Average Score ~ 1090
//        Average step ~ 118
        averageAgentScore(10000);
    }
    
    public static void averageAgentScore(int iteration){
        long totalScore = 0;
        long step = 0;
        System.out.println("Random Agent");
        for (int i = 0; i < iteration; i++) {
            GamePlayingAgent agent = new RandomAgent();
            GameModel infModel = new GameModel(Integer.MAX_VALUE);
            GameModel.GameState state = infModel.generateInitialState();
            do {
                GameState copyState = state.copy();
                GameAction chosenAction = agent.selectAction(copyState, new GameModel(100000));
                
                state = infModel.applyAction(state, chosenAction);
                step++;
            } while (!state.isTerminal());
            totalScore += state.getScore();
            System.out.println("Score: " + state.getScore());
        }
        System.out.println("Average Score: " + ((double) totalScore / iteration));
        System.out.println("Average Step: " + ((double) step / iteration));
    }
}
