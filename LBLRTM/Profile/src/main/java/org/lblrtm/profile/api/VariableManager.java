/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.Set;
import org.dj.listener.api.Listener;
import org.lblrtm.lblrtmdata.api.Molecule;

/**
 *
 * @author djabry
 */
public interface VariableManager extends Listener{
    
    Set<String> getVariables();
    
    Set<Molecule> getMolecules();
    Set<PhysicalProperty> getPhysicalProperties();
    
    void addVariable(String variable);
    void addMolecule(Molecule m);
    void addPhysicalProperty(PhysicalProperty p);
    
    void removeVariable(String variable);
    void removeMolecule(Molecule m);
    void removePhysicalProperty(PhysicalProperty p);
    
    
    String getVariableFullName(String variable);
    Enum getVariableEnum(String variable);
    VariableManager duplicate();
    
}
