package controller;

import agent.GamePlayingAgent;
import agent.RandomAgent;
import agent.mcts.MctsAgent;
import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;
import io.ExperimentLogger;

/**
 *
 * @author Jiang Han
 */
public class Experimentor {

    public static void main(String[] args) {
        GameModel infModel = new GameModel(Integer.MAX_VALUE);
        GamePlayingAgent agent = new MctsAgent();
        GameState state = infModel.generateInitialState();
        ExperimentLogger logger = new ExperimentLogger();
        logger.log(state);

        do {
            GameState copyState = state.copy();
            long startTime = System.currentTimeMillis();
            GameAction chosenAction = agent.selectAction(copyState, new GameModel(1000000));
            long endTime = System.currentTimeMillis();

            state = infModel.applyAction(state, chosenAction);
            System.out.println("Duration: " + (endTime - startTime) + " ms");
            logger.log(chosenAction, state);

            System.out.println("Score: " + state.getScore());
        } while (!state.isTerminal());

    }
}
