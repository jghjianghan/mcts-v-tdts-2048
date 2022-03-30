
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
    private long board;
    private int score = 0;
    
    public GameController(UI ui){
        this.ui = ui;
        board = GameModel.generateInitialBoard();
        ui.start(GameModel.decode(board), this);
    }
    
    public void moveBoard(GameAction move){
        if (GameModel.isMoveValid(board, move)){
            Pair<Integer, Long> result = GameModel.applyMove(board, move);
            
            board = result.getValue();
            score += result.getKey();
            
            if (GameModel.isGameOver(board)){
                ui.showGameOver();
            }
            ui.displayBoard(GameModel.decode(board), score);
        }
    }
    
    public void restartGame(){
        score = 0;
        board = GameModel.generateInitialBoard();
        ui.restart(GameModel.decode(board));
    }
}
