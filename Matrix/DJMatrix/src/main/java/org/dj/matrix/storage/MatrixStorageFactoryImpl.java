/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;


import org.dj.matrix.api.MatrixStorageFactory;
import org.dj.matrix.api.MatrixStorage;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author djabry
 */
@ServiceProvider(service = MatrixStorageFactory.class)
public class MatrixStorageFactoryImpl implements MatrixStorageFactory{

    @Override
    public MatrixStorage createStorage(int r, int c) {
       return new DenseSerializedMatrixStorage(r,c);
    }
    
}
