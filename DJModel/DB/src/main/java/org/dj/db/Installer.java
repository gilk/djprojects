/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.db;

import org.dj.db.api.DBSyncAgent;
import org.dj.db.api.DJObjectDB;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.modules.ModuleInstall;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {
    
    private static final DBSyncAgent sA = Lookup.getDefault().lookup(DBSyncAgent.class);

    @Override
    public void restored() {
        // TODO
        //sA.start();
        
    }
    
     @Override
    public boolean closing() {
        DJObjectDB dB = Lookup.getDefault().lookup(DJObjectDB.class);
        ProgressHandle handle = ProgressHandleFactory.createHandle("Updating database...");
        handle.start();
        dB.close();
        handle.finish();
        // sA.stop();
        return super.closing();
    }
}
