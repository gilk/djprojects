/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parameter.api;

import java.util.Collection;
import java.util.Map;
import javolution.util.FastMap;
import org.dj.listener.api.ListenerAbstr;

/**
 *
 * @author djabry
 */
public class ParametersAbstr extends ListenerAbstr implements Parameters{
    
    private final Map<String, Object> map = new FastMap<String,Object>();
    private boolean notify=false;
    
    
    

    @Override
    public Object getValue(String key) {
        return map.get(key);
    }

    @Override
    public boolean setValue(String key, Object value) {
        
        if(map.containsKey(key)){
            
            Object currentVal = map.get(key);
            
            if(currentVal!=null){
                if(currentVal.equals(value)){               
                    return false;
                } 
            }

        }

        Object oldValue = map.get(key);
        map.put(key, value);
        
        if(isNotify()){
            
            notifyListeners(key,oldValue,value);
        }
        
        return true;
        
    }
    
    
    
    public void notifyListeners(String key,Object oldValue,Object newValue){
        
        firePropertyChange(key, oldValue, newValue);
    }

    @Override
    public Collection<Object> getAllValues() {
        
        return map.values();
    }

    /**
     * @return the notify
     */
    @Override
    public boolean isNotify() {
        return notify;
    }

    /**
     * @param notify the notify to set
     */
    @Override
    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    
}
