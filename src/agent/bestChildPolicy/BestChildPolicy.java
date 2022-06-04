package agent.bestChildPolicy;

import agent.ActionNode;
import agent.StateNode;

/**
 * Interface untuk memilih node action terbaik untuk dilakukan di akhir simulasi
 * atau pembelajaran berdasarkan definisi aksi terbaik tertentu.
 *
 * @author Jiang Han
 */
public interface BestChildPolicy {

    /**
     * Memilih satu simpul aksi terbaik yang dapat dilakukan pada state tertentu
     * 
     * @param root Simpul state yang darinya ingin dipilih simpul aksi terbaik.
     * @return Node action yang dianggap terbaik pada root
     */
    public ActionNode selectBestChild(StateNode root);
}
