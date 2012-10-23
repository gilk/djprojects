/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile.api;

import java.util.HashSet;

/**
 *
 * @author djabry
 */
public class CustomLevels extends ProfileAbstr{
    
    private final DJProfile childProf;
    private boolean useProfileLevels;
    
    
    
    public CustomLevels(DJProfile childProf, boolean useProfileLevels, boolean isPressureLevels){
        
        super(new HashSet<Level>(),new CustomLevelsVariableManager(childProf.getVariableManager(),isPressureLevels));
        this.childProf=childProf;
        this.useProfileLevels=useProfileLevels;
        
    }

    @Override
    public Profile duplicate() {
        return new CustomLevels((DJProfile)this.childProf.duplicate(), this.isUseProfileLevels(),
                ((CustomLevelsVariableManager)this.getVariableManager()).isUsePressureLevels());
    }

    /**
     * @return the useProfileLevels
     */
    public boolean isUseProfileLevels() {
        return useProfileLevels;
        
    }

    /**
     * @param useProfileLevels the useProfileLevels to set
     */
    public void setUseProfileLevels(boolean useProfileLevels) {
        this.useProfileLevels = useProfileLevels;

    }
    
    public void setUsePressureLevels(boolean tf){
        
        ((CustomLevelsVariableManager)this.getVariableManager()).setUsePressureLevels(tf);
    }
    
    public boolean isUsePressureLevels(){
        
        return ((CustomLevelsVariableManager)this.getVariableManager()).isUsePressureLevels();
    }

    @Override
    public double getValueForVariable(String name, int level) {
        
        double val = super.getValueForVariable(name, level);
        
        if(this.isUseProfileLevels()){
            
            val = this.childProf.getValueForVariable(name, level);
        }
        
        
        return val;
       
    }

    @Override
    public void setValueForVariable(String name, int level, double value) {

        if(!this.isUseProfileLevels()){
            
            super.setValueForVariable(name, level, value);
        }
    }
    
    
    
    
    
    
    
}
