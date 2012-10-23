/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmdata.api;

import java.util.Iterator;
import java.util.List;
import javolution.util.FastList;

/**
 *
 * @author djabry
 */
public class WNCollection {
    
    private List<WNRange> wNRanges;
    
    public WNCollection(){
        
        this.wNRanges=new FastList<WNRange>();
        
    }
    
    public void add(WNRange range){
        this.wNRanges.add(range);
    }
    
    public void put(int i, WNRange range){
        
        wNRanges.add(i, range);
    }
    
    public List<WNRange> getRanges(){
        
        return this.wNRanges;
    }
    
    public double getSize(){
        
        Iterator<WNRange> iterator = this.wNRanges.iterator();
        double length = 0.0;
        
        while (iterator.hasNext()){ 
            length+=iterator.next().getLength();
        }
        
        return length;
        
    
    }
    
    
}
