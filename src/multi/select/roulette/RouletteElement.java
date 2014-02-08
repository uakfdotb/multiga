/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select.roulette;

/**
 *
 * @author wizardus
 */
public class RouletteElement {
    public int chrom_index;
    public double slots;

    public RouletteElement(int index, double _slots) {
        chrom_index = index;
        slots = _slots;
    }
}
