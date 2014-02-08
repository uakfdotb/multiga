/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.genes;

import multi.Gene;
import multi.Population;
/**
 *
 * @author wizardus
 */
public class DoubleGene implements Gene {
    Double lower_bound;
    Double upper_bound;
    Double double_value;

    /**
     * Constructs an DoubleGene with the specified Double value.
     * @param Double the value to set the gene.
     * @param lower the lower bound of possible values for this gene.
     * @param upper the upper bound of possible values for this gene.
     */
    public DoubleGene(Double d, double lower, double upper) {
        double_value = d;
        lower_bound = lower;
        upper_bound = upper;
    }

    /**
     * Constructs an DoubleGene with a value greater or equal
     * to the lower bound and less than the upper bound.
     * @param lower the lower bound to randomly generate a value.
     * @param upper the upper bound to randomly generate a value.
     */
    public DoubleGene(double lower, double upper) {
        lower_bound = lower;
        upper_bound = upper;

        double range = upper_bound - lower_bound;
        double random = Population.nextDouble() * range;
        double_value = random + lower_bound;
    }

    public Object getAllele() {
        return double_value;
    }

    public void setAllele(Object obj) {
        if(obj instanceof Double) {
            double_value = (Double) obj;
        } else {
            throw new RuntimeException
                    ("Received non-Double object in setAllele().");
        }
    }

    public double doubleValue() {
        return double_value.doubleValue();
    }

    public double lowerBound() {
        return lower_bound.doubleValue();
    }

    public double upperBound() {
        return upper_bound.doubleValue();
    }

    public Gene generate() {
        return new DoubleGene
                (lower_bound.doubleValue(), upper_bound.doubleValue());
    }

    public void mutate(double percentage) {
        if(percentage > 0) {
            double max_add = upper_bound - double_value;
            double change = max_add * percentage;
            double mutated = double_value + change;

            if(mutated < upper_bound) {
                setAllele(new Double(mutated));
            }
        } else if(percentage < 0) {
            double max_subtract = double_value - lower_bound;
            double change = max_subtract * percentage;
            double mutated = double_value + change;

            if(mutated > lower_bound) {
                setAllele(new Double(mutated));
            }
        }
    }

    public Object clone() {
        return new DoubleGene(double_value.doubleValue(),
                lower_bound.doubleValue(), upper_bound.doubleValue());
    }
}
