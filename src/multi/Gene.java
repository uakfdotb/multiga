/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package multi;

/**
 *
 * @author wizardus
 */
public interface Gene {
    /**
     * Sets the allele (value) of this gene.
     * The object usually must be the same type as the type of gene.
     * @param allele the allele to point to (not cloned).
     */
    public void setAllele(Object allele);
    /**
     * @return the allele (value) of this gene.
     */
    public Object getAllele();
    /**
     * Generates a gene based on the min and max values for this gene.
     * @return the generated gene.
     */
    public Gene generate();
    public Object clone();
    /**
     * Performs a mutation operation using the percentage specified.
     * Some implementations of Gene disregard this percentage.
     * @param percentage the percentage.
     */
    public void mutate(double percentage);
}
