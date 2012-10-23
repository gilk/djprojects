/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import com.google.common.collect.Range;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javolution.util.FastMap;
import org.dj.index.api.IntegerIndex;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public class HostRanker extends GParallelTaskAbstr{
    
    private final GParallelTaskAbstr t;
    
    public HostRanker(GParallelTaskAbstr t,IntegerIndex ranges){
        
        super(t.getTask(),ranges,generateEqualHostTimes(t.getHosts()),false);
        this.t=t;
    }

    @Override
    public GCompletedTask generateCompletedTaskFrom(GSplittableTask tsk, Integer duration) {
        return t.generateCompletedTaskFrom(tsk, duration);
    }
    
    private static Map<HostInfo, Integer> generateEqualHostTimes(Set<? extends HostInfo> hosts) {

        Map<HostInfo, Integer> hostTimes = new FastMap<HostInfo, Integer>(hosts.size());
        Iterator<? extends HostInfo> iterator = hosts.iterator();
        while (iterator.hasNext()) {
            HostInfo next = iterator.next();
            hostTimes.put(next, 1000);

        }
        return hostTimes;
    }
    
}
