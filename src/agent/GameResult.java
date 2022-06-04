package agent;

import game.GameAction;
import game.GameModel.GameState;

/**
 * Kelas yang merepresentasikan hasil permainan. Bisa beruba hasil akhir
 * permainan atau hasil dari satu langkah permainan saja.
 *
 * @author Jiang Han
 */
public class GameResult {

    public final long score;
    public final GameState state;
    //Aksi yang dilakukan sebelum mencapai state terakhir
    public final GameAction precedingAction;

    public GameResult(long score) {
        this.score = score;
        state = null;
        precedingAction = null;
    }

    public GameResult(GameAction action, GameState state) {
        this.state = state;
        this.score = state.getScore();
        this.precedingAction = action;
    }
}
