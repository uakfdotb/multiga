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
public class Crossover implements Operator {
    double rate;
    boolean replace;

    /**
     * Initializes a new crossover operator that appends
     * children to the population.
     * @param _rate crossover rate (0 = never, 1 = always).
     */
    public Crossover(double _rate) {
        rate = _rate;
        replace = false;
    }

    /**
     * Initializes a new crossover operator.
     * @param _rate crossover rate.
     * @param _replace true if children should replace
     * parents in the population.
     */
    public Crossover(double _rate, boolean _replace) {
        rate = _rate;
        replace = _replace;
    }

    public void apply(Vector<Chromosome> chroms) {
        //the changed chromosomes
        Vector<Chromosome> result = new Vector<Chromosome>();
        //# crossovers to perform
        int crossovers = (int) Math.round(chroms.size() * rate);

        int index1; //index of first chromosome
        int index2; //index of second chromosome

        for(int i = 0; i < crossovers; i++) {
            //random index
            index1 = Population.nextInt(chroms.size());
            index2 = Population.nextInt(chroms.size());
            
            Chromosome chromosome1 = null;
            Chromosome chromosome2 = null;
            
            if(replace) {
                chromosome1 = chroms.get(index1);
                chromosome2 = chroms.get(index2);
                chromosome1.setObjectives(null);
                chromosome2.setObjectives(null);
            } else {
                chromosome1 = (Chromosome) chroms.get(index1).clone();
                chromosome2 = (Chromosome) chroms.get(index2).clone();
            }
            
            //do the crossover, and add to result set if necessary
            doCrossover(chromosome1, chromosome2);
            
            if(!replace) {
                result.add(chromosome1);
                result.add(chromosome2);
            }
        }

        for(int i = 0; i < result.size(); i++) {
            chroms.add(result.get(i));
        }
    }

    public void doCrossover(Chromosome chrom1, Chromosome chrom2) {
        Vector<Gene> genes1 = chrom1.getGenes();
        Vector<Gene> genes2 = chrom2.getGenes();

        int locus = Population.nextInt(genes1.size());
        Object swap;

        for(int i = locus; i < genes1.size(); i++) {
            swap = genes1.get(i).getAllele();
            genes1.get(i).setAllele(genes2.get(i).getAllele());
            genes2.get(i).setAllele(swap);
        }
    }
}
