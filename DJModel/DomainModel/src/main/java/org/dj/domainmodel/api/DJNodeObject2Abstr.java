/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Sets;
import java.util.Set;
import lombok.Data;

/**
 *
 * @author djabry
 */
@Data
public class DJNodeObject2Abstr extends DJObject2Abstr implements DJNodeObject2{

    private final BiMap<Integer,Set<DJNodeObject2>> allChildren=HashBiMap.create();
    private final BiMap<Integer,DJNodeObject2> allParents = HashBiMap.create();

    @Override
    public Set<DJNodeObject2> getChildren() {
        return this.getChildren(DEFAULT_DIMENSION);
    }

    @Override
    public Set<DJNodeObject2> getChildren(int hDim) {
        Set<DJNodeObject2> get = this.getAllChildren().get(hDim);
        if(get==null){
            get=Sets.newHashSet();
        }
        
        return get;
    }
    
    
    
    
    
    
}
