package agent.bestChildPolicy;

import agent.ActionNode;
import agent.StateNode;

/**
 *
 * @author Jiang Han
 */
public interface BestChildPolicy {
    public ActionNode selectBestChild(StateNode root);
}
