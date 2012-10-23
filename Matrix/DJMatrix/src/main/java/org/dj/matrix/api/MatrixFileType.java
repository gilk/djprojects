/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author djabry
 */
public enum MatrixFileType {
    
    CSV("csv","Comma-separated values (.csv)"),
    NC("nc","NetCDF (.nc)"),
    MAT("mat","Matlab (.mat)");
    
    public final String fileExtension;
    public final String fullName;
    public final FileFilter fileFilter;
    
    MatrixFileType(String fileExtension,String fullName){
        this.fileExtension=fileExtension;
        this.fullName=fullName;
        this.fileFilter=new FileFilter(){

            @Override
            public boolean accept(File file) {
                if(file.getPath().toLowerCase().endsWith(MatrixFileType.this.fileExtension)){
                    return true;
                }
                return false;
            }

            @Override
            public String getDescription() {
                return MatrixFileType.this.fullName;
            }
            
            
        };
        
    }
    
    
    
}
