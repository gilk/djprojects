/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.task.editor;

import java.util.Iterator;
import java.util.List;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManagerAbstr;

/**
 *
 * @author djabry
 */
public class DoubleListManager extends DJObjectManagerAbstr<DoubleEntry> implements DJNodeObject.Source{
    private final List<Double> dblList;
    
    public DoubleListManager(List<Double> dblList){
       
        
        this.dblList=dblList;
        pullValuesFromList();
    }
    
    
    
    public void pullValuesFromList(){
        Iterator<DJNodeObject> iterator = this.getChildren().iterator();
        while(iterator.hasNext()){
            this.removeChild(iterator.next());
        }
        
        for(Double val:dblList){
            this.addChild(new DoubleEntry(val));
            
        }
        
    }
    
    public void pushValuesToList(){
        
        this.dblList.clear();
        Iterator<DJNodeObject> iterator = this.getChildren().iterator();
        while(iterator.hasNext()){
            DJNodeObject next = iterator.next();
            if(next instanceof DoubleEntry){
                DoubleEntry dE = (DoubleEntry) next;
                dblList.add(dE.getDoubleValue());
                
                
            }
            
            
        }
        
    }
    
    

    @Override
    public DJNodeObject createObject() {
        Iterator<DJNodeObject> iterator = this.getChildren().iterator();
        double minVal = Double.POSITIVE_INFINITY;
        double maxVal = Double.NEGATIVE_INFINITY;
        int numChildren = 0;
        while(iterator.hasNext()){
            DJNodeObject next = iterator.next();
            if(next instanceof DoubleEntry){
                numChildren++;
                DoubleEntry doubleEntry = (DoubleEntry) next;
                double val = doubleEntry.getDoubleValue();
                if(val<minVal){
                    
                    minVal  = val;
                }
                
                if(val>maxVal){
                    
                    maxVal=val;
                }
                
            }
            
            
            
        }
        
        if(numChildren==0){
            
            maxVal=-1.0;
            minVal=1.0;
        }
        
        
        DoubleEntry dE = new DoubleEntry(maxVal+1.0);
        this.addChild(dE);
        return dE;
        
    }
     
}
