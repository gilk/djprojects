/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import com.google.common.collect.Range;
import java.util.List;
import java.util.Set;
import org.dj.remote.api.HostInfo;

/**
 *
 * @author djabry
 */
public interface GBin<T extends HostInfo> {
    
    Set<Range<Integer>> getRanges();
    boolean putRange(Range<Integer> r);
    int getRemainingCapacity();
    int getTotalCapacity();
    T getHostInfo();
    
}
