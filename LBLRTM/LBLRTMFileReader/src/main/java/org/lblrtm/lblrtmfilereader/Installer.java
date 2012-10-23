/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilereader;

import org.openide.modules.ModuleInstall;
import org.openide.util.Exceptions;
import org.openide.util.Lookup;
import ucar.nc2.NetcdfFile;
import ucar.nc2.iosp.IOServiceProvider;

public class Installer extends ModuleInstall {

    @Override
    public void restored() {
        try {
            // Register IOSP for reading LBLRTM formatted files
              
               for(Class c:Lookup.getDefault().lookupResult(IOServiceProvider.class).allClasses()){
                   
                   NetcdfFile.registerIOProvider(c);
               }
 
            
        } catch (IllegalAccessException ex) {
            Exceptions.printStackTrace(ex);
        } catch (InstantiationException ex) {
            Exceptions.printStackTrace(ex);
        }
    }
}
