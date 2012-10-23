/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.parallel.api.FileMerger;
import org.openide.util.Lookup;
import org.openide.util.lookup.ServiceProvider;
import ucar.ma2.InvalidRangeException;
import ucar.nc2.NetcdfFile;
import ucar.nc2.NetcdfFileWriteable;
import ucar.nc2.Variable;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = FileMerger.Provider.class)
public class NetcdfMerger implements FileMerger.Provider{
    
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

    @Override
    public boolean canMerge(File f1, File f2) {
        try {
            
            NetcdfFile fIn1 = NetcdfFile.open(f1.getPath());
            NetcdfFile fIn2 = NetcdfFile.open(f2.getPath());
            return true;
        
        
        } catch (IOException ex) {
            
            
            Logger.getLogger(NetcdfMerger.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        
       
    }

    @Override
    public File mergeFiles(File f1, File f2) throws IOException {
        
        try {
            NetcdfFile fileIn = NetcdfFile.open(f2.getPath());
            NetcdfFileWriteable fileOut = NetcdfFileWriteable.openExisting(f1.getPath());
            
            int[] origin = fileOut.getVariables().iterator().next().getShapeAll();
                        Iterator<Variable> fIiterator = fileIn.getVariables().iterator();
                        
                        try{
                            
                            while (fIiterator.hasNext()) {
                            Variable vIn = fIiterator.next();
                            if (fileOut.findVariable(vIn.getName()) != null) {

                                String varName = vIn.getName();
                                fileOut.write(varName, origin, vIn.read());

                            }

                        }
                            
                            
                        }finally{
                            fileIn.close();
                            fileOut.close();
                            
                            
                        }

           return f1;
           
           
        } catch (InvalidRangeException ex) {
            Logger.getLogger(NetcdfMerger.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public File mergeFiles(File f1, File f2, File outputFile) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
