/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.lblrtm.lblrtmfilewriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import org.dj.domainmodel.api.DJNodeObject;
import org.dj.domainmodel.api.DJNodeObjectAbstr;
import org.dj.filter.api.Filter;
import org.dj.property.api.DJProperty;
import org.lblrtm.lblrtmfilewriter.api.DJTAPE5;
import org.lblrtm.lblrtmfilewriter.api.Field;
import org.lblrtm.lblrtmfilewriter.api.Record;
import org.lblrtm.lblrtmfilewriter.api.RecordContainer;

/**
 *
 * @author djabry
 */
public abstract class RecordAbstr extends DJNodeObjectAbstr implements Record, Filter{
    
    
    private int order;
    private static final Comparator<DJNodeObject> childComparator = new Comparator<DJNodeObject>(){

            @Override
            public int compare(DJNodeObject o1, DJNodeObject o2) {
                if(o1 instanceof Field&&o2 instanceof Field){
                    Field field2 = (Field) o2;
                    Field field1 = (Field) o1;   
                    return ((Integer)field1.getFieldOrder()).compareTo((Integer)field2.getFieldOrder()); 
                }
                
                return o1.getName().compareTo(o2.getName());
            }
        };
    
    
    
    public RecordAbstr(String name, RecordContainer parent){

        this(name);
//        DJProperty parentProp = getPropertyFactory().createProperty(PROP_PARENT_TAPE5, parent);
//        parentProp.setCanRead(false);
//        setProperty(parentProp);
        
        parent.addChild(this);
        
    }
    
    public RecordAbstr(String name){
        
        super();
        setName(name);
        order = 0;
        
        //Sort children by addition order
        this.setChildComparator(childComparator);
        
        
        
        
    }

    @Override
    public void addChild(DJNodeObject obj) {
        if(obj instanceof Field){
            
            obj.setPropertyValue(Field.PROP_FIELD_ORDER, this.order);
            order++;
        }
        
        super.addChild(obj);
    }
    
    


    @Override
    public List<Field> getFields() {
        List<Field> fields = new ArrayList<Field>();
        for(DJNodeObject obj:this.getChildren()){ 
            if(obj instanceof Field){
                Field field = (Field) obj;  
                fields.add(field);
            } 
        }
        return fields; 
    }

    @Override
    public Field getField(String fieldName) {
        Iterator<DJNodeObject> iterator = getChildren().iterator();
        
        while(iterator.hasNext()){
            DJNodeObject next = iterator.next();
            if(next instanceof Field){
                Field field = (Field) next;
                if(field.getName().equals(fieldName)){ 
                    return field;
                }
            }
        }
        
        return null;
    }

    @Override
    public DJTAPE5 getParentTAPE5() {
        
        DJNodeObject parent = this.getParent();
        
        while(parent!=null&&!(parent instanceof DJTAPE5)){
            
            parent = parent.getParent();
        }

        if(parent instanceof DJTAPE5){
            DJTAPE5 dJTAPE5 = (DJTAPE5) parent;
            
            return dJTAPE5;
            
        }
        
        return null;
        
    }

    @Override
    public boolean filter(Object obj) {
        return isValid();
    }
    
    
    @Override
    public int getLevel(){
        
        return (Integer)getPropertyValue(PROP_LEVEL);
    }

    @Override
    public Record duplicate(RecordContainer parent,final boolean v) {
        
        Record r = new RecordAbstr(getName(),parent) {
            @Override
            public boolean isValid() {
                return v;
            }
        };

        for(Field f:getFields()){
            
            f.duplicate(r); 
        }
        
        return r;
    }

    @Override
    public String getRecordString() {
        
        String s = "";
        
        for(Field f:getFields()){
            s+=f.getValueString();
            
        }
        
        return s;
    }
    
    
    
    
    
}
