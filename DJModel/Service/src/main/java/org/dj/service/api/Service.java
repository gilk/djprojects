/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.service.api;

import org.dj.domainmodel.api.DJObject;
import org.dj.filter.api.Filter;

/**
 *
 * @author djabry
 */
public interface Service<T,O extends DJObject>{

    T createObject(O obj);
    
    
    public interface Provider<T,O extends DJObject> extends Filter<O> {
        
        Service<T,O> getService();
    }
    
    
}
