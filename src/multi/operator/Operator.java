/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.operator;

import java.util.Vector;

import multi.Chromosome;
/**
 *
 * @author wizardus
 */
public interface Operator {
    /**
     * Apply the operator to the population.
     * @param chromosomes population.
     */
    public void apply(Vector<Chromosome> chromosomes);
}
