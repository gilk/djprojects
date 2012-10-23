/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import com.google.common.collect.BiMap;
import com.google.common.collect.Range;
import java.util.Set;
import org.dj.index.api.IntegerIndex;


/**
 *
 * @author djabry
 */
public interface MatrixOperations {
    
    //void release(DJMatrix m);
    
    //m1+m2
    DJMatrix add(DJMatrix m1,DJMatrix m2);
    
    //m1*m2
    DJMatrix multiply(DJMatrix m1,DJMatrix m2);
    
    //m1'*m2
    DJMatrix transMultiply(DJMatrix m1, DJMatrix m2);
    
    //m1*d
    DJMatrix multiply(DJMatrix m1, double d);
    
    //All elements are multiplied by corresponding elements, output is same size as m1
    DJMatrix elementMultiply(DJMatrix m1, DJMatrix m2);

    //m1\m2
    DJMatrix solve(DJMatrix m1, DJMatrix m2);
    
    //Iterator going over elements of m
    DJMatrixIterator iteratorFor(DJMatrix m, boolean allElements);

    //Return a view of m
    DJMatrix subMatrix(DJMatrix m,int startRow, int endRow, int startCol, int endCol);
    
    //m1'
    DJMatrix transpose(DJMatrix m1);
    
    //|m|
    double determinant(DJMatrix m);
    
    //m1.m2
    double dot(DJMatrix m1, DJMatrix m2);
    
    //tr(m)
    double trace(DJMatrix m);
    
    //sum of all elements
    double elementSum(DJMatrix m);

    //m^(-1)
    DJMatrix inverse(DJMatrix m);
    
    //m1'*m2
    DJMatrix transposeMultiply(DJMatrix m1,DJMatrix m2);

    
    //m1'\m2
    DJMatrix transposeSolve(DJMatrix m1, DJMatrix m2);
    
    //m1-m2
    DJMatrix minus(DJMatrix m1, DJMatrix m2);
    
    //Return a copy of m
    DJMatrix copy(DJMatrix m);
    
    DJMatrix subMatrix(DJMatrix m, Set<Integer> rows, Set<Integer> cols);
    //void clearCache();
    
    //create a submatrix from a bimap of the row and column ranges
    DJMatrix subMatrix(DJMatrix m, BiMap<Integer,Integer> rows, BiMap<Integer,Integer> cols);
    
    
    DJMatrix subMatrix(DJMatrix m, IntegerIndex rows,IntegerIndex cols);
    
    
    
    DJMatrix fromRanges(DJMatrix m, Set<Range<Integer>> rowRanges, Set<Range<Integer>> colsRange);
    
    //boolean isUseCache();
    //void setUseCache(boolean tf);
    
    boolean allElementsEqual(DJMatrix m1, DJMatrix m2,double tol);
    
}
