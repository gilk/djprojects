/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.util.Set;
import org.dj.index.api.IntegerIndex;

/**
 *
 * @author djabry
 */
public interface GBins<B extends GBin> {
    
    void putRanges(IntegerIndex ranges);
    Set<B> getBins();
    
}
