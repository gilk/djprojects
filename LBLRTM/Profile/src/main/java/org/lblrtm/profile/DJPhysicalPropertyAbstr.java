/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.profile;

import org.lblrtm.profile.api.DJPhysicalProperty;
import org.lblrtm.profile.api.PhysicalProperty;

/**
 *
 * @author djabry
 */
public class DJPhysicalPropertyAbstr extends ValueUnitCellAbstr implements DJPhysicalProperty {

    public DJPhysicalPropertyAbstr(){
        
        this(PhysicalProperty.PRESSURE);
        

    }
    
    public DJPhysicalPropertyAbstr(PhysicalProperty p){
        super(p,p.availableUnits,p.defaultUnit,p.defaultValue);
        this.setPhysicalProperty(p);
        this.setName(p.fullName);
    }

    @Override
    public PhysicalProperty getPhysicalProperty() {
        return (PhysicalProperty) this.getPropertyValue(PROP_PHYSICAL_PROPERTY);
    }
    
    public void setPhysicalProperty(PhysicalProperty p){
        this.setPropertyValue(PROP_PHYSICAL_PROPERTY, p);
    }

    @Override
    public DJPhysicalProperty duplicate() {
        DJPhysicalPropertyAbstr p = new DJPhysicalPropertyAbstr(this.getPhysicalProperty());
        p.setValue(this.getValue());
        p.setUnit(this.getUnit());
        
        return p;
    }

    @Override
    public Integer getIndex() {
        return this.getPhysicalProperty().ordinal();
    }




    
    
}
