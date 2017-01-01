package drops;

import drops.npc.NPCDrop;
import page.WikiConstants;
import utils.AliasMethod;
import utils.PageUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Andrew
 */
public class DropTable {

    private static final DropTable RARE;

    static {
        //setup the rare drop table.
        RARE = new DropTable(1/64); //1/8.4321 is about the correct rarity for zulrah
        RARE.addDrops(WikiConstants.getNPCDrops(PageUtils.getWikiPageSource(WikiConstants.rareDropTableUrl), WikiConstants.rareDropTableName));
        NPCDrop[] blanks = new NPCDrop[10];
        for(int i = 0; i < blanks.length; i++) {
            blanks[i] = NPCDrop.NULL_DROP;
        }
        RARE.addDrops(Arrays.asList(blanks));
        RARE.normalize();
    }

    /**
    The probability of getting a chance to roll on the dropTable.
     May remove this in the future..
     */
    private final double landingProbability;

    private final List<NPCDrop> drops;

    public DropTable(double landingProbability) {
        this.landingProbability = landingProbability;
        drops = new ArrayList<>();
    }

    /**
     * normalizes the probabilities so that they add up to 1.
     * @implNote Should Always be called atleast once before starting to roll on the drop table.
     */
    public void normalize() {
        double sum = 0;
        for(NPCDrop drop : drops)
        {
            sum += drop.probability;
        }
        for(NPCDrop drop : drops) {

            drop.probability /= sum;
        }
    }

    public NPCDrop roll() {
        List<Double> probabilities = new ArrayList<>();
        for(NPCDrop drop : drops) {
            if(drop == null) {
                probabilities.add(DropProbability.SUPER_RARE.probability);
                continue;
            }
            probabilities.add(drop.probability);
        }
        AliasMethod rng = new AliasMethod(probabilities);
        NPCDrop roll = drops.get(rng.next());
        if(roll == NPCDrop.NULL_DROP) {
            roll = null;
        }
        if(roll != null && roll.item.name.equals(WikiConstants.rareDropTableName)) {
            roll = RARE.roll();
        }
        return roll;
    }

    public void addDrop(NPCDrop drop) {
        drops.add(drop);
    }

    private void addDrops(List<NPCDrop> drops) {
        this.drops.addAll(drops);
    }

    public int getNumDrops() {
        return drops.size();
    }
}
