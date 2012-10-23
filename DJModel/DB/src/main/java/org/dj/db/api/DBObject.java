/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import javax.persistence.Entity;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObject;

/**
 *
 * @author djabry
 */

public interface DBObject extends DJNodeObject{
    
     public static final String PROP_ALL_PARENT_IDS = "allParentIds";
    
}
