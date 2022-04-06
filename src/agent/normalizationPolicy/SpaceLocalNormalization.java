package agent.normalizationPolicy;

import agent.ActionNode;

/**
 * Melakukan normalisasi dengan teknik Space-Local Value Normalization yang
 * digagas oleh Vodipivec. Utility dinormalisasi berdasarkan local boundnya,
 * nilai terkecil dan terbesar yang pernah melewati satu state tersebut. Jika
 * boundhya tidak valid, digunakan global bound, yaitu nilai terbesar dan
 * terkecil dari seluruh simulasi yang ada.
 *
 * @author Jiang Han
 */
public final class SpaceLocalNormalization implements NormalizationPolicy {

    private double globalLowerBound;
    private double globalUpperBound;

    public SpaceLocalNormalization() {
        resetNormalizationBound();
    }

    @Override
    public double getNormalizedUtility(ActionNode node) {
        double localLower = node.getLowerBound();
        double localUpper = node.getUpperBound();

        if (localLower < localUpper) {
            return (node.getUtility() - localLower) / (localUpper - localLower);
        } else if (globalLowerBound < globalUpperBound) {
            return (node.getUtility() - globalLowerBound) / (globalUpperBound - globalLowerBound);
        } else {
            return 0.5;
        }
    }

    @Override
    public void updateNormalizationBound(double value) {
        globalLowerBound = Math.min(globalLowerBound, value);
        globalUpperBound = Math.max(globalUpperBound, value);
    }

    @Override
    public void resetNormalizationBound() {
        globalLowerBound = Double.POSITIVE_INFINITY;
        globalUpperBound = Double.NEGATIVE_INFINITY;
    }

}
