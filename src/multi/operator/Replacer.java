/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.operator;

import java.util.Vector;

import multi.Chromosome;
import multi.Evaluator;
import multi.Gene;
import multi.Population;
/**
 *
 * @author wizardus
 */
public class Replacer implements Operator {
    double m_rate, c_rate;
    int size;
    Evaluator eval;

    /**
     * Initializes a new Replacer.
     * This uses mutation, crossover, and refresh operators
     * to create a new population.
     * @param mutation mutation rate.
     * @param crossover crossover rate.
     * @param _size size of generated populations.
     * @param _eval evaluator to use.
     */
    public Replacer(double mutation, double crossover,
            int _size, Evaluator _eval) {
        m_rate = mutation;
        c_rate = crossover;
        size = _size;
        eval = _eval;
    }

    public void apply(Vector<Chromosome> population) {
        Vector<Chromosome> old_population =
                new Vector<Chromosome>(population.size());
        for(Chromosome c : population) {
            old_population.add(c);
        }
        population.clear();

        int mutations = (int) (m_rate * old_population.size());
        int crossovers = (int) (c_rate * old_population.size());
        int generate = size - mutations - crossovers * 2;

        for(int i = 0; i < mutations; i++) {
            int chrom_index = Population.nextInt(old_population.size());

            Chromosome do_mutate =
                    (Chromosome) old_population.get(chrom_index).clone();
            int gene_index = Population.nextInt(do_mutate.getGenes().size());

            mutateGene(do_mutate.getGene(gene_index));
            population.add(do_mutate);
        }

        for(int i = 0; i < crossovers; i++) {
            int index1 = Population.nextInt(old_population.size());
            int index2 = Population.nextInt(old_population.size());
            Chromosome chrom1 =
                    (Chromosome) old_population.get(index1).clone();
            Chromosome chrom2 =
                    (Chromosome) old_population.get(index2).clone();
            doCrossover(chrom1, chrom2);
            
            population.add(chrom1);
            population.add(chrom2);
        }

        for(int i = 0; i < generate; i++) {
            Chromosome chrom_gen = new Chromosome(old_population.get(0));
            population.add(chrom_gen);
        }
    }

    private void mutateGene(Gene gene) {
        double percentage = Population.nextDouble() * 2 - 1;
        gene.mutate(percentage);
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
