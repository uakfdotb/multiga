/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select;

import java.util.Vector;

import multi.Chromosome;
/**
 *
 * @author wizardus
 */
public interface Selector {
    /**
     * Selects chromosomes from a population.
     * @param chroms a vector containing the chromosomes.
     * @param num the number of chromosomes to select.
     * @return the result vector.
     */
    public Vector<Chromosome> select(Vector<Chromosome> chroms, int num);
}
