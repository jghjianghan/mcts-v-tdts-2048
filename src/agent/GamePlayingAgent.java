package agent;

import game.GameAction;
import game.GameModel;
import game.GameModel.GameState;

/**
 * Kelas abstrak yang merepresentasi sebuah agen permainan (GPA) yang dapat
 * memainkan permainan 2048.
 *
 * @author Jiang Han
 */
public abstract class GamePlayingAgent {

    /**
     * Memilih aksi yang ingin dilakukan pada state permainan tertentu. Agen
     * juga diberikan model permainan yang dapat dipakai untuk melakukan proses
     * tertentu untuk mendukung pemilihan aksinya.
     *
     * @param state State tempat agen berada saat ini
     * @param model Model permainan 2048 yang dapat mensimulasikan perpindahan
     * state saat dilakukan aksi tertentu
     * @return
     */
    abstract public GameAction selectAction(GameState state, GameModel model);

    /**
     * Mengembalikan representasi teks dari konfigurasi parameter agen.
     *
     * @return Representasi teks dari konfigurasi parameter agen
     */
    abstract public String getConfigurationString();
}
