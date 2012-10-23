/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import ucar.ma2.Array;

/**
 *
 * @author djabry
 */
public interface MatrixFactory {
    
    DJMatrix createVector(Double[] elements, boolean rowVector);
    DJMatrix createVector(List<Double> elements, boolean rowVector);
    DJMatrix createVector(int numElements, boolean rowVector);
    DJMatrix createRandomMatrix(int r, int c, double minVal,double maxVal);
    DJMatrix createRandomVector(int numElements,boolean rowVec, double min, double max);
    DJMatrix createDiagonalMatrix(Double[] diagonal);
    DJMatrix createDiagonalMatrix(List<Double> elements);
    DJMatrix createDiagonalMatrix(int diagLength);
    DJMatrix createMatrix(int rows, int cols);
    DJMatrix createMatrixFromStorage(MatrixStorage mS);
    DJMatrix createFromArray(Array arr);
    DJMatrix createIdentityMatrix(int size);
    DJMatrix createFromFile(String fileName) throws IOException,FileNotFoundException;
    DJMatrix createBlockDiagonalMatrix(List<DJMatrix> diags);
    //File getTempDir();
    
    
}
