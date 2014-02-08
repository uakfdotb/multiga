/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select.elite;

import multi.Chromosome;
/**
 *
 * @author wizardus
 */
public class ChromDominance {
    public Chromosome chrom;
    public int dominance;
    public boolean changed = true;

    public ChromDominance(Chromosome _chrom) {
        dominance = 0;
        chrom = _chrom;
    }

    public ChromDominance(Chromosome _chrom, int _dominance) {
        dominance = _dominance;
        chrom = _chrom;
    }
}
