/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.solarscape.data.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import javax.swing.filechooser.FileFilter;
import org.dj.db.api.DBObjectManager;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;

/**
 *
 * @author djabry
 */
public interface SolarPlantManager extends DBObjectManager<SolarPlant>, DJNodeObject.Source {
    public static final String DEFAULT_BROWSE_DIR = "solarPlantManagerDefaultDir";
    public static final FileFilter CSV_FILTER  = new FileFilter() {

        @Override
        public boolean accept(File f) {
           if(f.getPath().toLowerCase().endsWith(".csv")){
               return true;
           }
           return false;
        }

        @Override
        public String getDescription() {
            return "CSV file";
        }

       
    };
    void addFromFile(String file) throws IOException, FileNotFoundException;
   
    
}
