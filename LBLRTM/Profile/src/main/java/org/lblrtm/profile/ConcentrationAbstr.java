/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import org.lblrtm.profile.api.Concentration;
import org.lblrtm.profile.api.PhysicalProperty;
import org.lblrtm.profile.api.ValueUnitCell;

/**
 *
 * @author djabry
 */
public class ConcentrationAbstr extends ValueUnitCellAbstr implements Concentration{
    
    private static final Set<LBLRTMUnit> h2OUnits 
            = new TreeSet(Arrays.asList(new LBLRTMUnit[]{
                LBLRTMUnit.VOLUME_MIXING_RATIO,
                LBLRTMUnit.NUMBER_DENSITY,
                LBLRTMUnit.PARTIAL_PRESSURE,
                LBLRTMUnit.MASS_MIXING_RATIO,
                LBLRTMUnit.DEW_POINT_K,
                LBLRTMUnit.DEW_POINT_C,
                LBLRTMUnit.RELATIVE_HUMIDITY,
                LBLRTMUnit.TROPICAL,
                LBLRTMUnit.MLS,
                LBLRTMUnit.MLW,
                LBLRTMUnit.SAS,
                LBLRTMUnit.SAW,
                LBLRTMUnit.US     
            }));
    
    private static final Set<LBLRTMUnit> normalUnits =new TreeSet(Arrays.asList(new LBLRTMUnit[]{
                LBLRTMUnit.VOLUME_MIXING_RATIO,
                LBLRTMUnit.NUMBER_DENSITY,
                LBLRTMUnit.PARTIAL_PRESSURE,
                LBLRTMUnit.MASS_MIXING_RATIO,
                LBLRTMUnit.TROPICAL,
                LBLRTMUnit.MLS,
                LBLRTMUnit.MLW,
                LBLRTMUnit.SAS,
                LBLRTMUnit.SAW,
                LBLRTMUnit.US
            }));
    
    private static final LBLRTMUnit defaultUnit = LBLRTMUnit.VOLUME_MIXING_RATIO;
    private final Integer index;
    
    public ConcentrationAbstr(Molecule m){
        
        super (m,m.equals(Molecule.H2O) ? h2OUnits : normalUnits ,defaultUnit,0.0);
        this.index=m.getMoleculeNumber()+PhysicalProperty.values().length;
        this.setName(m.getName());
        
        this.setPropertyValue(PROP_MOLECULE, m);
    }
    
    public ConcentrationAbstr(){
        this(Molecule.H2O);
        
    }


    @Override
    public Molecule getMolecule() {
        return (Molecule) this.getPropertyValue(PROP_MOLECULE);
    }

    @Override
    public ValueUnitCell duplicate() {
        ConcentrationAbstr c = new ConcentrationAbstr(this.getMolecule());
        c.setValue(this.getValue());
        c.setUnit(this.getUnit());
        
        return c;
    }

    @Override
    public Integer getIndex() {
        return index;
    }
    
}
