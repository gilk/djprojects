/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.storage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Iterator;
import no.uib.cipr.matrix.DenseMatrix;
import no.uib.cipr.matrix.LowerSymmBandMatrix;
import no.uib.cipr.matrix.Matrices;
import no.uib.cipr.matrix.Matrix;
import no.uib.cipr.matrix.MatrixEntry;
import org.dj.matrix.api.MatrixStorageAbstr;

/**
 *
 * @author djabry
 */
public class MTJMatrixStorage extends MatrixStorageAbstr implements Serializable{
    
    private final Matrix m;
    
    
    public MTJMatrixStorage(Matrix m){
        
        super(m.numRows(),m.numColumns());
        this.m=m;
        
    }
    
    public Matrix getMatrix(){
        return m;
        
    }

    @Override
    public double getValueAt(int index) {
        return this.m.get(this.rowFromIndex(index), this.columnFromIndex(index));
    }

    @Override
    public void setValueAt(int index, double value) {
        this.m.set(this.rowFromIndex(index), this.columnFromIndex(index), value);
    }

    @Override
    public Iterator<Integer> getIndexIterator(boolean allElements) {
        return new MTJIterator(allElements);
    }
    
    private void readObject(ObjectInputStream oIS) throws ClassNotFoundException, IOException{
        
        oIS.defaultReadObject();
        initialiseStorage();
        Iterator<Integer> iterator = getIndexIterator(false);
        
        while(iterator.hasNext()){
            Integer index = iterator.next();
            setValueAt(index, oIS.readDouble()); 
            
        } 
        
    }
    
    private void writeObject(ObjectOutputStream oOS) throws IOException{
        
        oOS.defaultWriteObject();
        Iterator<Integer> iterator = getIndexIterator(false);
        while(iterator.hasNext()){
            oOS.writeDouble(getValueAt(iterator.next()));
        }
        
    }

    @Override
    public int getCardinality() {
        return Matrices.cardinality(m);
    }

    @Override
    public boolean isDiagonal() {
        
       if(m instanceof LowerSymmBandMatrix){
            LowerSymmBandMatrix lM = (LowerSymmBandMatrix) m;
            if(lM.numSubDiagonals()==0){
                
                return true;
            }
       }
       
       return false;
        
//        boolean sq = m.isSquare();
//        int cardinality = Matrices.cardinality(m);
//        int cl = m.numColumns();
//        
//       if(sq&&cardinality==cl){
//           int[] rowBandwidth = Matrices.rowBandwidth(m);
//           int[] columnBandwidth = Matrices.columnBandwidth(m);
//           
//           
//           for(int r:rowBandwidth){
//               
//               if(r!=1){
//                   
//                   return false;
//               }
//           }
//           
//           for (int c:columnBandwidth){
//               
//               if(c!=1){
//                   
//                   return false;
//               }
//   
//           }
//           
//           return true;
//           
//       }
//       
//       return false;
    }

    @Override
    public void close() throws IOException {
        
    }

    @Override
    public boolean isSerialized() {
        return false;
    }

    @Override
    public boolean isDense() {
        if(m instanceof DenseMatrix){
            
            return true;
        }
        
        return false;
    }

    @Override
    public void initialiseStorage() {
        
    }
    
    class MTJIterator implements Iterator<Integer> {
        
        //private final Iterator<MatrixEntry> iterator;
        //private MatrixEntry e;
        private final boolean allElements;
        private final int limit;
        private int currIndex;
        
        public MTJIterator(boolean tf){
            this.allElements=tf;
            
            limit = m.numRows()*m.numColumns()-1;
            
            if(allElements||m instanceof DenseMatrix){
                currIndex = -1;
            }else{
                currIndex = -1*(m.numColumns() +1);
            }

        }

        @Override
        public boolean hasNext() {
            return currIndex<limit;
        }

        @Override
        public Integer next() {
            currIndex++;
            

            if(allElements||m instanceof DenseMatrix){
                
               
                
            }else{
                
                currIndex+=m.numColumns();
            }
            
            return currIndex;
        }

        @Override
        public void remove() {
            
        }
        
        
    }
    
}
