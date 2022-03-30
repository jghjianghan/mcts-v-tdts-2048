
package controller;

import java.util.List;
import model.Pair;
import model.GameModel;
import model.GameAction;
import view.UI;

/**
 * Controls the flow of the application.
 * Bridges the communication between UI and the models
 * @author Jiang Han
 */
public class GameController {
    private UI ui;
    private GameModel.GameState state;
    private GameModel forwardModel;
    
    public GameController(UI ui){
        this.ui = ui;
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.start(state, this);
    }
    
    public void moveBoard(GameAction move){
        if (forwardModel.applyAction(state, move)){
            if (state.isTerminal()){
                ui.showGameOver();
            }
            ui.displayBoard(state);
        }
    }
    
    public void restartGame(){
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.restart(state);
    }
}
