/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parameter.api;


import java.util.Map;
import javolution.util.FastMap;
import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public class FilteredParametersAbstr extends ParametersAbstr implements FilteredParameters{
    
    private final Map<String, Filter> filters= new FastMap<String,Filter>();;
    
    public FilteredParametersAbstr(){
        super();

    }
    
    @Override
    public boolean setValue(String key, Object value){
        
        Filter filter = filters.get(key);
        
        if(filter!=null){
            
            if(!filter.filter(value)){
                return false;
            }   
        }
        
        return super.setValue(key, value);
        
    }
    
    

    @Override
    public void setFilter(String key, Filter filter) {
        
        
        //Filter oldFilter = filters.get(key);
        filters.put(key, filter);
        
        if(!filter.filter(getValue(key))){
            
            setValue(key,null);
            
        }
        
        firePropertyChange(PROP_FILTER_CHANGE,key,filter);

        
    }
    
}
