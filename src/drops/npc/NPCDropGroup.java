package drops.npc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andrew
 */
public class NPCDropGroup {

    public double probability;
    private List<NPCDrop> drops;

    private double sumProb;

    public NPCDropGroup(final double probability) {
        this.probability = probability;
        drops = new ArrayList<>();
        sumProb = 0;
    }

    double sum = 0;

    public void normalize() {
        System.out.println("Group normalizing....");

        //get the maximum price of an item in the group.
        int maxPrice = 1;
        for(NPCDrop drop : drops) {
            if(drop.marketPrice > maxPrice) maxPrice = drop.marketPrice;
        }
        //set the new probabilities using the max price.
        for(NPCDrop drop : drops) {
            drop.probability = (maxPrice - drop.marketPrice + 100) * drop.probability;
            sumProb += drop.probability;
        }



//        double whole = 1_000_000;
//        sumProb *= whole;
        drops.forEach(drop -> {
            System.out.println("\titem name: " + drop.item.name + " probability: " + drop.probability);
//            drop.probability *= whole;
            drop.probability /= sumProb;
//            drop.probability = drop.probability;
            System.out.println("\tNew drop probability: " + drop.probability);
            sum += drop.probability;
        });
        System.out.printf("\ndrop sum: %1.4f\n", sum);
        //we are now normalized, the sum should be 1.
        sumProb = 1;
    }

    public void addDrop(NPCDrop drop) {
        drops.add(drop);
    }

    public final List<NPCDrop> getDrops() {
        return drops;
    }

    static double nothing = 0;

    public final NPCDrop roll() {
        double p = Math.random();
        double cumulativeProbability = 0.0;
        for (NPCDrop drop : drops) {
            cumulativeProbability += drop.probability;
            if (Double.compare(p, cumulativeProbability) <= 0) {
                return drop;
            }
        }
        return null;
    }
}
