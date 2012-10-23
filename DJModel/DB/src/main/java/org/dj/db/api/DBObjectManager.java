/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import java.util.Set;
import org.dj.domainmodel.api.DJObjectManager;

/**
 *
 * @author djabry
 */

//Interface for manager of domain objects that are automatically reloaded on startup
public interface DBObjectManager<O extends DBObject> extends DBObject,DJObjectManager<O>{
    

    void refreshFromDB();

    void refreshDB();
    
    String getDBName();

    
    
    
}
