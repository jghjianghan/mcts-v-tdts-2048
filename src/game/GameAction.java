package game;

/**
 * Enumerasi dari semua aksi permainan. Permainan 2048 hanya hemiliki paling
 * banyak 4 aksi yang valid. Setiap aksi diberi id supaya dapat dipakai sebagai
 * penentu nomor dalam array.
 *
 * @author Jiang Han
 */
public enum GameAction {
    LEFT(0),
    RIGHT(1),
    UP(2),
    DOWN(3);

    public final int id;

    GameAction(int id) {
        this.id = id;
    }
}
