/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public abstract class GCompletedTaskAbstr<T extends GSplittableTask<H>,H extends HostInfo,R extends Result<R>> implements GCompletedTask<T,H,R>{
    
    private final int duration;
    private final T tsk;
    
    public GCompletedTaskAbstr(T t, int duration){
        
        this.duration=duration;
        this.tsk=t;
    }

    @Override
    public Integer getTimeTaken() {
        return duration;
    }

    @Override
    public T getOriginalTask() {
        return tsk;
    }

    
}
