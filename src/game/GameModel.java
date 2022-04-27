package game;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Forward model dari permainan 2048. Mensimulasikan mekanisme permainan dan
 * perhitungan skor Pemakaian objek dari class ini dibatasi oleh nilai tick
 *
 * @author Jiang Han
 */
public class GameModel {

    private int tickLeft;
    private Random rand = new Random();
    public final int BOARD_SIZE = 4;
    //Batas probabilitan untuk menentukan nilai tile baru
    private final double NEW_TILE_PROB_THRES = 0.9;
    //Nilai tile baru yang ditambahkan jika nilai random < threshold
    private final int NEW_TILE_VALUE_PRIMARY = 2;
    //Nilai tile baru yang ditambahkan jika nilai random >= threshold
    private final int NEW_TILE_VALUE_SECONDARY = 4;

    /**
     * Membuat sebuah forward model baru untuk permainan 2048 yang dapat dipakai
     * oleh GPA dengan jumlah pemakaian yang dibatasi oleh tick. *
     *
     * @param tick Batas jumlah pemakaian objek (pemanggilan applyMove())
     */
    public GameModel(int tick) {
        this.tickLeft = tick;
    }

    /**
     * Inner class yang merepresentasikan state permainan
     */
    public final class GameState {

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("Score: ");
            sb.append(score);
            sb.append('\n');

            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    sb.append(board[i][j].value);
                    sb.append('\t');
                }
                sb.append('\n');
            }
            return sb.toString();
        }

        private Cell[][] board;
        private int score;
        private boolean isTerminal;
        /**
         * Array dari aksi yang valid. Kalau aksi ke-i tidak valid, array[i]
         * adalah null. Index adalah id dari enum aksi
         *
         */
        private GameAction validActions[];

        public GameState() {
            this(new int[BOARD_SIZE][BOARD_SIZE], 0);
        }

        public GameState(int[][] board) {
            this(board, 0);
        }

        public GameState(int[][] board, int score) {
            assert board.length == BOARD_SIZE && board[0].length == BOARD_SIZE;
            this.board = new Cell[BOARD_SIZE][];
            for (int i = 0; i < BOARD_SIZE; i++) {
                this.board[i] = new Cell[BOARD_SIZE];
                for (int j = 0; j < BOARD_SIZE; j++) {
                    this.board[i][j] = new Cell(board[i][j]);
                }
            }
            this.score = score;

            evaluateAttributes();
        }

        //Precalculate valid actions and isTerminal
        private void evaluateAttributes() {
            validActions = new GameAction[GameAction.values().length];
            isTerminal = true;
            for (GameAction action : GameAction.values()) {
                //Apakah aksi menyebabkan tile berpindah?
                if (this.isSlideable(action)) {
                    validActions[action.id] = action;
                    //Asal ada satu state yang valid, maka state ini non-terminal
                    isTerminal = false;
                }
            }
        }

        public int getCellValue(int row, int col) {
            return board[row][col].value;
        }

        private void setCellValue(int row, int col, int value) {
            board[row][col].value = value;
        }

        public int getScore() {
            return score;
        }

        private void setScore(int score) {
            this.score = score;
        }

        /**
         * Mengembalikan ukuran papan yang direpresentasikan oleh state. Panjang
         * papan sama dengan lebarnya.
         *
         * @return Ukuran papan
         */
        public int getSize() {
            return BOARD_SIZE;
        }

        /**
         * Membuat salinan dari state ini yang sepenuhnya terpisah (tanpa common
         * reference)
         *
         * @return Salisan dari state ini
         */
        public GameState copy() {
            int copyBoard[][] = new int[BOARD_SIZE][BOARD_SIZE];
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    copyBoard[i][j] = this.board[i][j].value;
                }
            }
            GameState salinan = new GameState(copyBoard);
            salinan.score = this.score;
            return salinan;
        }

        /**
         * Memeriksa apakah suatu aksi valid untuk dilakukan pada state ini.
         * Method ini dipanggil di konstruktor, sedangkan method
         * isActionValid(GameAction) memakai hasil perhitungan dari method ini.
         *
         * @param action Aksi yang ingin dilakukan
         * @return True jika aksi valid dan false jika sebaliknya
         */
        private boolean isSlideable(GameAction action) {
            switch (action) {
                case LEFT:
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        for (int j = 1; j < BOARD_SIZE; j++) {
                            if (isTileMovable(board[i][j].value, board[i][j - 1].value)) {
                                return true;
                            }
                        }
                    }
                    break;
                case RIGHT:
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        for (int j = 0; j < BOARD_SIZE - 1; j++) {
                            if (isTileMovable(board[i][j].value, board[i][j + 1].value)) {
                                return true;
                            }
                        }
                    }
                    break;
                case UP:
                    for (int i = 1; i < BOARD_SIZE; i++) {
                        for (int j = 0; j < BOARD_SIZE; j++) {
                            if (isTileMovable(board[i][j].value, board[i - 1][j].value)) {
                                return true;
                            }
                        }
                    }
                    break;
                case DOWN:
                    for (int i = 0; i < BOARD_SIZE - 1; i++) {
                        for (int j = 0; j < BOARD_SIZE; j++) {
                            if (isTileMovable(board[i][j].value, board[i + 1][j].value)) {
                                return true;
                            }
                        }
                    }
                    break;
            }
            return false;
        }

        /**
         * Memeriksa apakah tile di satu cell bisa berpindah ke cell lain.
         * Diasumsikan kedua tile memang bersebelahan.
         *
         * @param sourceValue Nilai pada cell pertama
         * @param targetValue Nilai pada cell kedua (cell yang dituju)
         * @return True jika tile pada cell pertama bisa berpindah ke cell
         * kedua. False jika tidak ada tile pada cell pertama atau memang tidak
         * bisa berpindah.
         */
        private boolean isTileMovable(int sourceValue, int targetValue) {
            return sourceValue != 0 && (targetValue == 0 || targetValue == sourceValue);
        }

        /**
         * Memeriksa apakah suatu aksi valid untuk dilakukan pada state ini.
         *
         * @param action Aksi yang ingin dilakukan
         * @return True jika aksi valid dan false jika sebaliknya
         */
        public boolean isActionValid(GameAction action) {
            return validActions[action.id] != null;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            final int modulo = 1000000009;

            long hash = 0;
            long primePower = 1;
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    hash = (hash + board[i][j].value * primePower) % modulo;
                    primePower = (primePower * prime) % modulo;
                }
            }
            hash = (hash + score * primePower) % modulo;

            return (int) hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final GameState other = (GameState) obj;
            if (this.score != other.score) {
                return false;
            }

            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    if (other.board[i][j].value != this.board[i][j].value) {
                        return false;
                    }
                }
            }
            return true;
        }

        /**
         * Mengembalikan aksi yang valid untuk dilakukan pada state ini
         *
         * @return List aksi yang valid
         */
        public List<GameAction> getAvailableActions() {
            return Arrays.stream(validActions)
                    .filter(action -> action != null)
                    .collect(Collectors.toList());
        }

        /**
         * Memeriksa apakah state ini adalah terminal state. Terminal state
         * adalah state yang tidak memiliki aksi yang valid lagi.
         *
         * @return True jika state ini adalah terminal state dan False jika
         * sebaliknya
         */
        public boolean isTerminal() {
            return isTerminal;
        }

        /**
         * Mengembalikan list dari cell-cell yang kosong dalam state ini
         *
         * @return List dari cell yang kosong
         */
        private List<Cell> getEmptyCells() {
            return Arrays.stream(board).flatMap(Arrays::stream)
                    .filter(cell -> cell.value == 0)
                    .collect(Collectors.toList());
        }
    
        /**
         * Gets the value of the largest tile is this state.
         * 
         * @return The value of the largest tile
         */
        public int getLargestTile(){
            int maxValue = 0;
            for(int i = 0; i<BOARD_SIZE; i++){
                for (int j = 0; j < BOARD_SIZE; j++) {
                    maxValue = Math.max(maxValue, board[i][j].value);
                }
            }
            return maxValue;
        }
    }

    /**
     * Menggerakan board dalam state sesuai aksi dan menghitung skor yang
     * dihasilkan. Method ini tidak menambahkan tile baru ke papan.
     *
     * @param state
     * @param action
     */
    private void slideTiles(GameState state, GameAction action) {
        Cell[] line;
        int scoreIncrement = 0;
        //Membangun line sesuai arah geser
        switch (action) {
            case LEFT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    line = new Cell[BOARD_SIZE];
                    for (int j = 0; j < BOARD_SIZE; j++) {
                        line[j] = state.board[i][j];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case RIGHT:
                for (int i = 0; i < BOARD_SIZE; i++) {
                    line = new Cell[BOARD_SIZE];

                    for (int j = 0, k = BOARD_SIZE - 1; j < BOARD_SIZE; j++, k--) {
                        line[j] = state.board[i][k];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case UP:
                for (int j = 0; j < BOARD_SIZE; j++) {
                    line = new Cell[BOARD_SIZE];
                    for (int i = 0; i < BOARD_SIZE; i++) {
                        line[i] = state.board[i][j];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case DOWN:
                for (int j = 0; j < BOARD_SIZE; j++) {
                    line = new Cell[BOARD_SIZE];
                    for (int i = 0, k = BOARD_SIZE - 1; i < BOARD_SIZE; i++, k--) {
                        line[i] = state.board[k][j];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
        }
        state.setScore(state.getScore() + scoreIncrement);
    }

    /**
     * Menggeser garis papan permainan dari indeks kecil ke arah indeks besar
     *
     * @param line Array cell yang membentuk garis papan
     * @return Skor yang didapatkan dari hasil penggabungan saat digeser
     */
    private int slideLine(Cell[] line) {
        assert line.length == BOARD_SIZE;
        int score = 0;
        //Join tile yang sama (tidak harus sebelahan)
        int ptr = 0;
        while (ptr < BOARD_SIZE - 1) {
            //Cari cell terdepan yang tidak kosong yang masih boleh bergabung
            while (ptr < BOARD_SIZE - 1 && line[ptr].value == 0) {
                ptr++;
            }
            if (ptr < BOARD_SIZE - 1) {
                int ptr2 = ptr + 1;
                //Cari cell terdepan setelah ptr yang tidak kosong
                while (ptr2 < BOARD_SIZE && line[ptr2].value == 0) {
                    ptr2++;
                }
                if (ptr2 < BOARD_SIZE) {
                    if (line[ptr].value == line[ptr2].value) {
                        line[ptr].value += line[ptr2].value;
                        score += line[ptr].value;
                        line[ptr2].value = 0;
                        ptr = ptr2 + 1;
                    } else {
                        ptr = ptr2;
                    }
                } else {
                    break;
                }
            }
        }

        //Shift tile kalau kosong
        ptr = 0;
        while (ptr < BOARD_SIZE - 1) {
            //Cari cell terdepan yang kosong
            while (ptr < BOARD_SIZE - 1 && line[ptr].value != 0) {
                ptr++;
            }
            if (ptr < BOARD_SIZE - 1) {
                int ptr2 = ptr + 1;
                //Cari cell terdepan setelah ptr yang tidak kosong
                while (ptr2 < BOARD_SIZE && line[ptr2].value == 0) {
                    ptr2++;
                }
                if (ptr2 < BOARD_SIZE) {
                    line[ptr].value = line[ptr2].value;
                    line[ptr2].value = 0;
                    ptr++;
                } else {
                    break;
                }
            }
        }

        return score;
    }

    /**
     * Menerapkan sebuah aksi pada state tertentu. Aksi mengakibatkan tile-tile
     * bergeser atau bergabung, lalu tile baru akan muncul di salah satu tempat
     * kosong.
     *
     * @param state State yang ingin dikenakan aksi
     * @param action Aksi yang ingin dilakukan
     * @return State baru kalau berhasil, null kalau gagal (karena aksi tidak
     * valid atau tick sudah habis)
     */
    public GameState applyAction(GameState state, GameAction action) {
        if (tickLeft > 0 && state.isActionValid(action)) {
            tickLeft--;
            GameState copyState = state.copy();
            slideTiles(copyState, action);
            List<Cell> emptyCells = copyState.getEmptyCells();

            Cell chosen = emptyCells.get(rand.nextInt(emptyCells.size()));
            chosen.value = (Math.random() >= NEW_TILE_PROB_THRES) ? NEW_TILE_VALUE_SECONDARY : NEW_TILE_VALUE_PRIMARY;

            copyState.evaluateAttributes();

            return copyState;
        } else {
            return null;
        }
    }

    /**
     * Mengembalikan sebuah initial state permainan. Permainan diawali dengan 2
     * tile pada posisi acak (90% bernilai 2, 10% bernilai 4).
     *
     * @return Initial state permainan
     */
    public GameState generateInitialState() {
        GameState state = new GameState();
        List<Cell> cells = state.getEmptyCells();

        Cell cell1 = cells.remove(rand.nextInt(cells.size()));
        Cell cell2 = cells.remove(rand.nextInt(cells.size()));

        cell1.value = (Math.random() >= 0.9) ? 4 : 2;
        cell2.value = (Math.random() >= 0.9) ? 4 : 2;

        state.evaluateAttributes();

        return state;
    }

    /**
     * Apakah objek model ini masih dapat dipakai untuk applyAction? Model hanya
     * dapat dipakai jika tick belum habis. *
     */
    public boolean isUsable() {
        return tickLeft > 0;
    }
}
