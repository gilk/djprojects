/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import org.dj.matrix.api.MatrixStorage;



/**
 *
 * @author djabry
 */
public interface MatrixStorageFactory {
    
    MatrixStorage createStorage(int r, int c);
    
}
