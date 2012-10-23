/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import com.google.common.collect.Range;
import java.io.Closeable;
import java.io.IOException;
import java.util.Set;
import org.dj.index.api.IntegerIndex;

/**
 *
 * @author djabry
 */
public interface DJMatrix{
    
    int getNumberOfColumns();
    int getNumberOfRows(); 
    double getValueAt(int r, int c);
    void setValueAt(int r, int c, double val);
    //double[] getDiagonal();
    boolean isVector();
    boolean isDiagonal();
    boolean isSquare();
    
    void saveToFile(String fileName)throws IOException;
    void saveToFile(String fileName,MatrixFileType mFT) throws IOException;
    
    void saveToFile() throws IOException;
    
    DJMatrix transpose();
    DJMatrix multiply(DJMatrix mat);
    DJMatrix multiply(double val);
    DJMatrix transMultiply(DJMatrix mat);
    DJMatrix solve(DJMatrix mat);
    DJMatrix plus(DJMatrix mat);
    DJMatrix elementMultiply(DJMatrix mat);
    MatrixStorage getMatrixStorage();
    DJMatrix inverse();
    DJMatrix minus(DJMatrix mat);
    DJMatrix subMatrix(Set<Integer> rows, Set<Integer> cols);
    DJMatrix subMatrix(IntegerIndex rows, IntegerIndex cols);
    DJMatrix fromRanges(Set<Range<Integer>> rowRange, Set<Range<Integer>> colRange);
    DJMatrix extractVector(boolean extractRow, int element);
    boolean allElementsEqual(DJMatrix m, double tol);
    DJMatrix copy();
    int getCardinality();
    
    DJMatrixIterator iterator(boolean allElements);
    //DJMatrixIterator allElementsIterator();
    
    DJMatrixIterator getIteratorFor(boolean rowMajor, int startRow, int startCol, int endRow, int endCol);
    
    double elementSum();
    double trace();
    double determinant();
    int getNumElements();
    int getLength();
    
    int getDimensionLength(int dim);
    String getDimensionName(int dim);
    void setDimensionName(int dim, String name);

    String getTitle();
    void setTitle(String title);
    
    
    
    
    
}
