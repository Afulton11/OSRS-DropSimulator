package utils;

import drops.item.Item;
import drops.npc.NPC;
import drops.npc.NPCDrop;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 12/27/2016.
 */
public class LootUtils {

    private static Random rand;
    /**
     * A list of all the currently loaded npcs.
     */
    private static Map<String, NPC> npcMap;

    private static double killsWanted;
    private static int loopIndex;
    private static ScheduledExecutorService progressService;
    private static Runnable progressCommand;

    private static double progress;
    private static String progressString;
    public static final String progressFinished;

    static {
        rand = new Random();
        npcMap = new HashMap<>();
        loopIndex = 0;
        killsWanted = 1;
        progress = 0;
        progressFinished = "Finished!";
        progressService = Executors.newScheduledThreadPool(1);
        progressCommand = () -> {
            progress = 100 * (double) loopIndex / killsWanted;
            if(progress < 100)
                setProgressString();
            else {
                progressString = progressFinished;
                stopProgressService();
            }
        };
    }


    /**
     * Fetches the npc from the npcMap given the npcName, otherwise creates a new Npc,
     * then kills the npc given the amount of kills and returns the loot.
     * @param npcName the String name of the npc to get loot from.
     * @param kills amount of times to kill the npc
     * @return the loot from killing the npc x times.
     * @throws FileNotFoundException thrown if the given npcName doesn't translate into a wiki url.
     */
    public static Map<Item, MutableLong> getLoot(String npcName, int kills) throws IOException {
        resetProgressVars();
        killsWanted = kills;
        NPC npc;
        if(npcMap.containsKey(npcName))
            npc = npcMap.get(npcName);
        else {
            progressString = "Retrieving Npc...";
            npc = NPC.createNPC(npcName);
            progressString = "Retrieved Npc!";
            npcMap.put(npcName, npc);
        }
        if(npc.getDropTable().getNumDrops() < 1) {
            progressString = "Error: Given Npc doesn't drop any loot!";
            return null;
        }
        startProgressService();
        Map<Item, MutableLong> loot = new HashMap<>();
        for (loopIndex = 0; loopIndex < killsWanted; loopIndex++) {
            NPCDrop[] drops = npc.rollDrop();
            for (NPCDrop drop : drops) {
                if (drop == null) continue;
                if (!loot.containsKey(drop.item)) loot.put(drop.item, new MutableLong(drop.getRandomAmount(rand)));
                else loot.get(drop.item).incrementBy(drop.getRandomAmount(rand));
            }
        }
        return loot;
    }

    public static void printLoot(String npcName, int kills) {
        killsWanted = kills;
        NPC npc;
        try {
            npc = NPC.createNPC(npcName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        System.out.println("NPC: " + npcName);
        System.out.println(String.format("Killing %s %d times...", npcName, kills));
        startProgressService();
        Map<String, MutableLong> total_drops = new HashMap<>();
        for(int i = 0; i < killsWanted; i++) {
            NPCDrop[] drops = npc.rollDrop();
            for(NPCDrop drop : drops) {
                if(drop == null) continue;
                if(!total_drops.containsKey(drop.item.name)) total_drops.put(drop.item.name, new MutableLong(drop.getRandomAmount(rand)));
                else total_drops.get(drop.item.name).incrementBy(drop.getRandomAmount(rand));
            }
        }
        stopProgressService();
        System.out.println("Rare Drop Table Rolls: " + NPC.i);
        System.out.println("========================================================\n\t\t\tTOTAL LOOT\n========================================================\n");
        total_drops.forEach((item, val) -> System.out.println(item + " : " + val));
    }

    private static void resetProgressVars() {
        loopIndex = 0;
        progress = 0;
        setProgressString();
    }

    private static void setProgressString() {
        progressString = String.format("Gathering loot... %3.2f%%", progress);
    }

    public static final String getProgressString() {
        return progressString;
    }

    public static final double getProgress() {
        return progress;
    }

    private static void startProgressService() {
        progressService = Executors.newSingleThreadScheduledExecutor();
        progressService.scheduleAtFixedRate(progressCommand, 0, 1, TimeUnit.MICROSECONDS);
    }

    private static void stopProgressService() {
        progressService.shutdownNow();
    }
}
