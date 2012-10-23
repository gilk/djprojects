/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;

import java.nio.ByteBuffer;
import java.util.List;
import ucar.ma2.*;

/**
 *
 * @author djabry
 */
public class DJMatrixArrayWrapper extends ArrayDouble{
    
    private final DJMatrix child;

    
    public DJMatrixArrayWrapper(DJMatrix ch){
        
        super(new int[]{0,0});
        this.child=ch;
    }

    @Override
    public double get(Index i) {
        
        throw new UnsupportedOperationException("Not supported yet.");

    }

    @Override
    public Object getObject(Index i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    

    private static int[] shapeFromMatrix(DJMatrix m){
        
        int[] shape;
        
        //if(m.isVector()){
        //    shape = new int[]{m.getNumElements()};
            
        //}else{
            
            shape = new int[]{m.getNumberOfRows(),m.getNumberOfColumns()};
        //}
        
        return shape;
        
        
    }

    @Override
    public Class getElementType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    

    @Override
    public Object getStorage() {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public double getDouble(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDouble(Index index, double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloat(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFloat(Index index, float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLong(Index index, long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInt(Index index, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getShort(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setShort(Index index, short s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByte(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByte(Index index, byte b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char getChar(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChar(Index index, char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBoolean(Index index) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBoolean(Index index, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setObject(Index index, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDouble(int i) {
        //System.out.println("getting value at"+i+" = "+ child.getMatrixStorage().getValueAt(i));
        //return child.getMatrixStorage().getValueAt(i);
        
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDouble(int i, double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloat(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFloat(int i, float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLong(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLong(int i, long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getInt(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setInt(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getShort(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setShort(int i, short s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByte(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByte(int i, byte b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char getChar(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setChar(int i, char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBoolean(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBoolean(int i, boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getObject(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setObject(int i, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int[] getShape() {
        return shapeFromMatrix(child);
    }

    @Override
    public long getSize() {
         throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Index getIndexPrivate() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ByteBuffer getDataAsByteBuffer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Index getIndex() {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    public Index getIndexCalc() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexIterator getIndexIterator() {
        return new DJIndexIterator(this.child);
    }

    @Override
    public IndexIterator getRangeIterator(List<Range> ranges) throws InvalidRangeException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IndexIterator getIndexIteratorFast() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object get1DJavaArray(Class wantType) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Array copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object copyTo1DJavaArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object copyToNDJavaArray() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean hasNext() {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object next() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double nextDouble() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    
    
    

    @Override
    public int getRank() {
//        if(child.isVector()){
//            
//            return 1;
//        }
        
        return 2;
    }

    public double[] getStorageD() {
       throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getSizeBytes() {
       throw new UnsupportedOperationException("Not supported yet.");
    }


    
    
    
    
    
    
}
