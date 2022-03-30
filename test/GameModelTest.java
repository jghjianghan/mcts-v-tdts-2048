
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.GameModel;
import model.GameAction;
import model.GameModel.GameState;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class GameModelTest {

    @Test
    public void testAvailableMoves1() {
        int board[][];
        GameModel model = new GameModel();
        GameModel.GameState state;
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 2, 0, 0},
            {0, 0, 2, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        List<GameAction> l1 = state.getAvailableActions();
        List<GameAction> l2 = Arrays.asList(GameAction.values());
        
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(GameAction.values()));

        board = new int[][]{
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},
            {2, 2, 2, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(GameAction.values()));

        board = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 4, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[0]));
    }

    @Test
    public void testAvailableMoves2() {
        int board[][];
        GameModel model = new GameModel();
        GameModel.GameState state;
        
        board = new int[][]{
            {2, 4, 2, 4},
            {4, 8, 8, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT}));

        board = new int[][]{
            {2, 4, 2, 4},
            {8, 8, 4, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT}));

        board = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 8, 8},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT}));

        board = new int[][]{
            {2, 4, 4, 4},
            {4, 2, 4, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(GameAction.values()));

        board = new int[][]{
            {2, 4, 8, 4},
            {4, 2, 8, 2},
            {2, 4, 2, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.UP, GameAction.DOWN}));

        board = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 8, 2},
            {2, 4, 8, 4},
            {4, 2, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.UP, GameAction.DOWN}));

        board = new int[][]{
            {2, 4, 2, 4},
            {4, 2, 4, 2},
            {2, 4, 2, 8},
            {4, 2, 4, 8},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.UP, GameAction.DOWN}));
    }

    @Test
    public void testAvailableMoves3() {
        int board[][];
        GameModel model = new GameModel();
        GameModel.GameState state;
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 2, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.UP}));
        
        board = new int[][]{
            {2, 2, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 2, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 2, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT, GameAction.UP}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 0, 0, 0},
            {2, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.UP, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 2},
            {0, 0, 0, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.UP, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 0, 2},
            {0, 0, 0, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.UP, GameAction.DOWN}));

        board = new int[][]{
            {2, 0, 0, 0},
            {2, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.UP, GameAction.DOWN}));
    }

    @Test
    public void testAvailableMoves4() {
        int board[][];
        GameAction expectedResult[];
        GameModel model = new GameModel();
        GameModel.GameState state;
        
        board = new int[][]{
            {2, 0, 0, 0},
            {4, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.DOWN}));

        board = new int[][]{
            {2, 4, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 2, 4},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 0, 4},
            {0, 0, 0, 2},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.DOWN}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 4},
            {0, 0, 0, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.UP}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 4, 2},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.UP}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {4, 2, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.UP}));

        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 0, 0, 0},
            {4, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT, GameAction.UP}));
    }

    @Test
    public void testAvailableMoves5() {
        int board[][];
        GameAction expectedResult[];
        GameModel model = new GameModel();
        GameModel.GameState state;

        board = new int[][]{
            {2, 0, 0, 0},
            {4, 0, 0, 0},
            {2, 0, 0, 0},
            {4, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.RIGHT}));

        board = new int[][]{
            {0, 2, 0, 0},
            {0, 4, 0, 0},
            {0, 2, 0, 0},
            {0, 4, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT, GameAction.RIGHT}));
        
        board = new int[][]{
            {0, 0, 0, 2},
            {0, 0, 0, 4},
            {0, 0, 0, 2},
            {0, 0, 0, 4},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.LEFT}));
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 4, 2, 4},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.UP}));
        
        board = new int[][]{
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {2, 4, 2, 4},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.UP, GameAction.DOWN}));
        
        board = new int[][]{
            {2, 4, 2, 4},
            {0, 0, 0, 0},
            {0, 0, 0, 0},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        Assert.assertEquals(state.getAvailableActions(), Arrays.asList(new GameAction[]{GameAction.DOWN}));
    }

    @Test
    public void testMoveLeft() {
        int board[][], expectedBoard[][];
        GameModel model = new GameModel();
        GameModel.GameState state, expectedState;
        
        board = new int[][]{
            {2, 0, 0, 2},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {1024, 1024, 64, 0},};
        expectedBoard = new int[][]{
            {4, 0, 0, 0},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {2048, 64, 0, 0},};
        
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4+2048);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.LEFT);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        board = new int[][]{
            {2, 2, 4, 8},
            {4, 0, 4, 4},
            {16, 16, 16, 16},
            {32, 16, 16, 32},};
        expectedBoard = new int[][]{
            {4, 4, 8, 0},
            {8, 4, 0, 0},
            {32, 32, 0, 0},
            {32, 32, 32, 0},};

        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4 + 8 + 64 + 32);
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.LEFT);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMoveRight() {
        int board[][], expectedBoard[][];
        GameModel model = new GameModel();
        GameModel.GameState state, expectedState;
        
        board = new int[][]{
            {2, 0, 0, 2},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {1024, 1024, 64, 0},};

        expectedBoard = new int[][]{
            {0, 0, 0, 4},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {0, 0, 2048, 64},};

        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4+2048);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.RIGHT);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        board = new int[][]{
            {2, 2, 4, 8},
            {4, 0, 4, 4},
            {16, 16, 16, 16},
            {32, 16, 16, 32},};
        expectedBoard = new int[][]{
            {0, 4, 4, 8},
            {0, 0, 4, 8},
            {0, 0, 32, 32},
            {0, 32, 32, 32},};
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4 + 8 + 64 + 32);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.RIGHT);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMoveUp() {
        int board[][], expectedBoard[][];
        GameModel model = new GameModel();
        GameModel.GameState state, expectedState;
        
        board = new int[][]{
            {2, 0, 0, 2},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {1024, 1024, 64, 0},};
        expectedBoard = new int[][]{
            {2, 16, 8, 4},
            {4, 64, 32, 4},
            {2, 1024, 64, 0},
            {1024, 0, 0, 0},};
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.UP);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }

        board = new int[][]{
            {2, 4, 16, 32},
            {2, 0, 16, 16},
            {4, 4, 16, 16},
            {8, 4, 16, 32},};
        expectedBoard = new int[][]{
            {4, 8, 32, 32},
            {4, 4, 32, 32},
            {8, 0, 0, 32},
            {0, 0, 0, 0},};
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4 + 8 + 64 + 32);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.UP);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testMoveDown() {
        int board[][], expectedBoard[][];
        GameModel model = new GameModel();
        GameModel.GameState state, expectedState;
        
        board = new int[][]{
            {2, 0, 0, 2},
            {4, 16, 8, 2},
            {2, 64, 32, 4},
            {1024, 1024, 64, 0},};
        expectedBoard = new int[][]{
            {2, 0, 0, 0},
            {4, 16, 8, 0},
            {2, 64, 32, 4},
            {1024, 1024, 64, 4},};
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.DOWN);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        board = new int[][]{
            {2, 4, 16, 32},
            {2, 0, 16, 16},
            {4, 4, 16, 16},
            {8, 4, 16, 32},};
        expectedBoard = new int[][]{
            {0, 0, 0, 0},
            {4, 0, 0, 32},
            {4, 4, 32, 32},
            {8, 8, 32, 32},};
        state= model.new GameState(board);
        expectedState= model.new GameState(expectedBoard, 4 + 8 + 64 + 32);       
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.DOWN);
            Assert.assertEquals(expectedState, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
