/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import java.util.Map;
import java.util.Set;

/**
 *
 * @author djabry
 */
public interface DJNodeObject extends DJObject {
    
    public static final String PROP_CHILDREN = "children";
    public static final String MESSAGE_CHILD_ADDED = "added";
    public static final String MESSAGE_CHILD_REMOVED = "removed";
    public static final String PROP_PARENTS = "parents";
    public static final String PROP_HTML_DISPLAY_NAME  = "htmlDisplayName";
    public static final String MESSAGE_REMOVE_ME = "deleteMe";
    public static final String MESSAGE_RECYCLE_ME = "recycleMe";
    //public static final String MESSAGE_DELETED = "deleted";
    public static final Integer DEFAULT_DIMENSION = 0;
    
    public interface Source{
        
        DJNodeObject createObject();
    }
    
    Set<DJNodeObject> getChildren();
    DJNodeObject getParent();
    
    String getHtmlDisplayName();
    
    
    Map<Integer,Set<DJNodeObject>> getAllChildren();
    Map<Integer,DJNodeObject> getAllParents();
    
    
    void addChild(DJNodeObject child, Integer dim);
    void removeChild(DJNodeObject child, Integer dim);

    void addChild(DJNodeObject obj);
    void removeChild(DJNodeObject obj);

    
    
}
