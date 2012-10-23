/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import java.util.Set;
import org.dj.db.api.DBObject;
import org.dj.db.api.DBObjectManager;
import org.dj.db.api.DBSyncAgent;
import org.dj.db.api.DJObjectDB;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.openide.util.Lookup;

/**
 *
 * @author djabry
 */


public abstract class DBObjectManager2<O extends DBObject> extends DJObjectManagerAbstr<O> implements DBObjectManager<O>{

    private static final DBSyncAgent sA = Lookup.getDefault().lookup(DBSyncAgent.class);
    //Generate static Id based on class name
    
    public DBObjectManager2(){
        super();
        sA.registerDBManager(this);
    }
    @Override
    public String getId() {
        return this.getClass().getName();
    }

    @Override
    public String getDBName() {
        return DJObjectDB.DEFAULT_DATABASE;
    }

    @Override
    public void refreshDB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void refreshFromDB() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    

    
    
}
