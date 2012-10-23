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
public class ROProp extends PropertySupport.ReadOnly{
    
    private DJProperty prop;
    
    public ROProp(DJProperty prop){
        super(prop.getPropertyName(), prop.getDisplayValue().getClass(), prop.getDisplayName(), prop.getShortDescription());
        this.prop=prop;
    }

    @Override
    public Object getValue() throws IllegalAccessException, InvocationTargetException {
        return prop.getDisplayValue();
    }
    
}
