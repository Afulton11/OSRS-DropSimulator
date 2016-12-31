import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Andrew on 12/29/2016.
 */
public class ProbProducer {

    private static double COMMON_PROB = 0.65;
    private static double uncommon;
    private static double rare;
    private static double veryRare;
    private static double superRare;
    private static double sum;

    //accuracy of final common prob value.
    private static final double indexer = 0.0000000000000001;

    private static ScheduledExecutorService trackerService;

    private ProbProducer() {}

    public static void main(String[] args) {
        System.out.println("Calculating wiki probabilities...");
        System.out.println("Indexer: " + indexer);
        calcValues();
        sum = calcSum();
        printValues();
        System.out.println("SUM: " + sum);
        System.out.println();
        //progress tracker.
        startTracker();
        while(sum != 1) {
            if(sum > 1) { COMMON_PROB -= indexer; break; }
            if(1 - sum >= 0.00001) COMMON_PROB += indexer * 1_000_000_000;
            else if(1 - sum >= 0.000001) COMMON_PROB += indexer * 100_000_000;
            else if(1 - sum >= 0.0000001) COMMON_PROB += indexer * 10_000_000;
            COMMON_PROB += indexer;
            calcValues();
            sum = calcSum();
        }
        stopTracker();
        calcValues();
        sum = calcSum();
        System.out.println();
        printValues();
        System.out.println("Sum: " + sum);
    }

    private static void calcValues() {
        uncommon = COMMON_PROB / 4;
        rare = uncommon / 4;
        veryRare = rare / 4;
        superRare = veryRare / 4;
    }

    private static void printValues() {
        System.out.printf("\nCOMMON: %1.16f\nUNCOMMON: %1.20f\nRARE: %1.20f\nVERY_RARE: %1.20f\nSUPER_RARE: %1.20f\n",
                COMMON_PROB, uncommon, rare, veryRare, superRare);
    }

    private static double calcSum() {
        return (COMMON_PROB + uncommon + rare + veryRare + superRare);
    }

    private static void startTracker() {
        trackerService = Executors.newScheduledThreadPool(1);
        trackerService.scheduleAtFixedRate(() -> {
            System.out.printf("Progress: %3.5f%%\n", sum  * 100);
        }, 1, 2, TimeUnit.SECONDS);
    }

    private static void stopTracker() {
        System.out.println("Shutting down tracker...");
        trackerService.shutdown();
    }
}
