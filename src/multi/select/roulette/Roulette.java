/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select.roulette;

import java.util.Vector;

import multi.Chromosome;
import multi.Population;
/**
 *
 * @author wizardus
 */
public class Roulette {
    Vector<RouletteElement> slots;

    public Roulette(Vector<Chromosome> chroms, int objective,
            boolean max_optimal) {
        slots = new Vector<RouletteElement>();

        if(max_optimal) {
            double min = -1;

            for(Chromosome curr : chroms) {
                if(curr.getObjectives()[objective] < min || min == -1) {
                    min = curr.getObjectives()[objective];
                }
            }

            for(int i = 0; i < chroms.size(); i++) {
                Chromosome curr = chroms.get(i);
                slots.add(new RouletteElement
                        (i, curr.getObjectives()[objective] - min));
            }
        } else {
            double max = -1;

            for(Chromosome curr : chroms) {
                if(curr.getObjectives()[objective] > max || max == -1) {
                    max = curr.getObjectives()[objective];
                }
            }

            for(int i = 0; i < chroms.size(); i++) {
                Chromosome curr = chroms.get(i);
                slots.add(new RouletteElement
                        (i, max - curr.getObjectives()[objective]));
            }
        }
    }

    public int size() {
        return slots.size();
    }

    public int spin() {
        //calculate total slots
        double total_slots = 0;

        for(RouletteElement element : slots) {
            total_slots += element.slots;
        }

        //select one
        double selection = Population.nextDouble() * total_slots;

        //find the chromosome
        double current_slots = 0;

        for(RouletteElement element : slots) {
            current_slots += element.slots;

            if(selection - current_slots <= 0) {
                //found the element
                return element.chrom_index;
            }
        }

        return slots.lastElement().chrom_index;
    }

    public void remove(int chrom_index) {
        for(int i = 0; i < slots.size(); i++) {
            if(slots.get(i).chrom_index == chrom_index) {
                slots.remove(i);
                i--;
            }
        }
    }
}
