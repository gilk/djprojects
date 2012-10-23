/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import java.util.List;
import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import ucar.ma2.Array;
import ucar.ma2.Range;
import ucar.ma2.Section;

/**
 *
 * @author djabry
 */
public interface LBLRTMProfile extends DJNodeObject{
    
    public static final String PROP_PRESSURE="pressure";
    public static final String PROP_TEMPERATURE="temperature";
    public static final String PROP_ALTITUDE="altitude";
    public static final String PROP_MOLECULES = "molecules";
    
    
    List<Double> getTempProfile();
    List<Double> getAltitude();
    List<Double> getPressureProfile();
    List<Double> getProfileForMolecule(Molecule molecule);

    Array getProfileForVariable(String name);
    Array getArrayForVariable(String name, Section section);
    
    Range getLimitsForVariable(String name);
    
    
    List<LBLRTMUnit> getUnitsForMolecule(Molecule molecule);
    
    Set<Molecule> getMolecules();
    
    
    
    interface Provider {
        
        LBLRTMProfile getProfile();
    }
    
    LBLRTMProfile duplicate();
    
    LBLRTMProfile section(double startAlt, double endAlt);
    
}
