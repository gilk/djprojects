/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.propertysheetgenerator;

import java.lang.reflect.InvocationTargetException;
import org.dj.property.api.DJProperty;
import org.openide.nodes.PropertySupport;

/**
 *
 * @author djabry
 */
public class RWProp extends PropertySupport.ReadWrite{
    
    private DJProperty prop;
    
    public RWProp(DJProperty prop){
        
        super(prop.getPropertyName(),prop.getDisplayValue().getClass(),prop.getDisplayName(),prop.getShortDescription());
        
        this.prop=prop;
        
    }

    @Override
    public boolean canWrite() {
        return prop.isCanWrite();
    }

    @Override
    public boolean isHidden() {
        return !prop.isCanRead();
    }

    @Override
    public String getDisplayName() {
        return prop.getDisplayName();
    }

    @Override
    public Class getValueType() {
        return prop.getDisplayValue().getClass();
    }

    @Override
    public String getShortDescription() {
        return prop.getShortDescription();
    }
    

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return prop.getDisplayValue();
    }

    @Override
    public void setValue(Object t) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        prop.setDisplayValue(t);
    }
    
}
