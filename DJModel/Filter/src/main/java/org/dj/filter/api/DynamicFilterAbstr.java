/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.filter.api;

import org.dj.listener.api.ListenerAbstr;

/**
 *
 * @author djabry
 */
public abstract class DynamicFilterAbstr extends ListenerAbstr implements DynamicFilter{
    
    
    
    private Filter filter;
    
    public DynamicFilterAbstr(){
        
        filter = new Filter(){

            @Override
            public boolean filter(Object obj) {
                return true;
            }
        };
    }

    @Override
    public boolean filter(Object obj) {
        return getFilter().filter(obj);
    }

    /**
     * @return the filter
     */
    public Filter getFilter() {
        return filter;
    }

    /**
     * @param filter the filter to set
     */
    @Override
    public void setFilter(Filter filter) {
        
        if(!this.filter.equals(filter)){
            Filter oldFilter = this.filter;
            this.filter = filter;
            firePropertyChange(PROP_FILTER,oldFilter,this.filter);
        }
       
    }
    
}
