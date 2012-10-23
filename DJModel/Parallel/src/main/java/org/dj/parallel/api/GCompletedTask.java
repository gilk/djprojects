/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import com.google.common.collect.Range;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */

public interface GCompletedTask<T extends GSplittableTask<H>,H extends HostInfo, R extends Result<R>> {
    
    Integer getTimeTaken();
    T getOriginalTask();
    R getResult();
    
}
