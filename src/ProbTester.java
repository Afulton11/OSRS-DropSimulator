/**
 * Created by Andrew on 12/29/2016.
 */
public class ProbTester {

    private Probability[] probabilities = new Probability[] {
            new Probability(10, 1),
            new Probability(2, 1),
            new Probability(9, 1),
            new Probability(3, 1),
            new Probability(4, 1),
            new Probability(5, 1),
            new Probability(6, 1),
            new Probability(7, 1),
            new Probability(8, 1),
    };


    private ProbTester() {
        probabilities = normalize(probabilities);
        testProb(10000000);
    }

    private void testProb(int pickX) {
        Probability[] picked = new Probability[pickX];
        for(int i = 0; i < pickX; i++) {
            picked[i] = roll();
        }
        //calculate percentages...
        for(Probability num : probabilities) {
            int number = num.number;
            int timesPicked = 0;
            for(Probability prob : picked) {
                if(prob.number == number) {
                    timesPicked++;
                }
            }
            System.out.println(number + " : " + (timesPicked / (double)pickX));
        }
    }

    private Probability roll() {
        double p = Math.random();
        double cumulativeProb = 0.0;
        for(Probability probability : probabilities) {
            cumulativeProb += probability.get();
            if( p <= cumulativeProb ) {
                return probability;
            }
        }
        return null;
    }

    private Probability[] normalize(Probability[] probabilities) {
        double sum = 0;
        for(Probability p : probabilities) {
            sum += p.get();
        }
        for (Probability p : probabilities) {
            p.set(p.get() / sum);
        }
        System.out.println("Sum: " + sum);
        return probabilities;
    }

    private class Probability {

        public int number;
        private double probability;

        public Probability(int num, double probability) {
            this.number = num;
            this.probability = probability;
        }

        public double get() {
            return probability;
        }

        public void set(double probability) {
            this.probability = probability;
        }

        @Override
        public String toString() {
            return String.format("Num: %d, Probability: %1$,.2f", number, probability);
        }
    }

    public static void main(String[] args) {
        new ProbTester();
    }
}
