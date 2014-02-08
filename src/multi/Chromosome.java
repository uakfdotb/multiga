/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi;

import java.util.Vector;
/**
 *
 * @author wizardus
 */
public class Chromosome {
    Vector<Gene> genes;
    double[] objectives;

    /**
     * Initializes a chromosome with an empty gene set.
     */
    public Chromosome() {
        genes = new Vector<Gene>();
    }

    /**
     * Initializes a chromosome with the specified gene set.
     * @param gene_array the gene set.
     */
    public Chromosome(Gene[] gene_array) {
        genes = new Vector<Gene>();

        for(Gene curr : gene_array) {
            genes.add(curr);
        }
    }

    /**
     * Initializes a chromosome with the specified gene set.
     * @param _genes the gene set to point to (not cloned).
     */
    public Chromosome(Vector<Gene> _genes) {
        genes = _genes;
    }

    /**
     * Initializes a chromosome based on the sample chromosome's gene set.
     * The genes of this chromosome are generated randomly in the range
     * specified in the gene set.
     * @param sample the sample chromosome.
     */
    public Chromosome(Chromosome sample) {
        genes = new Vector<Gene>();
        Vector<Gene> sample_genes = sample.getGenes();

        for(Gene curr : sample_genes) {
            genes.add(curr.generate());
        }
    }

    /**
     * Retuns the last retrieved objectives for this chromosome.
     * @return an array of objectives
     */
    public double[] getObjectives() {
        return objectives;
    }

    /**
     * Points this chromosomes objectives to the array specified.
     * @param _objectives an array of objectives.
     */
    public void setObjectives(double[] _objectives) {
        objectives = _objectives;
    }

    /**
     * @return the gene vector.
     */
    public Vector<Gene> getGenes() {
        return genes;
    }

    /**
     * @param _genes the gene vector to point to (not cloned).
     */
    public void setGenes(Vector<Gene> _genes) {
        genes = _genes;
    }

    /**
     * Retrieves the gene at the specified index.
     * @param index the index in the gene set.
     * @return gene at index.
     */
    public Gene getGene(int index) {
        return genes.get(index);
    }

    /**
     * Sets a gene.
     * @param index the index in the gene set.
     * @param gene new gene.
     */
    public void setGene(int index, Gene gene) {
        genes.set(index, gene);
    }

    public Object clone() {
        Chromosome clone = new Chromosome();
        clone.genes = new Vector<Gene>();

        for(Gene curr : genes) {
            clone.genes.add((Gene) curr.clone());
        }

        return clone;
    }
}
