/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi.select.elite;

/**
 *
 * @author Wizardus
 */
public class EliteSorter implements java.util.Comparator {
    public int compare(Object o1, Object o2) {
        ChromDominance cd1 = (ChromDominance) o1;
        ChromDominance cd2 = (ChromDominance) o2;
        if(cd1.dominance < cd2.dominance) return -1;
        else if(cd1.dominance > cd2.dominance) return 1;
        else return 0;
    }
}
