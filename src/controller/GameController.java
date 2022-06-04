package controller;

import game.GameModel;
import game.GameAction;
import game.GameModel.GameState;
import io.GraphicalUI;

/**
 * Controls the flow of the application. Bridges the communication between UI
 * and the models
 *
 * @author Jiang Han
 */
public class GameController {

    private final GraphicalUI ui;
    private GameModel.GameState state;
    private GameModel forwardModel;

    public GameController(GraphicalUI ui) {
        this.ui = ui;
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.start(state, this);
    }

    /**
     * Memproses aksi permainan yang diinputkan oleh pengguna. Jika aksi valid,
     * maka state baru akan ditampilkan. Jika aksi tidak valid, maka tidak
     * terjadi apa-apa. Jika state yang dicapai adalah terminal state, maka
     * controller mengirim perintah untuk menampilkan notifikasi GAME OVER.
     *
     * @param move Aksi yang diinputkan oleh pengguna
     */
    public void moveBoard(GameAction move) {
        GameState nextState = forwardModel.applyAction(state, move);
        if (nextState != null) {
            state = nextState;
            if (state.isTerminal()) {
                ui.showGameOver();
            }
            ui.displayBoard(state);
        }
    }

    /**
     * Mereset semua atribut ke nilai asalnya untuk merestart permainan.
     */
    public void restartGame() {
        forwardModel = new GameModel(Integer.MAX_VALUE);
        state = forwardModel.generateInitialState();
        ui.restart(state);
    }
}
