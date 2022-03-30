package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Forward model of the 2048 game.
 * Simulates the mechanism of the game and scoring system.
 * Has a ticking count to limit the number of simulation performed
 * @author Jiang Han
 */
public class GameModel {
    private int tickLeft;
    private Random rand = new Random();
    public static final int BOARD_SIZE = 4;
    
    public GameModel(int tick){
        this.tickLeft = tick;
    }
    
    public class GameState {
        private Cell[][] board;
        private int score;
        public GameState(){
            this(new int[4][4], 0);
        }
        public GameState(int[][] board){
            this(board, 0);
        }
        public GameState(int[][] board, int score){
            assert board.length == 4 && board[0].length == 4;
            this.board = new Cell[4][];
            for(int i = 0; i<4; i++){
                this.board[i] = new Cell[4];
                for (int j = 0; j < 4; j++) {
                    this.board[i][j] = new Cell(board[i][j]);
                }
            }
            this.score = score;
        }

        public int getCellValue(int row, int col){
            return board[row][col].value;
        }
        
        private void setCellValue(int row, int col, int value){
            board[row][col].value = value;
        }
        
        public int getScore() {
            return score;
        }

        private void setScore(int score) {
            this.score = score;
        }
        
        public int getSize(){
            return BOARD_SIZE;
        }
        
        public GameState copy(){
            GameState salinan = new GameState();
            salinan.score = this.score;
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    salinan.board[i][j].value = this.board[i][j].value;
                }
            }
            return salinan;
        }
        
        public boolean isActionValid(GameAction action){
            GameState salinan = this.copy();
            GameModel.slideTiles(salinan, action);
            return !salinan.equals(this);
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            final int modulo = 1000000009;
            
            long hash = 0;
            long primePower = 1;
            for(int i = 0; i<4; i++){
                for (int j = 0; j < 4; j++) {
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
            
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if (other.board[i][j].value != this.board[i][j].value){
                        return false;
                    }
                }
            }
            return true;
        }
        
        public List<GameAction> getAvailableActions(){
            List<GameAction> availableMoves = new ArrayList<>(4);
        
            for (GameAction move : GameAction.values()){
                if (isActionValid(move)){
                    availableMoves.add(move);
                }
            }       

            return availableMoves;
        }
        
        public boolean isTerminal(){
            return getAvailableActions().isEmpty();
        }
        
        private List<Cell> getEmptyCells(){
            List<Cell> emptyCells = new ArrayList<>();
            for(int i = 0; i<4; i++){
                for (int j = 0; j < 4; j++) {
                    if (board[i][j].value == 0){
                        emptyCells.add(board[i][j]);
                    }
                }
            }
            
            return emptyCells;
        }
    }
    
    private static void slideTiles(GameState state, GameAction move) {
        Cell[] line;
        int scoreIncrement = 0;
        switch(move){
            case LEFT:
                for(int i=0; i<4; i++){
                    line = new Cell[4];
                    for(int j = 0; j<4; j++){
                        line[j] = state.board[i][j];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case RIGHT:
                for(int i=0; i<4; i++){
                    line = new Cell[4];
                    
                    for(int j = 0, k = 3; j<4; j++, k--){
                        line[j] = state.board[i][k];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case UP:
                for(int j = 0; j<4; j++){
                    line = new Cell[4];
                    for(int i = 0; i<4; i++){
                        line[i] = state.board[i][j];
                    }
                    scoreIncrement += slideLine(line);
                }
                break;
            case DOWN:
                for(int j = 0; j<4; j++){
                    line = new Cell[4];
                    for(int i = 0, k = 3; i<4; i++, k--){
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
     * @param line Array dari garis papan
     * @return Skor yang didapatkan dari hasil penggabungan saat digeser
     */
    private static int slideLine(Cell[] line){
        assert line.length == 4;
        int score = 0;
        //Join tile yang sama (tidak harus sebelahan)
        int ptr = 0;
        while(ptr<3){
            //Cari cell terdepan yang tidak kosong yang masih boleh bergabung
            while(ptr<3 && line[ptr].value == 0){
                ptr++;
            }
            if (ptr < 3){
                int ptr2 = ptr+1;
                //Cari cell terdepan setelah ptr yang tidak kosong
                while(ptr2 < 4 && line[ptr2].value == 0){
                    ptr2++;
                }
                if (ptr2 < 4){
                    if (line[ptr].value == line[ptr2].value){
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
        while(ptr<3){
            //Cari cell terdepan yang kosong
            while(ptr<3 && line[ptr].value != 0){
                ptr++;
            }
            if (ptr < 3){
                int ptr2 = ptr+1;
                //Cari cell terdepan setelah ptr yang tidak kosong
                while(ptr2 < 4 && line[ptr2].value == 0){
                    ptr2++;
                }
                if (ptr2 < 4){
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
     * 
     * @param state
     * @param move
     * @return True kalau action berhasil di-apply. False kaalau tick sudah habis atau move tidak valid
     */
    public boolean applyAction(GameState state, GameAction move){
        if (tickLeft > 0 && state.isActionValid(move)){
            tickLeft--;
            slideTiles(state, move);
            List<Cell> emptyCells = state.getEmptyCells();
            
            Cell chosen = emptyCells.get(rand.nextInt(emptyCells.size()));
            chosen.value = (Math.random() >= 0.9) ? 4 : 2;
            
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * Generates the starting board (initial state) for the game.
     * Game starts with 2 tiles (with probability of 90% valued 2 and 10% valued 4) on random positions.
     * @return The initial state
     */
    public GameState generateInitialState() {
        GameState state = new GameState();
        List<Cell> cells = state.getEmptyCells();
        
        Cell cell1 = cells.remove(rand.nextInt(cells.size()));
        Cell cell2 = cells.remove(rand.nextInt(cells.size()));
        
        cell1.value = (Math.random() >= 0.9) ? 4 : 2;
        cell2.value = (Math.random() >= 0.9) ? 4 : 2;
        
        return state;
    }
}
