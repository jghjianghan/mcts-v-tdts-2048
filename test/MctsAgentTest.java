
import agent.mcts.MctsAgent;
import game.GameModel;
import game.GameAction;
import game.GameModel.GameState;
import org.junit.Test;
//import org.junit.Assert;

/**
 *
 * @author Jiang Han
 */
public class MctsAgentTest {

    public static void main(String[] args) {
        GameModel model = new GameModel(10000);
        GameState state = model.new GameState(new int[][]{
            {0, 2, 0, 2},
            {2, 4, 8, 4},
            {4, 8, 16, 8},
            {1024, 256, 128, 2},
        });
        MctsAgent gpa = new MctsAgent();
        GameAction[] actionList = GameAction.values();
        int[] counter = new int[actionList.length];
        double[] percentage = new double[actionList.length];
        for(int i = 0; i<1000; i++){
            GameAction chosenAction = gpa.selectAction(state, model);
            counter[chosenAction.id]++;
        }
        
        for(int i = 0; i<actionList.length; i++){
            System.out.println(actionList[i] + ": " + counter[i] + "(" + (counter[i] / 1000.0) + "%)");
        }
    }
    
    public void testSelect() {
        GameModel model = new GameModel(1000);
        GameState state = model.new GameState(new int[][]{
            {0, 2, 0, 0},
            {2, 4, 0, 32},
            {64, 16, 128, 2},
            {512, 256, 0, 128},
        });
        MctsAgent gpa = new MctsAgent();
        GameAction chosenAction = gpa.selectAction(state, model);
        System.out.println(chosenAction);
    }

    @Test
    public void testExpand() {
    }
    
    @Test
    public void testBackPropagate() {
    }
}
