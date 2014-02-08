/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select;

import java.util.Collections;
import java.util.Vector;

import multi.Chromosome;
import multi.Evaluator;
import multi.Gene;
import multi.genes.DoubleGene;
import multi.genes.IntegerGene;
import multi.select.elite.ChromDominance;
import multi.select.elite.EliteSorter;
/**
 *
 * @author wizardus
 */
public class EliteSelector implements Selector {
    public Vector<ChromDominance> elite;
    public Vector<Chromosome> last_population;
    public int thresh_evolutions;
    public double[] objective_change; //last change in objective value
    public double[] last_avg_objectives;

    int elite_size;
    Evaluator eval;
    double thresh_percent;
    //used for selecting from the remaining chromosomes
    //     to achieve pop_size - elite_size
    Selector selector;
    int num_select;
    boolean merge = false;


    public EliteSelector(int _elite_size, double _thresh_percent,
            Evaluator _eval, Selector _selector, Chromosome sample) {
        elite_size = _elite_size;
        thresh_percent = _thresh_percent;
        eval = _eval;
        selector = _selector;
        num_select = 0;

        elite = new Vector<ChromDominance>();
    }

    public EliteSelector(int _elite_size, double _thresh_percent,
            Evaluator _eval, Selector _selector, Chromosome sample, boolean _merge) {
        elite_size = _elite_size;
        thresh_percent = _thresh_percent;
        eval = _eval;
        selector = _selector;
        num_select = 0;

        elite = new Vector<ChromDominance>();
        merge = _merge;
    }

    public EliteSelector(int _elite_size, int _num_select,
            double _thresh_percent, Evaluator _eval,
            Selector _selector, Chromosome sample) {
        elite_size = _elite_size;
        thresh_percent = _thresh_percent;
        eval = _eval;
        selector = _selector;
        num_select = _num_select;

        elite = new Vector<ChromDominance>();
    }

    //chrom is the chromosome to check;
    //    returns false if chrom should be removed
    public boolean goodChromosome(Chromosome chrom) {
        //return false if this gene is duplicate; check with elite set
        for(int i = 0; i < elite.size(); i++) {
            Chromosome elite_chrom = elite.get(i).chrom;
            if(isDuplicate(chrom, elite_chrom)) {
                return false;
            }
        }

        return true;
    }

    public boolean isDuplicate(Chromosome chrom1, Chromosome chrom2) {
        boolean duplicate = true;

        //check gene by gene
        for(int g = 0; g < chrom1.getGenes().size(); g++) {
           if(chrom1.getGene(g) instanceof IntegerGene) {
               IntegerGene i_gene = (IntegerGene) chrom1.getGene(g);
               IntegerGene j_gene = (IntegerGene) chrom2.getGene(g);
               if(i_gene.intValue() != j_gene.intValue()) duplicate = false;
           } else if(chrom1.getGene(g) instanceof DoubleGene) {
               DoubleGene i_gene = (DoubleGene) chrom1.getGene(g);
               DoubleGene j_gene = (DoubleGene) chrom2.getGene(g);
               //compute 1% of range in gene set
               double max_difference = (i_gene.upperBound() - i_gene.lowerBound()) * .01;

               if(Math.abs(i_gene.doubleValue() - j_gene.doubleValue()) > max_difference) duplicate = false;
           } else if(!chrom1.getGene(g).equals(chrom2.getGene(g))) {
               duplicate = false;
           }
        }

        return duplicate;
    }

    public Vector<Chromosome> select(Vector<Chromosome> from, int num) {
        //** FIRST, evaluate any new chromosomes
        //check for duplicates from
        for(int i = 0; i < from.size(); i++) {
            for(int j = i + 1; j < from.size(); j++) {
                Chromosome i_chrom = from.get(i);
                Chromosome j_chrom = from.get(j);

                if(isDuplicate(i_chrom, j_chrom)) {
                    //remove j_chrom
                    from.remove(j);
                    j--;
                    continue;
                }
            }
        }

        for(Chromosome curr : from) {
            if(curr.getObjectives() == null) {
                curr.setObjectives(eval.getFitness(curr));
            }
        }

        //** SECOND, we want to add chromosomes to elite set and swap

        //copy from from to clone_from
        Vector<ChromDominance> clone_from = new Vector<ChromDominance>();
        for(Chromosome curr : from) {
            clone_from.add(new ChromDominance(curr));
        }

        //remove bad chromosomes
        //duplicate-checking with elite set now included in goodChromosome()
        for(int i = 0; i < clone_from.size(); i++) {
            Chromosome chrom = clone_from.get(i).chrom;

            if(!goodChromosome(chrom)) {
                clone_from.remove(i);
                i--;
                continue;
            }
        }

        //check to see if elite set is not full
        if(elite.size() < elite_size) {
            //move from clone_from to elite
            for(int i = 0; i < clone_from.size(); i++) {
                elite.add(clone_from.get(i));
            }

            //check if its still less
            if(elite.size() < elite_size) {
                if(num_select != 0) {
                    num = num_select;
                }

                return callSelector(from, num);
            }

            else {
                //now evaluate dominance counts within elite set
                for(int i = 0; i < elite.size(); i++) {
                    for(int j = i + 1; j < elite.size(); j++) {
                        ChromDominance i_chrom = elite.get(i);
                        ChromDominance j_chrom = elite.get(j);

                        if(eval.isFitter(i_chrom.chrom, j_chrom.chrom)) {
                            j_chrom.dominance++;
                        } else if(eval.isFitter(j_chrom.chrom,
                                i_chrom.chrom)) {
                            i_chrom.dominance++;
                        }
                    }
                }
            }
        }

        //compare for dominance counts within clone_from
        for(int i = 0; i < clone_from.size(); i++) {
            for(int j = i + 1; j < clone_from.size(); j++) {
                Chromosome i_chrom = clone_from.get(i).chrom;
                Chromosome j_chrom = clone_from.get(j).chrom;

                if(eval.isFitter(i_chrom, j_chrom)) {
                    ChromDominance cd = clone_from.get(j);
                    cd.dominance++;
                } else if(eval.isFitter(j_chrom, i_chrom)) {
                    ChromDominance cd = clone_from.get(i);
                    cd.dominance++;
                }
            }
        }

        //compare for dominance counts between clone_from and elite
        for(int i = 0; i < clone_from.size(); i++) {
            for(int j = 0; j < elite_size; j++) {
                Chromosome i_chrom = clone_from.get(i).chrom;
                Chromosome j_chrom = elite.get(j).chrom;

                if(eval.isEquivalent(i_chrom, j_chrom)) {
                    ChromDominance cd = clone_from.get(i);
                    cd.dominance++; //keep elite set individual if not comparable
                }

                if(eval.isFitter(i_chrom, j_chrom)) {
                    ChromDominance cd = elite.get(j);
                    cd.dominance++;
                } else if(eval.isFitter(j_chrom, i_chrom)) {
                    ChromDominance cd = clone_from.get(i);
                    cd.dominance++;
                }
            }
        }

        //switch between the sets, best -> elite set
        for(int i = 0; i < clone_from.size(); i++) {
            for(int j = 0; j < elite.size(); j++) {
                ChromDominance i_chrom = clone_from.get(i);
                ChromDominance j_chrom = elite.get(j);

                if(i_chrom.dominance < j_chrom.dominance) {
                    clone_from.remove(i);
                    elite.remove(j);
                    clone_from.add(j_chrom);
                    elite.add(i_chrom);
                    i--;
                    break;
                }
            }
        }

        //find num_changed
        int num_changed = 0; //number of chromosomes that are different now
        for(int i = 0; i < elite.size(); i++) {
            ChromDominance chrom = elite.get(i);
            if(chrom.changed) {
                num_changed++;
                chrom.changed = false;
            }
        }

        if(num_changed <= thresh_percent * elite_size) {
            //comparison of threshold evolutions to max
            //    is done in MultiConnector
            thresh_evolutions++;
        } else {
            thresh_evolutions = 0; //reset if not within threshold
        }

        //sort the elite set
        EliteSorter es = new EliteSorter();
        Collections.sort(elite, es);
        //calculate average objectives in the elite set
        double[] avg_objectives = new double[eval.getOptimal().length];
        for(int i = 0; i < elite.size(); i++) {
            double[] cd_objectives = elite.get(i).chrom.getObjectives();
            for(int j = 0; j < cd_objectives.length; j++) {
                avg_objectives[j] += cd_objectives[j];
            }
        }

        for(int i = 0; i < avg_objectives.length; i++) {
            avg_objectives[i] = avg_objectives[i] / elite.size();
        }
        //calculate change in average objectives
        if(last_avg_objectives != null) {
            for(int i = 0; i < avg_objectives.length; i++) {
                objective_change[i] = avg_objectives[i] / last_avg_objectives[i];
            }
        } else {
            objective_change = new double[avg_objectives.length];
        }
        last_avg_objectives = avg_objectives;


        //copy from new clone_from to a Vector<Chromosome>
        Vector<Chromosome> clone_chroms = new Vector<Chromosome>();
        for(int i = 0; i < clone_from.size(); i++) {
            clone_chroms.add(clone_from.get(i).chrom);
        }

        //copy from elite to clone_chroms
        for(int i = 0; i < elite.size(); i++) {
            clone_chroms.add(elite.get(i).chrom);
        }

        if(num_select != 0) {
            num = num_select;
        }

        if(merge) {
            return callSelector(clone_chroms, num);
        } else {
            return callSelector(from, num);
        }
    }

    public Vector<Chromosome> callSelector(Vector<Chromosome> from,
            int num) {
        Vector<Chromosome> result = null;

        if(selector != null) {
            result = selector.select(from, num);
        } else {
            result = from;
        }

        last_population = new Vector<Chromosome>();
        for(Chromosome chrom : result) {
            last_population.add(chrom);
        }

        return result;
    }

    /**
     * Prints the elite set. Same as toString().
     * @return String containing chromosomes of this elite set.
     */
    public String printElite() {
        EliteSorter es = new EliteSorter();
        Collections.sort(elite, es);

        String s = "";
        for(ChromDominance curr : elite) {
            s += print(curr);
        }

        return s;
    }

    public String toString() {
        return printElite();
    }

    /**
     * Prints a single ChromDominance.
     * @param cd the ChromDominance to print.
     * @return
     */
    public String print(ChromDominance cd) {
        String s = "";

        s += "-----\n";
        s += "dominance count: " + cd.dominance + "\n";
        s += "objectives: [";
        for(int i = 0; i < cd.chrom.getObjectives().length; i++) {
            s += cd.chrom.getObjectives()[i] + ",";
        }
        s += "]\n";

        s += "genes: [";
        for(int i = 0; i < cd.chrom.getGenes().size(); i++) {
            Gene curr = cd.chrom.getGene(i);
            if(curr instanceof DoubleGene) {
                DoubleGene double_curr = (DoubleGene) curr;
                s += double_curr.doubleValue() + ",";
            } else if(curr instanceof IntegerGene) {
                IntegerGene int_curr = (IntegerGene) curr;
                s += int_curr.intValue() + ",";
            } else {
                s += curr.toString() + ",";
            }
        }
        s += "]\n";

        return s;
    }

    public String print(Vector<Chromosome> print) {
        String s = "";

        for(Chromosome c : print) {
            s += print(c);
        }

        return s;
    }

    /**
     * Prints a single Chromosome
     * @param cd the Chromosome
     * @return
     */
    public String print(Chromosome cd) {
        String s = "";

        s += "-----\n";
        s += "objectives: [";
        for(int i = 0; i < cd.getObjectives().length; i++) {
            s += cd.getObjectives()[i] + ",";
        }
        s += "]\n";

        s += "genes: [";
        for(int i = 0; i < cd.getGenes().size(); i++) {
            Gene curr = cd.getGene(i);
            if(curr instanceof DoubleGene) {
                DoubleGene double_curr = (DoubleGene) curr;
                s += double_curr.doubleValue() + ",";
            } else if(curr instanceof IntegerGene) {
                IntegerGene int_curr = (IntegerGene) curr;
                s += int_curr.intValue() + ",";
            } else {
                s += curr.toString() + ",";
            }
        }
        s += "]\n";

        return s;
    }
}
