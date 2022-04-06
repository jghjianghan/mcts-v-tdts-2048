package agent.bestChildPolicy;

import agent.ActionNode;
import agent.StateNode;

/**
 * Interface untuk memilih node action terbaik untuk dilakukan di akhir simulasi
 * atau pembelajaran.
 *
 * @author Jiang Han
 */
public interface BestChildPolicy {

    public ActionNode selectBestChild(StateNode root);
}
