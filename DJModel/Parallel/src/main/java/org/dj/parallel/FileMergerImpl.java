/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.dj.fileutilities.api.FileUtilities;
import org.dj.parallel.api.FileMerger;
import org.openide.util.Lookup;
import org.openide.util.Lookup.Result;
import org.openide.util.Lookup.Template;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider (service = FileMerger.class)
public class FileMergerImpl implements FileMerger{
    
    private static final Result<FileMerger.Provider> providers = Lookup.getDefault().lookup(new Template(FileMerger.Provider.class));
    private static final FileUtilities fU = Lookup.getDefault().lookup(FileUtilities.class);

    @Override
    public File mergeFiles(File f1, File f2) throws IOException {
        
        Iterator<? extends Provider> iterator = providers.allInstances().iterator();
        while (iterator.hasNext()){
            Provider p = iterator.next();
            
            if(p.canMerge(f1, f2)){
                
                return p.mergeFiles(f1, f2);
            }
        }
        return null;
        
    }

    @Override
    public File mergeFiles(File f1, File f2, File outputFile) throws IOException {
        
        if(!outputFile.equals(f1)){
            
            fU.copyFile(fU.fromFile(f1), fU.fromFile(outputFile));
        }
        
        return mergeFiles(outputFile,f2);
        
    }
    
}
