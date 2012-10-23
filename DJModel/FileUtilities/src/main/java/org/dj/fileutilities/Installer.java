/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.fileutilities;

import java.io.IOException;
import org.dj.fileutilities.api.FileUtilities;
import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;

public class Installer extends ModuleInstall {
    
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

    @Override
    public void restored() {
        // TODO
    }

    @Override
    public void close() {

            fU.delete(fU.fromFile(fU.getTempDir()));
            

        super.close();
    }
    
    
}
