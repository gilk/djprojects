/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.matrix.api;


import ucar.ma2.IndexIterator;

/**
 *
 * @author djabry
 */
public class DJIndexIterator implements IndexIterator{
    
    private final DJMatrixIterator i;
    private final DJMatrix m;
    private double currentVal;
    
    
    public DJIndexIterator(DJMatrix m){
        
        this.m=m;
        this.i=m.iterator(true);
     
    }

    @Override
    public boolean hasNext() {
        return i.hasNext();
    }

    @Override
    public double getDoubleNext() {
       
        return (Double)next();
    }

    @Override
    public void setDoubleNext(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public double getDoubleCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDoubleCurrent(double d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloatNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFloatNext(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public float getFloatCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setFloatCurrent(float f) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLongNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLongNext(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getLongCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLongCurrent(long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getIntNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIntNext(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getIntCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIntCurrent(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getShortNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setShortNext(short s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public short getShortCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setShortCurrent(short s) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByteNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByteNext(byte b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public byte getByteCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setByteCurrent(byte b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char getCharNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCharNext(char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public char getCharCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCharCurrent(char c) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBooleanNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBooleanNext(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean getBooleanCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBooleanCurrent(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getObjectNext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setObjectNext(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object getObjectCurrent() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setObjectCurrent(Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Object next() {
        currentVal = i.next();
        
        //System.out.println("Next value = "+currentVal);
        return currentVal;
    }

    @Override
    public int[] getCurrentCounter() {
        throw new UnsupportedOperationException("Not supported yet.");
//        int[] cC;
//        if(m.isVector()){
//            
//            cC = new int[]{Math.max(i.getRow(), i.getColumn())};
//        }else{
//             cC = new int[]{i.getRow(),i.getColumn()};
//            
//        }
//        
//       
//        return cC;
        
        
    }
    
}
