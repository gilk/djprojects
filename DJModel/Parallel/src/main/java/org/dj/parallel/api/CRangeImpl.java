/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.parallel.api;

import org.dj.parallel.api.ContinuousRange;

/**
 *
 * @author djabry
 */
public class CRangeImpl implements ContinuousRange {
    
    public static final double DEFAULT_TOLERANCE =1E-6;
    
    private double start;
    private double end;
    
    public CRangeImpl(double start, double end){
        
        this.start=start;
        this.end=end;
        
    }

    @Override
    public double getStart() {
        return start;
    }

    @Override
    public double getEnd() {
        return end;
    }

    @Override
    public double getSize() {
        return getEnd()-getStart();
    }

    /**
     * @param start the start to set
     */
    @Override
    public void setStart(double start) {
        this.start = start;
    }

    /**
     * @param end the end to set
     */
    @Override
    public void setEnd(double end) {
        this.end = end;
    }
    
    private boolean isInRange(double v){
        
        if(v>=this.getStart()&&v<=this.getEnd()){
            
            return true;
        }
        
        return false;
    }
    
    

    @Override
    public boolean intersects(ContinuousRange r) {
        
        if(isInRange(r.getStart())||isInRange(r.getEnd())){
            
            return true;
        }
        
        return false;
    }

    @Override
    public boolean contains(ContinuousRange r) {
        if(isInRange(r.getStart())&&isInRange(r.getEnd())){
            
            return true;
        }
        
        return false;
    }

    @Override
    public boolean equals(Object o) {
        
        if(o instanceof ContinuousRange){
            ContinuousRange range = (ContinuousRange) o;
            if(range.getStart()==this.getStart()
                    &&range.getEnd()==this.getEnd()){
                
                return true;
            }
            
            return false;
        }
        
        
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return ((Double)this.getEnd()).hashCode()+((Double)this.getStart()).hashCode();
    }
    

    @Override
    public ContinuousRange duplicate() {
        return new CRangeImpl(this.getStart(),this.getEnd());
    }
    
}
