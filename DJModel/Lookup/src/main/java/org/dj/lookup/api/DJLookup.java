/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.lookup.api;

import java.util.Map;
import javolution.util.FastMap;
import org.openide.util.lookup.AbstractLookup;

/**
 *
 * @author djabry
 */
public class DJLookup extends AbstractLookup{
    
    private final Map<Object,DJPair> objects = new FastMap<Object,DJPair>();
    
    public void addObject(Object o){
        
        if(!this.objects.containsKey(o)){ 
            this.addPair(new DJPair(o));
        }
    }
    
    public void removeObject(Object o){
        
        if(this.objects.containsKey(o)){
            
            this.removePair(objects.get(o));
            this.objects.remove(o);
        }
        
    }
    
}
