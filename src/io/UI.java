package io;

import controller.GameController;
import game.GameModel;

/**
 * Interface for the user interface of the game
 * @author Jiang Han
 */
public interface UI {
    public void start(GameModel.GameState initialState, GameController controller);
    public void displayBoard(GameModel.GameState state);    
    public void showGameOver();
    public void restart(GameModel.GameState initialState);
}