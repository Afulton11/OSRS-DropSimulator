package drops;

import page.WikiConstants;

/**
 * An enum class that contains the most basic dropRates and their rates.
 * @author Andrew
 */
public enum DropProbability {

//    ALWAYS(1.0), COMMON(WikiConstants.COMMON_PROB), UNCOMMON(WikiConstants.COMMON_PROB / 1.5),
//        RARE(WikiConstants.COMMON_PROB / 2.5), VERY_RARE(WikiConstants.COMMON_PROB / 4.5), SUPER_RARE(WikiConstants.COMMON_PROB / 5.5);
    ALWAYS(1.0), COMMON(WikiConstants.COMMON_PROB), UNCOMMON(COMMON.probability / 4.0),
        RARE(UNCOMMON.probability / 4.0), VERY_RARE(RARE.probability / 4.0), SUPER_RARE(VERY_RARE.probability / 4.0);

    public final double probability;
    DropProbability(final double probability) { this.probability = probability; }
}
