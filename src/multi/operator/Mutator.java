/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.operator;

import java.util.Vector;

import multi.Chromosome;
import multi.Gene;
import multi.Population;
/**
 *
 * @author wizardus
 */
public class Mutator implements Operator {
    double rate;
    boolean replace;

    /**
     * Initializes a new mutation operator that appends
     * children to the population.
     * @param _rate mutation rate (0 = never, 1 = always).
     */
    public Mutator(double _rate) {
        rate = _rate;
        replace = false;
    }

    /**
     * Initializes a new mutation operator.
     * @param _rate mutation rate.
     * @param _replace true if operator should replace parent
     * with the child.
     */
    public Mutator(double _rate, boolean _replace) {
        rate = _rate;
        replace = _replace;
    }

    public void apply(Vector<Chromosome> chroms) {
        Vector<Chromosome> mutated = new Vector<Chromosome>();
        int mutations = (int) (rate * chroms.size());

        for(int i = 0; i < mutations; i++) {
            int chrom_index = Population.nextInt(chroms.size());
            Chromosome do_mutate = null;
            do_mutate = (Chromosome) chroms.get(chrom_index).clone();
            
            int gene_index = Population.nextInt(do_mutate.getGenes().size());

            mutateGene(do_mutate.getGene(gene_index));

            if(!replace) {
                mutated.add(do_mutate);
            } else {
                chroms.remove(chrom_index);
                chroms.add(do_mutate);
            }
        }

        for(int i = 0; i < mutated.size(); i++) {
            chroms.add(mutated.get(i));
        }
    }

    private void mutateGene(Gene gene) {
        double percentage = Population.nextDouble() * 2 - 1;
        gene.mutate(percentage);
    }
}
