/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property;

import org.dj.property.api.PropertyAbstr;
import javolution.context.ObjectFactory;
import org.dj.property.api.DJProperty;
import org.dj.property.api.PropertyFactory;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service= PropertyFactory.class)
public class PropertyFactoryImpl implements PropertyFactory{
    
    private static final ObjectFactory<PropertyAbstr> factory =  ObjectFactory.getInstance(PropertyAbstr.class);

    @Override
    public <T extends Object> DJProperty<T> createProperty(String propertyName, T value) {
        
        PropertyAbstr<T> object = new PropertyAbstr<T>(propertyName,value);
        object.setPropertyName(propertyName);
        object.setValue(value);
        return object;
        
    }
    
}
