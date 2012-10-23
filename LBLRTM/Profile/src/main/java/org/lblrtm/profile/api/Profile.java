/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.filter.api.Filter;
import org.lblrtm.lblrtmdata.api.LBLRTMUnit;
import org.lblrtm.lblrtmdata.api.Molecule;
import ucar.ma2.Array;
import ucar.ma2.Range;
import ucar.ma2.Section;

/**
 *
 * @author djabry
 */
public interface Profile {
    
    public static final String MESSAGE_LEVELS_CHANGED = "levelsChanged";
    public static final String MESSAGE_VARIABLES_CHANGED = "variablesChanged";
    public static final String MESSAGE_CELL_CHANGED = "cellChanged";
    
    
    public interface Provider {
        
        Profile getProfile();
    }
    
    int getNumberOfLevels();
    
    Set<Molecule> getMolecules();
    Set<PhysicalProperty> getPhysicalProperties();
    
    Set<String> getVariables();
    String getVariableFullName(String variable);
    
    Array getProfileForVariable(String name);
    Array getArrayForVariable(String name, Section section);
    
    Range getLimitsForVariable(String name);
    
    double getValueForVariable(String name, int level);
    void setValueForVariable(String name, int level, double value);
    
    LBLRTMUnit getUnitForVariable(String name, int level);
    void setUnitForVariable(String name, int level, LBLRTMUnit unit);
    
    Profile duplicate();
    
    Profile section(Filter<Level> levelFilter);
    

    
}
