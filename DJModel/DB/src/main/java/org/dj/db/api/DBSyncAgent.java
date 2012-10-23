/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db.api;

import com.google.common.util.concurrent.Service;

/**
 *
 * @author djabry
 */
public interface DBSyncAgent extends Service{
    
    void registerDBManager(DBObjectManager mgr);
    void unRegisterDBManager(DBObjectManager mgr);
    void sync();
    
    
}
