/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;
import javolution.util.FastSet;
import org.dj.parameter.api.ParametersAbstr;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public class PropertiesAbstr extends ParametersAbstr implements Properties{
    
    private static final PropertyFactory f = Lookup.getDefault().lookup(PropertyFactory.class);
    
       //Class to forward property change notifications originating from DJProperties
    private class PropListener implements PropertyChangeListener {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            Object source = evt.getSource();
            if(source instanceof DJProperty){
                firePropertyChange(evt);
                
            }
        }
 
    }
    
    
    
    public PropertiesAbstr(String propName, Object value){
        
        this();
        setProperty(getPropertyFactory().createProperty(propName, value));
    }
    
    public PropertiesAbstr(){
        
        super();
    }
    
    private PropListener propListener;
    
    public PropertyFactory getPropertyFactory(){
       
       return f;
    }
    
    public PropListener getPropListener(){
        
        if(propListener==null){

            propListener = new PropListener();
            
        }
        
        return propListener;
    }
   

    @Override
    public DJProperty getProperty(String propName) {
        
        Object value = this.getValue(propName);
        if(value instanceof DJProperty){
            DJProperty dJProperty = (DJProperty) value;
            return dJProperty;
        }
        
        return null;  
    }

    @Override
    public Object getPropertyValue(String propName) {
        return getProperty(propName).getValue();
    }

    @Override
    public void setPropertyValue(String propName, Object value) {
        
        DJProperty property = getProperty(propName);
        
        if(property ==null){
            
            property = getPropertyFactory().createProperty(propName, value);
            this.putProperty(property);
        
        }else{
            
            property.setValue(value);
        }
        
    }

    @Override
    @Deprecated
    public void setProperty(DJProperty prop) {
        this.putProperty(prop);
    }

    @Override
    public void putProperty(DJProperty property) {
        Object value = getValue(property.getPropertyName());
        
        if(value!=null){
            
            if(value instanceof DJProperty){
                DJProperty prop = (DJProperty) value;
                prop.removePropertyChangeListener(getPropListener());
            } 
        }
        
        //if the property is set up to notify listeners of changes then add a listener,
        //Otherwise do not add a listener in order to reduce the number of PropertyChangeSupport
        //objects being instantiated (created upon addPropertyChangeListener method being called)
        
        if(property.isNotify()){
            
            property.addPropertyChangeListener(getPropListener());
        }
        
        this.setValue(property.getPropertyName(), property);
    }

    @Override
    public Set<DJProperty> getAllProperties() {
        
        FastSet<DJProperty> allProps = new FastSet<DJProperty>();
        
        for(Object val:this.getAllValues()){
            if(val instanceof DJProperty){
                DJProperty djProperty = (DJProperty) val;
                
                allProps.add(djProperty);
                        
            }
            
        }

        return allProps;
    }
    
}
