/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.worldwindmodel.globe.api;

import java.beans.PropertyChangeEvent;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJObjectManager;
import org.dj.domainmodel.api.DJObjectManagerAbstr;
import org.dj.listener.api.ListenerAbstr;
import org.dj.property.api.DJProperty;
import org.dj.worldwindmodel.object.api.WWDJObject;

/**
 *
 * @author djabry
 */
public class GlobeManagerAbstr extends DJObjectManagerAbstr<WWDJObject> implements GlobeManager{
    
    private final ChildUpdater cU = new ChildUpdater(); 
    
    public GlobeManagerAbstr(){
        //This step allows the same Object to be added to this manager without removing it from the original
        this.setHierarchyDimnension(GLOBE_DIMENSION);
        
        DJProperty<Date> tProp = getPropertyFactory().createProperty(PROP_TIME, Calendar.getInstance().getTime());
        tProp.setDisplayName("Time");
        tProp.setNotify(true);
        this.putProperty(tProp);
        
    }
    

    @Override
    public Date getTime() {
        return (Date) this.getPropertyValue(PROP_TIME);
    }

    @Override
    public void setTime(Date d) {
        this.setPropertyValue(PROP_TIME, d);
    }

    @Override
    public void addChild(DJNodeObject obj, Integer dim, boolean notify) {
        //Allow all children to listen to time changes in globe manager
        super.addChild(obj, dim, notify);
        this.getProperty(PROP_TIME).addPropertyChangeListener(obj);
        
    }

    @Override
    public void removeChild(DJNodeObject obj, Integer dim, boolean notify) {
        super.removeChild(obj, dim, notify);
        this.getProperty(PROP_TIME).removePropertyChangeListener(obj);
    }
    
    private class ChildUpdater extends ListenerAbstr {
        

        @Override
        public void propertyChange(PropertyChangeEvent pce) {
            if(pce.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_ADDED)){
                
                //Need to decide how to remove children also
                DJNodeObject child = (DJNodeObject) pce.getNewValue();
                if(child instanceof WWDJObject){
                    
                    addChild(child);
                }
                
                
            }else if(pce.getPropertyName().equals(DJNodeObject.MESSAGE_CHILD_REMOVED)){
                
                DJNodeObject child = (DJNodeObject) pce.getNewValue();
                if(child instanceof WWDJObject){
                    
                    removeChild(child);
                }
                
            }
            
            super.propertyChange(pce);
        }
  
    }

    @Override
    public void attachSource(DJObjectManager source) {
        source.addPropertyChangeListener(cU);
    }

    @Override
    public void removeSource(DJObjectManager source) {
        source.removePropertyChangeListener(cU);
    }
    
    
    

    
    
      
    
}
