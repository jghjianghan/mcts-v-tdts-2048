package agent.bestChildPolicy;

import agent.ActionNode;
import agent.StateNode;
import game.GameAction;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Memilih action node dengan jumlah visit terbanyak. Kalau ada beberapa yang
 * sama, dilakukan random tie breaking.
 *
 * @author Jiang Han
 */
public class MostVisitPolicy implements BestChildPolicy {

    Random rand = new Random();

    @Override
    public ActionNode selectBestChild(StateNode root) {
        List<ActionNode> bestActionList = new ArrayList<>();
        int maxVisit = -1;
        for (GameAction action : root.state.getAvailableActions()) {
            ActionNode childAction = root.getChildNode(action);
            if (childAction.getVisitCount() > maxVisit) {
                maxVisit = childAction.getVisitCount();
                bestActionList.clear();
                bestActionList.add(childAction);
            } else if (childAction.getVisitCount() == maxVisit) {
                bestActionList.add(childAction);
            }
        }
        return bestActionList.get(rand.nextInt(bestActionList.size()));
    }

    @Override
    public String toString() {
        return "Robust Child (most visit)";
    }
}
