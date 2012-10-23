/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author djabry
 */
public interface MatrixIO {
    public static final String MATRIX_VARIABLE_NAME = "djmatrix";
    void saveToFile(DJMatrix mat,String fileName,MatrixFileType mFT) throws IOException;
    void saveToFile(DJMatrix mat, String fileName) throws IOException;
    DJMatrix loadFile(String fileName) throws IOException;
    DJMatrix loadFromStream(InputStream str, MatrixFileType mFT) throws IOException;
    
    boolean isValidFile(File f) throws IOException;
    void spawnSaver(DJMatrix mat) throws IOException;
    DJMatrix spawnOpener() throws IOException;
}
