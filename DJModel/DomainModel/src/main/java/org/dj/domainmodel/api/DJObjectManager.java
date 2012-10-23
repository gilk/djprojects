/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import java.util.Set;

/**
 *
 * @author djabry
 */
public interface DJObjectManager<O extends DJNodeObject> extends DJNodeObject{

    public static final String PROP_HIERARCHY_DIMENSION= "hierarchyDimension";
    //public static final String PROP_MAPPING = "mapping";
    
    void setHierarchyDimnension(int dim);
    int getHierarchyDimension();
    
    
    Set<O> getManagedChildren();
    

    
}
