/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi;

import java.util.Random;
import java.util.Vector;

import multi.operator.Operator;
import multi.select.Selector;
/**
 *
 * @author wizardus
 */
public class Population {
    static Random random = new Random();

    int size;
    Vector<Chromosome> chromosomes;
    Vector<Operator> operators;
    Evaluator evaluator;
    Selector selector;
    boolean first_operators;
    boolean auto_objectives;


    public Population(Chromosome sample_chromosome, int _size,
            Evaluator _evaluator, Selector _selector) {
        chromosomes = new Vector<Chromosome>();
        operators = new Vector<Operator>();
        size = _size;
        evaluator = _evaluator;
        selector = _selector;
        first_operators = true;
        auto_objectives = true;

        for(int i = 0; i < size; i++) {
            chromosomes.add(new Chromosome(sample_chromosome));
        }
    }

    public Population(Chromosome sample_chromosome, int _size,
            Evaluator _evaluator, Selector _selector,
            boolean _first_operators) {
        chromosomes = new Vector<Chromosome>();
        operators = new Vector<Operator>();
        size = _size;
        evaluator = _evaluator;
        selector = _selector;
        first_operators = _first_operators;
        auto_objectives = true;

        for(int i = 0; i < size; i++) {
            chromosomes.add(new Chromosome(sample_chromosome));
        }
    }

    public Population(Chromosome sample_chromosome, int _size,
            Evaluator _evaluator, Selector _selector,
            boolean _first_operators, boolean _auto_objectives) {
        chromosomes = new Vector<Chromosome>();
        operators = new Vector<Operator>();
        size = _size;
        evaluator = _evaluator;
        selector = _selector;
        first_operators = _first_operators;
        auto_objectives = _auto_objectives;

        for(int i = 0; i < size; i++) {
            chromosomes.add(new Chromosome(sample_chromosome));
        }
    }

    public Population(Vector<Chromosome> _chromosomes, Evaluator _evaluator,
            Selector _selector, boolean _first_operators) {
        chromosomes = _chromosomes;
        operators = new Vector<Operator>();
        size = chromosomes.size();
        evaluator = _evaluator;
        selector = _selector;
        first_operators = _first_operators;
        auto_objectives = true;
    }

    public Population(Vector<Chromosome> _chromosomes, Evaluator _evaluator,
            Selector _selector, boolean _first_operators, boolean _auto_objectives) {
        chromosomes = _chromosomes;
        operators = new Vector<Operator>();
        size = chromosomes.size();
        evaluator = _evaluator;
        selector = _selector;
        first_operators = _first_operators;
        auto_objectives = _auto_objectives;
    }

    public void evolve() {
        if(first_operators) {
            //apply genetic operators
            for(Operator operator : operators) {
                operator.apply(chromosomes);
            }

            if(auto_objectives) {
                //get objectives when necessary
                for(Chromosome chrom : chromosomes) {
                    if(chrom.getObjectives() == null) {
                        chrom.setObjectives(evaluator.getFitness(chrom));
                    }
                }
            }
        }

        //use selector
        chromosomes = selector.select(chromosomes, size);

        if(!first_operators) {
            //apply genetic operators
            for(Operator operator : operators) {
                operator.apply(chromosomes);
            }

            if(auto_objectives) {
                //get objectives when necessary
                for(Chromosome chrom : chromosomes) {
                    if(chrom.getObjectives() == null) {
                        chrom.setObjectives(evaluator.getFitness(chrom));
                    }
                }
            }
        }
    }

    public Vector<Chromosome> getChromosomes() {
        return chromosomes;
    }

    public void setChromosomes(Vector<Chromosome> chroms) {
        chromosomes = chroms;
    }

    public Chromosome getChromosome(int index) {
        return chromosomes.get(index);
    }

    public void setChromosome(int index, Chromosome chrom) {
        chromosomes.set(index, chrom);
    }

    public void addChromosome(Chromosome add) {
        chromosomes.add(add);
    }

    public Vector<Operator> getOperators() {
        return operators;
    }

    public void setOperators(Vector<Operator> ops) {
        operators = ops;
    }

    public void addOperator(Operator operator) {
        operators.add(operator);
    }

    public Evaluator getEvaluator() {
        return evaluator;
    }
    
    public void setEvaluator(Evaluator _evaluator) {
        evaluator = _evaluator;
    }

    public static void setSeed(long seed) {
        random.setSeed(seed);
    }

    public static Random random() {
        return random;
    }

    public static int nextInt() {
        return random.nextInt();
    }

    public static int nextInt(int max) {
        return random.nextInt(max);
    }

    public static double nextDouble() {
        return random.nextDouble();
    }
}
