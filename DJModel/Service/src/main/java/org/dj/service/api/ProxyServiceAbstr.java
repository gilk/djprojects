/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.service.api;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheLoader.InvalidCacheLoadException;
import com.google.common.cache.LoadingCache;
import java.beans.PropertyChangeEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javolution.util.FastMap;
import javolution.util.FastSet;
import org.dj.domainmodel.api.DJObject;
import org.dj.listener.api.ListenerAbstr;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */
public abstract class ProxyServiceAbstr<T,O extends DJObject> implements Service<T,O> {

    private boolean cacheResults = false;
    private final LoadingCache<O, T> objectCache = CacheBuilder.newBuilder().maximumSize(10000).build(new CacheLoader<O, T>() {

        @Override
        public T load(O k) throws Exception {
            
            T t = loadObject(k);
            if(t!=null){
                k.addPropertyChangeListener(rListener);
            }else{
                
                throw new ExecutionException(new NullPointerException("Service not found"));
            }
            
            return t;
        }
       
    });
    

    
    protected LoadingCache<O,T> getCache(){
        
        return this.objectCache;
    }
    
    protected T loadObject(O obj){
        T out = null;
        Service<T,O> s = findService(obj);
        
        if(s!=null){
            
            out = s.createObject(obj);
        }
        
        return out;
        
    }
    
    private final RemoveListener rListener = new RemoveListener();

   

    public void setCacheResults(boolean tf) {

        this.cacheResults = tf;

        if (!tf) {

            this.clearCache();
        }
    }
    
    
    public void clearCache(){
        Iterator<O> iterator = objectCache.asMap().keySet().iterator();
        
        while(iterator.hasNext()){
            
            O obj = iterator.next();
            obj.removePropertyChangeListener(rListener);
        }

        this.objectCache.invalidateAll();
        this.objectCache.cleanUp();
  
    }

    public boolean isCacheResults() {

        return this.cacheResults;
    }

    public abstract Lookup.Result<? extends Service.Provider<T,O>> getResult();

    public Service<T,O> findService(O obj) {

        for (Provider<T,O> p : getResult().allInstances()) {

            if (p.filter(obj)) {

                return p.getService();
            }
        }

        return null;

    }
    
    public Set<T> getAllObjects(O o){
        
        Set<T> out  = new FastSet<T>();
        
        for(Service<T,O> s: this.findAllServices(o)){
            
            out.add(s.createObject(o));
        }
        
        return out;
    }
    
    
    public Set<Service<T,O>> findAllServices(O o){
        
        Set<Service<T,O>> s = new FastSet<Service<T,O>>();
        for(Provider<T,O> p:getResult().allInstances()){
            
            if(p.filter(o)){
                
                s.add(p.getService());
            }
        }
        
        return s;  
    }
    
   

    @Override
    public T createObject(O obj) {

        T t = null;
        
        if(this.isCacheResults()){
            
            try {
                t=objectCache.get(obj);
            } catch (ExecutionException ex) {
                //Logger.getLogger(ProxyServiceAbstr.class.getName()).log(Level.SEVERE, null, ex);
            } 
        }else{
            
            t= loadObject(obj);
        }

        return t;

    }
    
    private class RemoveListener extends ListenerAbstr{

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            
            if(pce.getPropertyName().equals(MESSAGE_DELETED)){
                O source = (O)pce.getSource();
                objectCache.invalidate(source);
            }
            
            super.propertyChange(pce);
        }
        
        
        
    }
}
