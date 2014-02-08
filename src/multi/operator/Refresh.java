/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.operator;

import java.util.Vector;

import multi.Chromosome;
import multi.Population;
/**
 *
 * @author wizardus
 */
public class Refresh implements Operator {
    double rate;
    boolean replace;

    /**
     * Creates a new refresh operator.
     * This generates new individuals randomly.
     * @param _rate refresh rate.
     */
    public Refresh(double _rate) {
        rate = _rate;
        replace = false;
    }

    /**
     * Creates a new refresh operator.
     * This generates new individuals randomly.
     * @param _rate refresh rate.
     * @param _replace true if operator should randomly replace
     * old individuals in the population with newly generated ones.
     */
    public Refresh(double _rate, boolean _replace) {
        rate = _rate;
        replace = _replace;
    }

    public void apply(Vector<Chromosome> population) {
        Vector<Chromosome> new_chromosomes = new Vector<Chromosome>();
        int generate = (int) (rate * population.size());

        for(int i = 0; i < generate; i++) {
            Chromosome chrom_gen = new Chromosome(population.get(0));
            new_chromosomes.add(chrom_gen);

            if(replace) {
                int random = Population.nextInt(population.size());
                population.remove(random);
            }
        }

        for(int i = 0; i < new_chromosomes.size(); i++) {
            population.add(new_chromosomes.get(i));
        }
    }
}
