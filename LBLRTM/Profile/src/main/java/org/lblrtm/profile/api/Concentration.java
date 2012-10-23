/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import org.lblrtm.lblrtmdata.api.Molecule;

/**
 *
 * @author djabry
 */
public interface Concentration extends ValueUnitCell {
    
    
    Molecule getMolecule();
    
    public static final String PROP_MOLECULE = "molecule";
    
}
