
import game.GameModel;
import game.GameAction;
import game.GameModel.GameState;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class GameStateTest {

    @Test
    public void testEquals() {
        int board[][];
        GameModel model = new GameModel(100);
        GameModel.GameState state;
        
        board = new int[][]{
            {1, 2, 3, 4},
            {4, 3, 2, 1},
            {5, 6, 7, 8},
            {8, 7, 6, 5},};
        state = model.new GameState(board);
        GameModel.GameState state2 = model.new GameState(board);
        Assert.assertTrue(state.equals(state2));   
    }
    
    @Test
    public void testSlideLine(){
        
    }
    
    @Test
    public void testIsActionValid(){
        int board[][];
        GameModel model = new GameModel(100);
        GameModel.GameState state;
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 2, 0, 0},
            {0, 0, 2, 0},
            {0, 0, 0, 0},};
        state = model.new GameState(board);
        Assert.assertTrue(state.isActionValid(GameAction.LEFT));
        Assert.assertTrue(state.isActionValid(GameAction.RIGHT));
        Assert.assertTrue(state.isActionValid(GameAction.UP));
        Assert.assertTrue(state.isActionValid(GameAction.DOWN));
        
        board = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 4, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state = model.new GameState(board);
        Assert.assertFalse(state.isActionValid(GameAction.LEFT));
        Assert.assertFalse(state.isActionValid(GameAction.RIGHT));
        Assert.assertFalse(state.isActionValid(GameAction.UP));
        Assert.assertFalse(state.isActionValid(GameAction.DOWN));
        
        board = new int[][]{
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},};
        state = model.new GameState(board);
        Assert.assertTrue(state.isActionValid(GameAction.LEFT));
        Assert.assertTrue(state.isActionValid(GameAction.RIGHT));
        Assert.assertTrue(state.isActionValid(GameAction.UP));
        Assert.assertTrue(state.isActionValid(GameAction.DOWN));
        
        board = new int[][]{
            {2, 2, 0, 0},
            {4, 0, 0, 0},
            {4, 8, 0, 0},
            {8, 2, 32, 4},};
        state = model.new GameState(board);
        Assert.assertTrue(state.isActionValid(GameAction.LEFT));
        Assert.assertTrue(state.isActionValid(GameAction.RIGHT));
        Assert.assertTrue(state.isActionValid(GameAction.UP));
        Assert.assertTrue(state.isActionValid(GameAction.DOWN));
    }
    
    @Test
    public void testMaxTile(){
        int board[][];
        GameModel model = new GameModel(100);
        GameModel.GameState state;
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 2, 0},
            {0, 0, 0, 0},};
        
        state = model.new GameState(board);
        Assert.assertEquals(2, state.getLargestTile());
        
        board = new int[][]{
            {4, 8, 4, 8},
            {16, 4, 32, 2},
            {2, 8, 64, 8},
            {4, 2, 16, 4},};
        
        state = model.new GameState(board);
        Assert.assertEquals(64, state.getLargestTile());
    }
}
