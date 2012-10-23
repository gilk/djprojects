/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.SortedSet;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.filter.api.Filter;
import org.dj.matrix.api.DJMatrix;

/**
 *
 * @author djabry
 */
public interface DJProfile extends Profile, DJObjectManager<Level>, DJNodeObject.Source{

    public static final String PROP_VARIABLE_MANAGER  = "variableManager";
    
    Level getLevel(int levelIndex);
    Iterable<Level> getAllLevels();
    
    VariableManager getVariableManager();
    DJMatrix exportToMatrix();
    void importMatrix(DJMatrix mat);
    
    public double getValueAt(int r,int c);
    public void setValueAt(int r, int c,double val);


    public interface Template {
        DJProfile createProfile();
    }
    
}
