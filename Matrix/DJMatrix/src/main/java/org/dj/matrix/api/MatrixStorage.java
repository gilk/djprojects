/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import com.google.common.collect.Range;
import java.io.Closeable;
import java.io.Serializable;
import java.util.Iterator;

/**
 *
 * @author djabry
 */
public interface MatrixStorage extends Serializable, Closeable{
    
    int getNumberOfColumns();
    int getNumberOfRows();
    int getNumberOfElements();
    double getValueAt(int r, int c);
    
    double getValueAt(int index);
    Range<Double> findValueRange();
    void setValueAt(int index, double value);
    
    Iterator<Integer> getIndexIterator(boolean allElements);
    DJMatrixIterator iterator(boolean allElements);
    
    boolean isInRange(int index);
    
    void saveToFile(String fileName);
    void exportToCSV(String fileName);
    
    int rowFromIndex(int index);
    int columnFromIndex(int index);
    int indexFromRC(int r, int c);
    
    
    void setValueAt(int r, int c, double d);
    int getCardinality();
    boolean isDiagonal();
    boolean isSquare();
    boolean isSerialized();
    boolean isDense();
    
}
