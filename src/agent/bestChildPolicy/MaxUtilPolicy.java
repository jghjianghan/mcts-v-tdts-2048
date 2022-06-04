package agent.bestChildPolicy;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Memilih action node dengan raw utility (utility yang tidak dinormalisasi)
 * tertinggi. Kalau kandidat ada lebih dari 1, dilakukan random tie breaking.
 *
 * @author Jiang Han
 */
public class MaxUtilPolicy implements BestChildPolicy {

    Random rand = new Random();

    @Override
    public ActionNode selectBestChild(StateNode root) {
        List<ActionNode> bestActionList = new ArrayList<>();
        double maxUtil = -1;
        for (GameAction action : root.state.getAvailableActions()) {
            ActionNode childAction = root.getChildNode(action);
            if (childAction.getUtility() > maxUtil) {
                maxUtil = childAction.getUtility();
                bestActionList.clear();
                bestActionList.add(childAction);
            } else if (childAction.getVisitCount() == maxUtil) {
                bestActionList.add(childAction);
            }
        }
        return bestActionList.get(rand.nextInt(bestActionList.size()));
    }

    @Override
    public String toString() {
        return "Max Child (maximum utility)";
    }
}
