/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi;

/**
 *
 * @author wizardus
 */
public abstract class Evaluator {
    /**
     * The objectives for the first chromosome in the last call to isFitter().
     */
    public double[] objectives1;
    /**
     * The objectives for the 2nd chromosome in the last call to isFitter().
     */
    public double[] objectives2;

    public Evaluator() {

    }

    public abstract double[] getFitness(Chromosome chrom);
    /**
     * Used to determine whether an objective is more optimal when higher
     * or more optimal when lower.
     * @return a boolean array; if an element is true, the corresponding
     * objective is more optimal when higher.
     */
    public abstract boolean[] getOptimal();

    /**
     * Tests whether a chromosome is more fit than another.
     * @param chrom1 the first chromosome.
     * @param chrom2 the second chromosome.
     * @return true if chrom1 is more fit than chrom2, false otherwise.
     */
    public boolean isFitter(Chromosome chrom1, Chromosome chrom2) {
        objectives1 = chrom1.getObjectives();
        objectives2 = chrom2.getObjectives();

        if(objectives1 == null) {
            chrom1.setObjectives(getFitness(chrom1));
            objectives1 = chrom1.getObjectives();
        }

        if(objectives2 == null) {
            chrom2.setObjectives(getFitness(chrom2));
            objectives2 = chrom2.getObjectives();
        }

        if(objectives1.length != objectives2.length) {
            throw new RuntimeException
                    ("Inconsistent objective lengths encountered.");
        }

        boolean[] optimal = getOptimal();

        if(optimal.length != objectives1.length) {
            throw new RuntimeException
                    ("Optimal array is not the same length as objectives.");
        }

        for(int i = 0; i < objectives1.length; i++) {
            if(optimal[i]) {
                if(objectives1[i] < objectives2[i]) {
                    return false;
                }
            } else {
                if(objectives1[i] > objectives2[i]) {
                    return false;
                }
            }
        }

        return true;
    }

    public boolean isEquivalent(Chromosome chrom1, Chromosome chrom2) {
        double[] o1 = chrom1.getObjectives();
        double[] o2 = chrom2.getObjectives();

        if(o1 == null) {
            chrom1.setObjectives(getFitness(chrom1));
            o1 = chrom1.getObjectives();
        }

        if(o2 == null) {
            chrom2.setObjectives(getFitness(chrom2));
            o2 = chrom2.getObjectives();
        }

        for(int i = 0; i < o1.length; i++) {
            if(Math.abs(o1[i] - o2[i]) > .0000001) {
                return false;
            }
        }

        return true;
    }
}
