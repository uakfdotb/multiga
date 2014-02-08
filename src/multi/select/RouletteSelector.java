/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select;

import java.util.Vector;

import multi.Chromosome;
import multi.Evaluator;
import multi.select.roulette.Roulette;
/**
 *
 * @author wizardus
 */
public class RouletteSelector implements Selector {
    Evaluator eval;

    public RouletteSelector(Evaluator _eval) {
        eval = _eval;
    }

    public Vector<Chromosome> select(Vector<Chromosome> population,
            int num) {
        Roulette[] roulettes = new Roulette
                [population.get(0).getObjectives().length];

        for(int i = 0; i < roulettes.length; i++) {
            roulettes[i] = new Roulette(population, i, eval.getOptimal()[i]);
        }

        int roulette_index = 0;
        Vector<Chromosome> result = new Vector<Chromosome>();

        while(result.size() < num && roulettes[roulette_index].size() > 0) {
            int index = roulettes[roulette_index].spin();
            result.add(population.get(index));

            for(Roulette curr_roulette : roulettes) {
                curr_roulette.remove(index);
            }

            roulette_index++;

            if(roulette_index >= roulettes.length) {
                roulette_index = 0;
            }
        }

        return result;
    }
}
