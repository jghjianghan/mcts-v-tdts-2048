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

        public int getCell(int row, int col){
            return board[row][col].value;
        }
        
        private void setCell(int row, int col, int value){
            board[row][col].value = value;
        }
        
        public int getScore() {
            return score;
        }

        private void setScore(int score) {
            this.score = score;
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
    
    
    /*
    Dalam matrix:
    [ 0][ 1][ 2][ 3]
    [ 4][ 5][ 6][ 7]
    [ 8][ 9][10][11]
    [12][13][14][15]
   
    Dalam kode:
    [15][14][13][12][11]...[2][1][0] (masing-masing 4 bit)
    */
    
    private static int lastNewPos;
    private static int lastNewValue;
    
    /**
     * Converts board into a printable string
     * @param code The board in coded format
     * @return The formatted string of the board
     */
    public static String print(long code){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i<16; i++){
            sb.append((code & 0xf));
            if (i%4==3)
                sb.append('\n');
            else
                sb.append('\t');
            code >>>= 4;
        }
        return sb.toString();
    }
    
    /**
     * Given a board as 4x4 integer array, codes the board into a long variable.
     * @param board 4x4 integer array of the board (elements should be in range [0,15])
     * @return The coded result of the board
     */
    public static long encode(int[][] board){
        long code = 0;
        for(int i = 3; i>=0; i--){
            for (int j = 3; j >= 0; j--) {
                code <<= 4;
                code |= board[i][j];
            }
        }
        return code;
    }
    
    /**
     * Given a board data in coded format, returns it's 4x4 integer array format
     * @param code The coded format of the board
     * @return A 4x4 integer array representation of the board
     */
    public static int[][] decode(long code) {
        int board[][] = new int[4][4];
        for(int i = 0; i<16; i++){
            board[i/4][i%4] = (int)(code & 0xf);
            code >>>= 4;
        }
        return board;
    }
    
    /**
     * Gets the exponent value at some particular position on a board from its coded representation
     * @param code The coded representation of the board
     * @param pos The position in the board (0-15). 0 is top-left, 15 is bottom right
     * @return The exponent of the position. Zero exponent means the position is empty
     */
    public static int getValueAt(long code, int pos){
        return (int)((code & (0xfl << (pos << 2))) >>> (pos << 2) );
    }
    
    /**
     * Sets the exponent value at some particular position on a board from its coded representation
     * @param code The coded representation of the board
     * @param pos The position in the board (0-15). 0 is top-left, 15 is bottom right
     * @param value The new value to store
     * @return The new code
     */
    public static long setValueAt(long code, int pos, int value){
        return (code & (~(0xfl << (pos << 2)))) | ((long)value << (pos<<2));
    }

    /**
     * Generates the starting board (initial state) for the game.
     * Game starts with 2 tiles (with probability of 90% valued 2 and 10% valued 4) on random positions.
     * @return The initial board
     */
    public static long generateInitialBoard() {
        Random rand = new Random();
        int pos1 = rand.nextInt(16);
        int pos2 = pos1;
        while(pos2 == pos1){
            pos2 = rand.nextInt(16);
        }
        int value1 = (Math.random() >= 0.9) ? 2 : 1;
        int value2 = (Math.random() >= 0.9) ? 2 : 1;
        
        return setValueAt(setValueAt(0, pos1, value1), pos2, value2);
    }
    
    /**
     * Checks whether a particular move is valid in a game state.
     * A move is consider valid if and only if it changes the game state.
     * In other words, some times must change position or change value (due to merging)
     * @param code Current game state
     * @param move The move to be checked
     * @return Whether the move valid in current state
     */
    public static boolean isMoveValid(long code, GameAction move){
        switch(move){
            case LEFT:
                for(int i = 0; i<4; i++){
                    for (int j = 1; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int leftExp = getValueAt(code, (i<<2) + (j-1));
                            if (leftExp == 0 || leftExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case RIGHT:
                for(int i = 0; i<4; i++){
                    for (int j = 0; j < 3; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int rightExp = getValueAt(code, (i<<2) + (j+1));
                            if (rightExp == 0 || rightExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case UP:
                for(int i = 1; i<4; i++){
                    for (int j = 0; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int upExp = getValueAt(code, ((i-1)<<2) + j);
                            if (upExp == 0 || upExp == currExp)
                                return true;
                        }
                    }
                }
                break;
            case DOWN:
                for(int i = 0; i<3; i++){
                    for (int j = 0; j < 4; j++) {
                        int currExp = getValueAt(code, (i<<2) + j);
                        if (currExp != 0){
                            int downExp = getValueAt(code, ((i+1)<<2) + j);
                            if (downExp == 0 || downExp == currExp)
                                return true;
                        }
                    }
                }
                break;
        }
        
        return false;
    }

    /**
     * Gets an array of moves that are valid in a particular game state
     * @param code The current game state
     * @return An array of available moves in current state
     */
    public static GameAction[] getAvailableMoves(long code){
        List<GameAction> availableMoves = new ArrayList<>(4);
        
        for (GameAction move : GameAction.values()){
            if (isMoveValid(code, move)){
                availableMoves.add(move);
            }
        }       
        
        return availableMoves.toArray(new GameAction[0]);
    }    
    
    /**
     * Checks whether a game is over or not.
     * In other words, checks whether a game state is a terminal state.
     * Game is considered over when 
     * @param code The game state to check
     * @return Whether the game is over (state is terminal state)
     */
    public static boolean isGameOver(long code){
        return getAvailableMoves(code).length == 0;
    }
    
    /**
     * Simulate a sliding effect on the board (without the addition of new tile).
     * @param code The game state
     * @param move The move to apply on the state
     * @return A pair of score result from the action and the next game state code
     */
    public static Pair<Integer, Long> slideTiles(long code, GameAction move) {
        int point = 0;
        switch(move){
            case LEFT:
                for (int row = 0; row < 4; row++)
                {
                    int col = 1;
                    while (col < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        col++;
                    
                    int pointerPos = row<<2;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col < 4; col++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos++, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, ++pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case RIGHT:
                for (int row = 0; row < 4; row++)
                {
                    int col = 2;
                    while (col >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        col--;
                    
                    int pointerPos = (row<<2) + 3;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; col >= 0; col--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos--, pointerValue + 1);
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                code = setValueAt(code, --pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case UP:
                for (int col = 0; col < 4; col++)
                {
                    int row = 1;
                    while (row < 4 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        row++;
                    
                    int pointerPos = col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row < 4; row++)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos += 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos += 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
            case DOWN:
                for (int col = 0; col < 4; col++)
                {
                    int row = 2;
                    while (row >=0 && getValueAt(code, (row<<2)+col) == 0) // first movable tile
                        row--;
                    
                    int pointerPos = 12 + col;
                    int pointerValue = getValueAt(code, pointerPos);
                    
                    for (; row >= 0; row--)
                    {
                        int currentPos = (row<<2)+col;
                        int currentValue = getValueAt(code, currentPos);
                        if (currentValue != 0)
                        {
                            if (pointerValue == currentValue) //merge
                            {
                                point += 1 << (pointerValue+1);
                                code = setValueAt(code, pointerPos, pointerValue + 1);
                                pointerPos -= 4;
                                code = setValueAt(code, currentPos, 0);
                                pointerValue = getValueAt(code, pointerPos);       
                            }
                            else if (pointerValue == 0) //move to emptyPointer
                            {
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                                code = setValueAt(code, currentPos, 0);
                            }
                            else //move to after emptyPointer
                            {
                                code = setValueAt(code, currentPos, 0);
                                pointerPos -= 4;
                                code = setValueAt(code, pointerPos, currentValue);
                                pointerValue = currentValue;
                            }
                        }
                    }
                }
                break;
        }
        return new Pair<>(point, code);
    }
      
    /**
     * Actually applies a move to a game state (with addition of a new tile)
     * @param code The current game state
     * @param move The move to be applied
     * @return Pair of score caused by the action and the next game state
     */
    public static Pair<Integer, Long> applyMove(long code, GameAction move){
        Pair<Integer, Long> result = slideTiles(code, move);
        
        code = result.getValue();
        
        ArrayList<Integer> emptyCells = new ArrayList<>();
        for(int i = 0; i<16; i++){
            if (getValueAt(code, i) == 0){
                emptyCells.add(i);
            }
        }
        if (emptyCells.isEmpty()){
            return result;
        } else {    
            int value = (Math.random() >= 0.9) ? 2 : 1;
            Random rand = new Random();
            int pos = emptyCells.get(rand.nextInt(emptyCells.size()));
            lastNewPos = pos;
            lastNewValue = value;
            return new Pair<>(result.getKey(), setValueAt(code, pos, value));
        }
    }
}
