package agent.normalizationPolicy;

import agent.ActionNode;

/**
 * Interface untuk melakukan normalisasi terhadap utilitas dari node action.
 * Class yang meng-implement interface ini harus bisa menyimpan global boundnya
 * sendiri.
 *
 * @author Jiang Han
 */
public interface NormalizationPolicy {

    /**
     * Menghitung nilai normalisasi utilitas suatu simpul aksi.
     *
     * @param node
     * @return
     */
    public double getNormalizedUtility(ActionNode node);

    /**
     * Memperbarui global bound
     *
     * @param value Nilai yang akan dipakai untuk memperbarui batasan
     */
    public void updateNormalizationBound(double value);

    /**
     * Mereset global bound
     */
    public void resetNormalizationBound();

}
