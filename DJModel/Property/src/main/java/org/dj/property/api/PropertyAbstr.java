/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.property.api;

import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.filter.api.Filter;
import org.dj.parameter.api.FilteredParametersAbstr;

/**
 *
 * @author djabry
 */
public class PropertyAbstr<T> extends FilteredParametersAbstr implements DJProperty<T>{
    
    public PropertyAbstr(){
        
        this("",null);
        
    }
    
    public PropertyAbstr(String name, T value){
        
        this(name,value,"","","",new Filter<T>(){

            @Override
            public boolean filter(T obj) {
                return true;
            }
        },true,true,null);
        
        
    }

    public PropertyAbstr(String name,
            T value,
            String displayName, 
            String shortDescription, 
            String description,
            Filter filter,
            boolean canRead,
            boolean canWrite,
            Class propEditorClass){
        
        super();
        setFilter(filter);
        setPropertyName(name);
        setValue(value);
        setDisplayName(displayName);
        setShortDescription(shortDescription);
        setDescription(description);
        setCanRead(canRead);
        setCanWrite(canWrite);
        setPropertyEditorClass(propEditorClass);

    }
    
    public void setPropertyName(String name){
        setValue(PROP_PROPERTY_NAME,name);
    }

    @Override
    public String getPropertyName() {
        return (String) getValue(PROP_PROPERTY_NAME);
    }


    @Override
    public String getDisplayName() {
        return (String) getValue(PROP_DISPLAY_NAME);
    }

    @Override
    public String getShortDescription() {
        return (String) getValue(PROP_SHORT_DESCRIPTION);
    }

    @Override
    public void setDescription(String description) {
        setValue(PROP_DESCRIPTION,description);
    }

    @Override
    public void setShortDescription(String shortDescription) {
        setValue(PROP_SHORT_DESCRIPTION,shortDescription);
    }

    @Override
    public void setDisplayName(String displayName) {
        setValue(PROP_DISPLAY_NAME,displayName);
    }

    @Override
    public String getDescription() {
        return (String) getValue(PROP_DESCRIPTION);
    }

    @Override
    public T getValue() {
        return (T)getValue(PROP_VALUE);
    }

    @Override
    public boolean setValue(T value) {
        return setValue(PROP_VALUE,value);
    }

    @Override
    public boolean isCanRead() {
        return (Boolean)getValue(PROP_CAN_READ);
    }

    @Override
    public boolean isCanWrite() {
        return (Boolean)getValue(PROP_CAN_WRITE);
    }

    @Override
    public void setCanRead(boolean tf) {
        setValue(PROP_CAN_READ,tf);
    }

    @Override
    public void setCanWrite(boolean tf) {
        setValue(PROP_CAN_WRITE,tf);
    }

    @Override
    public void setFilter(Filter filter) {
        setFilter(PROP_VALUE, filter);
    }



    @Override
    public void notifyListeners(String key, Object oldValue, Object newValue) {
        //Only notify when the value has been changed (not the description etc.)
        if(key.equals(PROP_VALUE)){
            
            super.notifyListeners(getPropertyName(), oldValue, newValue);
            
        } 
    }

    //By default return the stored value
    @Override
    public Object getDisplayValue() {
        return getValue();
    }

     //Use the set value method by default
    @Override
    public void setDisplayValue(Object value) {
        this.setValue((T)value);
    }

    @Override
    public PropertyEditor getPropertyEditor() {
        
        Class<PropertyEditor> c = getPropertyEditorClass();
        PropertyEditor p = PropertyEditorManager.findEditor(getValue().getClass());
        
        if(c!=null){
            try {
                p=c.newInstance();
            } catch (InstantiationException ex) {
                Logger.getLogger(PropertyAbstr.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(PropertyAbstr.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        }
        
         return p;
    }

    @Override
    public <P extends PropertyEditor> void setPropertyEditorClass(Class<P> propEditorClass) {
        this.setValue(PROP_PROPERTY_EDITOR_CLASS, propEditorClass);
        
    }

    @Override
    public <P extends PropertyEditor> Class<P> getPropertyEditorClass() {
        return (Class<P>) getValue(PROP_PROPERTY_EDITOR_CLASS);
    }
    
    
    
}
