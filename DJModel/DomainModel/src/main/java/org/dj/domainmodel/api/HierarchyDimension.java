/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

/**
 *
 * @author djabry
 */
public interface HierarchyDimension extends DJObject{
    
   
    public static final HierarchyDimension DEFAULT = new DefaultHierarchyDimension();
    
    public interface Provider {
        
        HierarchyDimension getHierarchyDimension();
    }
    

}
