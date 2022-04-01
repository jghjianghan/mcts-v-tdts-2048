
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import game.GameModel;
import game.GameModel.GameState;
import game.GameAction;
import game.GameModel.GameState;
import org.junit.Test;
import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class MctsAgentTest {

    @Test
    public void testSelect() {
        GameModel model = new GameModel(10000);
        GameState state = model.generateInitialState();
        
        try {
            Method slideTiles = model.getClass().getDeclaredMethod("slideTiles", GameState.class, GameAction.class);
            slideTiles.setAccessible(true);
            slideTiles.invoke(model, state, GameAction.DOWN);
            Assert.assertEquals(state, state);
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(GameModelTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Test
    public void testExpand() {
    }
    
    @Test
    public void testBackPropagate() {
    }
}
