/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dj.domainmodel.api;

import java.util.Set;
import javolution.util.FastSet;
import org.dj.property.api.DJProperty;

/**
 *
 * @author djabry
 */
public abstract class DJObjectManagerAbstr<O extends DJNodeObject> extends DJNodeObjectAbstr implements DJObjectManager<O>{

    
    public DJObjectManagerAbstr(){
        
        DJProperty hProp = getPropertyFactory().createProperty(PROP_HIERARCHY_DIMENSION, DEFAULT_DIMENSION);
        hProp.setCanRead(false);
        putProperty(hProp);
 
    }

    @Override
    public void setHierarchyDimnension(int dim) {
        this.setPropertyValue(PROP_HIERARCHY_DIMENSION, dim);
    }

    
    @Override
    public int getHierarchyDimension() {
        return (Integer) this.getPropertyValue(PROP_HIERARCHY_DIMENSION);
    }

    @Override
    public Set<DJNodeObject> getChildren() {
        
        Set<DJNodeObject> get = this.getAllChildren().get(getHierarchyDimension());
        
        if(get==null){
            
            get = new FastSet<DJNodeObject>();
        }
        return get;
    }

    @Override
    public void addChild(DJNodeObject obj) {
        this.addChild(obj, getHierarchyDimension());
    }

    @Override
    public void removeChild(DJNodeObject obj) {
        this.removeChild(obj, getHierarchyDimension());
    }
    

    @Override
    public Set<O> getManagedChildren(){
        
       Set<O> managedChildren = new FastSet(this.getChildren());
       return managedChildren;

    }



    
    
    
    
}
