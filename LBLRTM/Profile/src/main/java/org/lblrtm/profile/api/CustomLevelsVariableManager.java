/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import com.google.common.collect.Sets;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.EnumSet;
import java.util.Set;
import org.lblrtm.lblrtmdata.api.Molecule;

/**
 *
 * @author djabry
 */
public class CustomLevelsVariableManager implements VariableManager{
    
    private boolean usePressureLevels;
    private final VariableManager subVM;
    

    public CustomLevelsVariableManager(VariableManager subVM, boolean usePressureLevels){
        this.usePressureLevels=usePressureLevels;
        this.subVM=subVM;
       
    }

    @Override
    public Set<Molecule> getMolecules() {
        return EnumSet.noneOf(Molecule.class);
    }
 
    @Override
    public VariableManager duplicate() {
        return new CustomLevelsVariableManager(this.subVM.duplicate(),this.usePressureLevels);
    }

    @Override
    public void addVariable(String variable) {
        
    }

    @Override
    public void removeVariable(String variable) {
        
    }
    


    /**
     * @return the usePressureLevels
     */
    public boolean isUsePressureLevels() {
        return usePressureLevels;
    }

    /**
     * @param usePressureLevels the usePressureLevels to set
     */
    public void setUsePressureLevels(boolean usePressureLevels) {
        this.usePressureLevels = usePressureLevels;
    }

    @Override
    public Set<String> getVariables() {
        String varName = PhysicalProperty.ALTITUDE.name();
        if(this.isUsePressureLevels()){
            varName = PhysicalProperty.PRESSURE.name();
        }
        
        return Sets.newHashSet(varName);

        
    }

    @Override
    public Set<PhysicalProperty> getPhysicalProperties() {
        
        PhysicalProperty prop = PhysicalProperty.ALTITUDE;
        if(this.isUsePressureLevels()){
            
            prop = PhysicalProperty.PRESSURE;
        }
        
        return Sets.newHashSet(prop);
        
        
    }

    @Override
    public void addMolecule(Molecule m) {
        
    }

    @Override
    public void addPhysicalProperty(PhysicalProperty p) {
       
    }

    @Override
    public void removeMolecule(Molecule m) {
        
    }

    @Override
    public void removePhysicalProperty(PhysicalProperty p) {
       
    }

    @Override
    public String getVariableFullName(String variable) {
       return this.subVM.getVariableFullName(variable);
    }

    @Override
    public Enum getVariableEnum(String variable) {
        return this.subVM.getVariableEnum(variable);
    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void firePropertyChange(String prop, Object o1, Object o2) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void firePropertyChange(PropertyChangeEvent pCE) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyChangeListener[] getPropertyChangeListeners(String propertyName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addPropertyChangeListener(String propName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removePropertyChangeListener(String propName, PropertyChangeListener listener) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public PropertyChangeSupport getPropertyChangeSupport() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
}
