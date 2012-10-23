/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.service.api;

import com.google.common.cache.LoadingCache;
import java.util.Collection;
import java.util.Set;
import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */
public abstract class MultiServiceAbstr<T,O extends DJObject> extends ProxyServiceAbstr<T,O> {

    
    public abstract T createObject(Set<T> c);

    @Override
    protected T loadObject(O obj) {
        T get = getCache().asMap().get(obj);
        
        if(get==null){
            get = createObject(this.getAllObjects(obj));
        }
        return get;
    }
    
   
    
}
