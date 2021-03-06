package utils;

import drops.DropProbability;
import javafx.util.Pair;

import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contains methods useful for rates.
 * @author Andrew Fulton
 */
public final class ProbabilityUtils {

    private static Map<String, Double> probabilities = new HashMap<>();

    private ProbabilityUtils() {}

    /**
     * returns the probability contained in the string, if the string contains parenthesis
     * and therefore has an abnormal drop rate, a new drop rate will be created.
     * @param rateString the string containing the ProbabilityUtils.
     * @return BigDecimal the probability represented as a BigDecimal
     * @throws ParseException thrown when the probability couldn't be retrieved for the given rateString.
     */
    public static Pair<Double, Double> getProb(String rateString) throws ParseException{
        double probability = 0, groupProbability = 0;

        if(rateString.contains("(")) {
            //check if the rateString has already been calculated and return it.
            if(probabilities.containsKey(rateString)) probability = probabilities.get(rateString);
            else {
                // wiki has updated format for rarity a little bit. Formats can also be (128; 32) where it is either 1/128 or 1/32

                if (rateString.contains(";"))
                {
                    // just get rid of the lower half, our probabilities should be high.
                    // also the lower half is only if you are wearing a ring of wealth, which we are not.
                    int colonIndex = rateString.indexOf(";");
                    String toRemove = rateString.substring(colonIndex, rateString.indexOf(')'));
                    rateString = rateString.replace(toRemove, "");
                }

                //get the dropRate from the text. the wiki normally uses this format: (1/R), where R is a number.
                String safeRate = rateString.replaceAll(",", "").replaceAll("~", "");
                probability = parseRatio(safeRate.substring(safeRate.indexOf('(') + 1, safeRate.indexOf(')')));
                //the dropRate doesn't exist, so add it to the list for next time
                probabilities.put(rateString, probability);
            }
        } else {
            String splitRate = rateString.split(" ")[0];
            for (DropProbability drop : DropProbability.values()) {
                String dropString = drop.name().split("_")[0];
                if (dropString.equalsIgnoreCase(splitRate)) {
                    groupProbability = drop.probability;
                    probability = groupProbability;
                }
            }
        }
        return new Pair<>(groupProbability, probability);
    }

    /**
     * returns a double ratio
     * @param ratio a String given in the format of: #/#
     * @return a double ratio
     */
    private static double parseRatio(String ratio) {
        if(ratio.contains("/")) {
           String[] split = ratio.split("/");
            /*
             *!! For some Reason parsing the double and immediately dividing by the other parsed double causes it to act like an int.
             */
           double num = Double.parseDouble(split[0]);
           double den = Double.parseDouble(split[1]);
           return num / den;
        } else {
            return Double.parseDouble(ratio);
        }
    }
}
