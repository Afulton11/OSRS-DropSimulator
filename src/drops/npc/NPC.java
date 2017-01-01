package drops.npc;

import drops.DropProbability;
import drops.DropTable;
import org.apache.commons.lang3.StringUtils;
import page.WikiConstants;
import utils.PageUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Andrew
 */
public class NPC {

    private static Random rand;
    static {
        rand = new Random();
    }

    private final DropTable dropTable;
    private final List<NPCDrop> alwaysDrops;
    private final byte rollsPerKill;

    /**
     * Creates a new NPC given an NPC name.
     * @param name String name of the drops.npc
     * @return a new NPC
     * @throws FileNotFoundException thrown if the given name doesnt translate into a correct wiki link.
     */
    public static NPC createNPC(String name)  throws IOException {
        String pagesource = PageUtils.getNPCPageSource(name);
        //parse the number of rolls per kill from the page source.
        int rollsPerKill = 1;
        if(pagesource.contains(String.format("%s gives two rolls per kill", StringUtils.capitalize(name)))) rollsPerKill++;
        return new NPC(WikiConstants.getNPCDrops(pagesource, name), rollsPerKill);
    }

    private NPC(List<NPCDrop> drops, int rollsPerKill) {
        System.out.printf("Rolls per kill: %d\n", rollsPerKill);
        this.rollsPerKill = (byte) rollsPerKill;
        this.alwaysDrops = new ArrayList<>();
        this.dropTable = new DropTable(1);
        for(NPCDrop drop : drops) {
            if(drop.probability == DropProbability.ALWAYS.probability) {
                this.alwaysDrops.add(drop);
                System.out.println("Added to Drop Table 'ALWAYS': " + drop);
            } else {
                dropTable.addDrop(drop);
            }
        }
        //normalize the dropTable as we should be done adding/removing drops to and from it.
        dropTable.normalize();
    }

    public static int i = 0;


    public NPCDrop[] rollDrop() {
        int numDrops = rollsPerKill + alwaysDrops.size();
        List<NPCDrop> drops = new ArrayList<>();
        for(NPCDrop drop : alwaysDrops) {
            drops.add(drop);
        }
        if(numDrops > alwaysDrops.size()) {
                for(int i = alwaysDrops.size(); i < numDrops; i++) {
                    NPCDrop roll = dropTable.roll() ;
                    if(roll != null)
                        drops.add(dropTable.roll());
                }
        }
        return drops.toArray(new NPCDrop[]{});
    }

    public final DropTable getDropTable() {
        return dropTable;
    }
}
