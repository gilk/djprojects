/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.List;
import java.util.Set;
import org.dj.domainmodel.api.DJObjectManager;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;

/**
 *
 * @author djabry
 */
public interface Level extends DJObjectManager<ValueUnitCell> , Comparable<Level>{
    

    
    Level duplicate();
   
    
    ValueUnitCell getCellForName(String name);

    double getValueForVariable(String varName);
    LBLRTMUnit getUnitForVariable(String varName);
    
    void setValueForVariable(String varName, double val);
    void setUnitForVariable(String varName, LBLRTMUnit u);

    
    double getValueForPhysicalProperty(PhysicalProperty p);
    void setValueForPhysicalProperty(PhysicalProperty p,double value);
 
    LBLRTMUnit getUnitForPhysicalProperty(PhysicalProperty p);
    void setUnitForPhysicalProperty(PhysicalProperty p, LBLRTMUnit u);
    
    
    double getValueForMolecule(Molecule m);
    LBLRTMUnit getUnitForMolecule(Molecule m);
    
    void setValueForMolecule(Molecule m, double val);
    void setUnitForMolecule(Molecule m, LBLRTMUnit u);
    
}
