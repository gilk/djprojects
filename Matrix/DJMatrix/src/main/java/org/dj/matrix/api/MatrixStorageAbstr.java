/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import com.google.common.collect.Range;
import com.google.common.collect.Ranges;
import java.io.*;
import java.util.Iterator;
import org.dj.matrix.DJMatrixIteratorImpl;
import org.openide.util.Exceptions;

/**
 *
 * @author djabry
 */
public abstract class MatrixStorageAbstr implements MatrixStorage, Serializable {

    private final int numCols;
    private final int numRows;

    public MatrixStorageAbstr(int r, int c) {
        this.numRows = r;
        this.numCols = c;
        this.initialiseStorage();

    }

    @Override
    public int getNumberOfColumns() {
        return numCols;
    }

    @Override
    public int getNumberOfRows() {
        return numRows;
    }

    @Override
    public int indexFromRC(int r, int c) {
        return r * this.getNumberOfColumns() + c;
    }

    @Override
    public double getValueAt(int r, int c) {
        return this.getValueAt(indexFromRC(r, c));
    }

    @Override
    public DJMatrixIterator iterator(boolean allElements) {
        return new DJMatrixIteratorImpl(this, allElements);
    }

    @Override
    public int rowFromIndex(int index) {
        return index / this.getNumberOfColumns();
    }

    @Override
    public int columnFromIndex(int index) {
        return index % this.getNumberOfColumns();
    }

    @Override
    public Range<Double> findValueRange() {
        
        double minVal = Double.POSITIVE_INFINITY;
        double maxVal = Double.NEGATIVE_INFINITY;
        Iterator<Integer> iI = this.getIndexIterator(true);
        int counter = 0;
        while(iI.hasNext()){
            Integer next = iI.next();
            double testVal = this.getValueAt(next);
            if(testVal<minVal){
                
                minVal = testVal;
            }
            
            if(testVal>maxVal){
                
                maxVal = testVal;
            }
            counter++;
            
        }
        
        if( counter==0){
            
            minVal = 0.0;
            maxVal = 0.0;
        }
        
        return Ranges.closed(minVal, maxVal);
    }
    
    

    @Override
    public void setValueAt(int r, int c, double d) {
        this.setValueAt(this.indexFromRC(r, c), d);
    }

    @Override
    public boolean isSquare() {
        return this.getNumberOfColumns() == this.getNumberOfRows();
    }

    @Override
    public int getNumberOfElements() {

        return this.getNumberOfColumns() * this.getNumberOfRows();
    }

    @Override
    public boolean isInRange(int index) {

        int row = rowFromIndex(index);
        int col = columnFromIndex(index);

        if (row >= 0
                && row < getNumberOfRows()
                && col >= 0
                && col < getNumberOfColumns()) {


            return true;
        }

        return false;

    }

    public abstract void initialiseStorage();

    @Override
    public void saveToFile(String filePath) {
        try {
            ObjectOutputStream stream = null;
            FileOutputStream fOS = new FileOutputStream(filePath);
            stream = new ObjectOutputStream(fOS);
            try {
                stream.writeObject(this);
                stream.flush();

            } finally {

                stream.close();
                fOS.close();
            }


        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }




    }

    @Override
    public void exportToCSV(String fileName) {
        try {
            FileWriter writer = new FileWriter(fileName);
            for (int i = 0; i < this.getNumberOfRows(); i++) {

                for (int j = 0; j < this.getNumberOfColumns(); j++) {

                    writer.append(""+this.getValueAt(i, j));
                    
                    if(j<this.getNumberOfColumns()-1){
                        
                         writer.append(","); 
                        
                    }
                    
                }
                writer.append("\n");
            }
            
            writer.flush();
            writer.close();


        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }

    }
}
