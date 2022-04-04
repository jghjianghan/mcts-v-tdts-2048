
package controller;

import game.GameModel;
import game.GameAction;
import game.GameModel.GameState;
import io.UI;

/**
 * Controls the flow of the application.
 * Bridges the communication between UI and the models
 * @author Jiang Han
 */
public class GameController {
    private final UI ui;
    private GameModel.GameState state;
    private GameModel forwardModel;
    
    public GameController(UI ui){
        this.ui = ui;
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.start(state, this);
    }
    
    public void moveBoard(GameAction move){
        GameState nextState = forwardModel.applyAction(state, move);
        if (nextState != null){
            state = nextState;
            if (state.isTerminal()){
                ui.showGameOver();
            }
//            System.out.println(state);
            ui.displayBoard(state);
        }
    }
    
    public void restartGame(){
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.restart(state);
    }
}
