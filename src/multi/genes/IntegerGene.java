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
public class IntegerGene implements Gene {
    Integer lower_bound;
    Integer upper_bound;
    Integer int_value;

    /**
     * Constructs an IntegerGene with the specified Integer value.
     * @param integer the value to set the gene.
     * @param lower the lower bound of possible values for this gene.
     * @param upper the upper bound of possible values for this gene.
     */
    public IntegerGene(Integer integer, int lower, int upper) {
        int_value = integer;
        lower_bound = lower;
        upper_bound = upper;
    }

    /**
     * Constructs an IntegerGene with a value from lower bound
     * to upper bound, inclusive.
     * @param lower the lower bound to randomly generate a value.
     * @param upper the upper bound to randomly generate a value.
     */
    public IntegerGene(int lower, int upper) {
        lower_bound = lower;
        upper_bound = upper;

        int range = upper_bound - lower_bound + 1; //because it's inclusive
        int random = Population.nextInt(range);
        int_value = random + lower_bound;
    }
    
    public Object getAllele() {
        return int_value;
    }
    
    public void setAllele(Object obj) {
        if(obj instanceof Integer) {
            int_value = (Integer) obj;
        } else {
            throw new RuntimeException
                    ("Received non-Integer object in setAllele().");
        }
    }

    public int intValue() {
        return int_value.intValue();
    }

    public Gene generate() {
        return new IntegerGene(lower_bound.intValue(),
                upper_bound.intValue());
    }

    public void mutate(double percentage) {
        if(percentage > 0) {
            int max_add = upper_bound - int_value;
            double change = max_add * percentage;
            int mutated = (int) Math.floor(int_value + change);
            setAllele(new Integer(mutated));
        } else if(percentage < 0) {
            int max_subtract = int_value - lower_bound;
            double change = max_subtract * percentage; //percentage will make it negative
            int mutated = (int) Math.floor(int_value + change);
            setAllele(new Integer(mutated));
        }
    }

    public Integer getLowerbound() {
        return lower_bound;
    }

    public void setLowerbound(Integer lower_bound) {
        this.lower_bound = lower_bound;
    }

    public Integer getUpperbound() {
        return upper_bound;
    }

    public void setUpperbound(Integer upper_bound) {
        this.upper_bound = upper_bound;
    }

    public Object clone() {
        return new IntegerGene(int_value.intValue(),
                lower_bound.intValue(), upper_bound.intValue());
    }
}
