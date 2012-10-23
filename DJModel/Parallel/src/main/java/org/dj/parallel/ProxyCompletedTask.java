/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import javolution.util.FastMap;
import org.dj.parallel.api.GCompletedTask;
import org.dj.parallel.api.GCompletedTaskAbstr;
import org.dj.parallel.api.GSplittableTask;
import org.dj.parallel.api.Result;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public class ProxyCompletedTask<T extends GSplittableTask<H>,H extends HostInfo,R extends Result<R>> extends GCompletedTaskAbstr<T,H,R> {
    
    private final R r;
    
    private static Integer calculateTimeTaken(Set<? extends GCompletedTask> tasks){
        Map<HostInfo,Integer> times=new FastMap<HostInfo,Integer>();
        Iterator<? extends GCompletedTask> iterator = tasks.iterator();
        while(iterator.hasNext()){
            GCompletedTask next = iterator.next();
            Integer t = next.getTimeTaken();
            HostInfo hI = next.getOriginalTask().getHostInfo();
            Integer tOld = times.get(hI);
            
            if(tOld!=null){
                
                t=tOld+t;
            }
            
            times.put(hI, t);
        }
        Iterator<Entry<HostInfo, Integer>> iterator1 = times.entrySet().iterator();
        Integer totalTime = 0;
        
        
        while(iterator1.hasNext()){
            Entry<HostInfo, Integer> next = iterator1.next();
            
            
            if(next.getValue()>totalTime){
                
                totalTime = next.getValue();
            }
            
        }
        
        return totalTime;
        
    }

    
    public ProxyCompletedTask(Set<GCompletedTask<T,H,R>> completedTasks) throws IllegalArgumentException{
        
        super((!completedTasks.isEmpty())?completedTasks.iterator().next().getOriginalTask():null, calculateTimeTaken(completedTasks));
        Iterator<GCompletedTask<T, H, R>> iterator = completedTasks.iterator();
        
        R rOut = iterator.next().getResult();
        
        while(iterator.hasNext()){
            rOut = rOut.mergeWith(iterator.next().getResult());
            
        }
        
        r= rOut;
 
    }
    
    

    @Override
    public R getResult() {
        return r;
    }
    
}
