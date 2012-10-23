/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task;

import java.io.IOException;
import org.lblrtm.task.api.TaskManager;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {

    private static final TaskManager mgr = Lookup.getDefault().lookup(TaskManager.class);
    @Override
    public void restored() {
        // TODO
    }

    @Override
    public void close() {
        try {
            mgr.close();
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        super.close();
        
    }
    
    
}
