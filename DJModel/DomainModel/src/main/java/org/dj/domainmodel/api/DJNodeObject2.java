/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import com.google.common.collect.BiMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author djabry
 */
public interface DJNodeObject2 extends DJObject2{
    
    public static final String PROP_CHILDREN = "children";
    public static final String MESSAGE_CHILD_ADDED = "added";
    public static final String MESSAGE_CHILD_REMOVED = "removed";
    public static final String PROP_PARENTS = "parents";
    public static final String MESSAGE_REMOVE_ME = "deleteMe";
    public static final String MESSAGE_RECYCLE_ME = "recycleMe";
    //public static final String MESSAGE_DELETED = "deleted";
    public static final Integer DEFAULT_DIMENSION = 0;
    
    Set<DJNodeObject2> getChildren();
    Set<DJNodeObject2> getChildren(int hDim);
    
    BiMap<Integer,Set<DJNodeObject2>> getAllChildren();
    BiMap<Integer,DJNodeObject2> getAllParents();
    
}
