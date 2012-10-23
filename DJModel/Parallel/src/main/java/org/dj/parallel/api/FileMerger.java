/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 *
 * @author djabry
 */
public interface FileMerger {
    
    File mergeFiles(File f1, File f2) throws IOException;
    File mergeFiles(File f1, File f2, File outputFile) throws IOException;
    
    public interface Provider extends FileMerger{
        
        boolean canMerge(File f1, File f2);
        
    }
    
}
