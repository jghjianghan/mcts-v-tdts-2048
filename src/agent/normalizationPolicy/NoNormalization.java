package agent.normalizationPolicy;

import agent.ActionNode;

/**
 * Tidak dilakukan normalisasi, utility dari state dikembalikan apa adanya.
 *
 * @author Jiang Han
 */
public class NoNormalization implements NormalizationPolicy {

    @Override
    public double getNormalizedUtility(ActionNode node) {
        return node.getUtility();
    }

    @Override
    public void updateNormalizationBound(double value) {
    }

    @Override
    public void resetNormalizationBound() {
    }

    @Override
    public String toString() {
        return "No Normalization";
    }
}
