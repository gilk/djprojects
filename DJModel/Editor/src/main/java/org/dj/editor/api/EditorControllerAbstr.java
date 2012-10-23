/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.editor.api;

import java.awt.Component;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.swing.JComponent;
import org.dj.domainmodel.api.DJObject;
import org.dj.domainmodel.api.DJObjectAbstr;
import org.dj.property.api.DJProperty;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public abstract class EditorControllerAbstr<T extends DJObject> extends DJObjectAbstr implements EditorController<T>{
    
    public EditorControllerAbstr(T obj){
        this.setPropertyValue(PROP_OBJECT, obj);
        
        this.setObject(obj);

    }
    
    public void setObject(DJObject obj){
        
        T object = this.getObject();
        
        if(object !=null){
            
            object.removePropertyChangeListener(this);
        }
        
        
        this.setPropertyValue(PROP_OBJECT, obj);
        
        if(obj!=null){
            
             obj.addPropertyChangeListener(this);
        }
        
       
    }
    
    public void setComponent(JComponent p){
        
        this.setPropertyValue(PROP_COMPONENT, p);
    }
    
    @Override
    public void close(){
        //Automatically done by ListenerAbstr class
        //getObject().removePropertyChangeListener(this);
        
    }
    
    public void opened(){
        
        //getObject().addPropertyChangeListener(this);
    }

    @Override
    public JComponent getComponent() {
       return (JComponent) this.getPropertyValue(PROP_COMPONENT);
    }

    

    @Override
    public void updateComponent() {
       Iterator<Entry<String, Component>> iterator = this.getComponentMap().entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String, Component> next = iterator.next();
            DJProperty prop = getObject().getProperty(next.getKey());
            this.updateComponentWithValue(next.getValue(), prop.getValue());
        }
    }
    
    protected abstract void updateComponentWithValue(Component c, Object o);

    @Override
    public void updateObject() {
        Iterator<Entry<String, Component>> iterator = this.getComponentMap().entrySet().iterator();
        while(iterator.hasNext()){
            Entry<String, Component> next = iterator.next();
            DJProperty prop = getObject().getProperty(next.getKey());
            prop.setValue(getComponentValue(next.getValue()));
        }
    }
    
    protected abstract Object getComponentValue(Component c);

    @Override
    public void propertyChange(PropertyChangeEvent pce) {
        super.propertyChange(pce);
        Map<String, Component> componentMap = getComponentMap();
        String propertyName = pce.getPropertyName();
        
        if(componentMap.containsKey(propertyName)){
            
            updateComponentWithValue(componentMap.get(propertyName),
                    getObject().getPropertyValue(propertyName));
        }
        
        
    }
    
    

    @Override
    public T getObject() {
        return (T) this.getPropertyValue(PROP_OBJECT);
    }
    
    @Override
    public void requestActive(){
        
    }
    
    protected abstract Map<String,Component> getComponentMap();
    

    
}
