package drops.npc;

import drops.DropProbability;
import drops.item.Item;

import java.util.Random;

/**
 * Created by Andrew on 12/27/2016.
 */
public class NPCDrop {

    /**
     * This specific NPCDrop is used only for null/blank values in the Rare drop table.
     */
    public static final NPCDrop NULL_DROP =  new NPCDrop(null, 0, 0, 0, 0, DropProbability.RARE.probability);

    public final Item item;
    public final int maxAmt, minAmt, optionalAmt;
    public final int marketPrice;
    public double probability;

    public NPCDrop(final Item item, final int minAmt, final int maxAmt, final int optionalAmt, final int marketPrice, final double probability) {
        this.item = item;
        this.minAmt = minAmt;
        this.maxAmt = maxAmt;
        this.optionalAmt = optionalAmt;
        this.marketPrice = marketPrice;
        this.probability = probability;
    }

    public int getAmtRange() {
        return maxAmt - minAmt;
    }

    public int getRandomAmount(Random rand) {
        if(optionalAmt > 0 && rand.nextBoolean())
            return optionalAmt;
        else if(getAmtRange() > 0)
            return rand.nextInt(getAmtRange() + 1) + minAmt;
        else
            return minAmt;
    }

    @Override
    public String toString() {
        return "Item: " + item + ", min/max Amount: " + minAmt + "/" + maxAmt + " Worth: " + marketPrice + ", Chance: " + probability;
    }
}
